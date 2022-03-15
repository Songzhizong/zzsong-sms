package com.zzsong.study.coroutine.sms.server.application

import cn.idealframework.id.IDGenerator
import cn.idealframework.id.IDGeneratorFactory
import cn.idealframework.kotlin.toJsonString
import cn.idealframework.lang.Lists
import cn.idealframework.util.Asserts
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs
import com.zzsong.study.coroutine.sms.server.domain.model.task.SendLogDo
import com.zzsong.study.coroutine.sms.server.domain.model.task.SendLogRepository
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateCache
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import com.zzsong.study.coroutine.sms.server.provider.SendResult
import com.zzsong.study.coroutine.sms.server.provider.SmsProviderRegistry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 宋志宗 on 2022/1/27
 */
@Service
class SmsService(
  idGeneratorFactory: IDGeneratorFactory,
  private val sendLogRepository: SendLogRepository,
  private val smsProviderRegistry: SmsProviderRegistry,
  private val providerTemplateCache: ProviderTemplateCache,
) {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SmsService::class.java)
  }

  /** 模板编码生成器 */
  private val idGenerator: IDGenerator = idGeneratorFactory.getGenerator("sms_task")

  /**
   * 发送短信
   *
   * @param args 发送短信参数
   * @author 宋志宗 on 2022/1/27
   */
  @DelicateCoroutinesApi
  suspend fun send(args: SendSmsArgs): Long {
    Asserts.notEmpty(args.mobiles, "手机号码列表不能为空")
    val taskId = idGenerator.generate()
    val code = args.templateCode
    val templateDo = providerTemplateCache.findByCode(code)
    val request = SendRequest()
    request.template = templateDo
    request.mobiles = args.mobiles
    request.params = args.params
    val providerCode = templateDo.providerCode
    val provider = smsProviderRegistry.getRequired(providerCode)
    GlobalScope.launch(Dispatchers.Default) {
      val results = try {
        provider.send(request)
      } catch (e: Exception) {
        // 正常情况下供应商实现中不应该抛出异常
        log.error(
          "task: {} provider: {} request: {} 执行短信发送出现异常",
          taskId,
          providerCode,
          request.toJsonString(),
          e
        )
        emptyList()
      }
      saveSendLogs(taskId, results)
    }
    return taskId
  }

  /**
   * 批量发送
   *
   * @param argsList 短信参数列表
   * @author 宋志宗 on 2022/1/27
   */
  @DelicateCoroutinesApi
  suspend fun send(argsList: List<SendSmsArgs>): Long {
    val taskId = idGenerator.generate()
    // 对请求对象进行转换
    val requestList = argsList
      .mapNotNull { args ->
        if (Lists.isEmpty(args.mobiles)) {
          null
        } else {
          val request = SendRequest()
          request.template = providerTemplateCache.findByCode(args.templateCode)
          request.mobiles = args.mobiles
          request.params = args.params
          request
        }
      }
    // 按服务商编码对请求进行分组发送
    val providerMap = requestList.groupBy { it.template.providerCode }
    GlobalScope.launch(Dispatchers.Default) {
      val results = providerMap
        .mapNotNull { (providerCode, requests) ->
          val provider = smsProviderRegistry.get(providerCode)
          if (provider == null) {
            log.error("无法通过提供商编码获取实现: {}", providerCode)
            null
          } else {
            try {
              provider.send(requests)
            } catch (e: Exception) {
              // 正常情况下供应商实现中不应该抛出异常
              log.error(
                "task: {} provider: {} requests: {} 执行短信发送出现异常",
                taskId,
                providerCode,
                requests.toJsonString(),
                e
              )
              emptyList()
            }
          }
        }.flatten()
      saveSendLogs(taskId, results)
    }
    return taskId
  }

  /**
   * 保存短信发送日志
   *
   * @author 宋志宗 on 2022/1/28
   */
  private suspend fun saveSendLogs(taskId: Long, resultList: List<SendResult>) {
    val sendLogs = resultList.map {
      val mobile = it.mobile
      val success = it.success
      val description = it.description
      val msg = if (success) "成功" else "失败"
      log.info("{} -> {} 短信发送{} {}", taskId, mobile, msg, description)
      SendLogDo.create(taskId, it)
    }
    try {
      sendLogRepository.saveAll(sendLogs)
    } catch (e: Exception) {
      log.warn("task: {} 保存发送日志出现异常", taskId, e)
    }
  }
}
