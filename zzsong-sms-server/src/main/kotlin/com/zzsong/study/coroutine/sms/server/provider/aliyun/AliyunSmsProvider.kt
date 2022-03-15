package com.zzsong.study.coroutine.sms.server.provider.aliyun

import cn.idealframework.extensions.reactor.FullWebClientOptions
import cn.idealframework.extensions.reactor.Reactors
import cn.idealframework.kotlin.parseJson
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import com.zzsong.study.coroutine.sms.server.provider.SendResult
import com.zzsong.study.coroutine.sms.server.provider.SmsProvider
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.stereotype.Component
import org.springframework.web.util.DefaultUriBuilderFactory
import java.time.Duration

/**
 * 阿里云短信服务实现
 *
 * @author 宋志宗 on 2022/1/28
 */
@Component("AliyunSmsProvider")
@ConditionalOnExpression("T(cn.idealframework.lang.StringUtils).isNotBlank('\${sms.server.provider.ali-yun.access-secret:}')")
class AliyunSmsProvider(private val properties: AliYunProperties) : SmsProvider {
  companion object {
    const val CODE = "ali_yun"
    const val SUCCESS_CODE = "OK"
    private val log: Logger = LoggerFactory.getLogger(AliyunSmsProvider::class.java)
    private val webClient = Reactors
      .webClient(FullWebClientOptions()
        .also {
          it.responseTimeout = Duration.ofSeconds(5)
          it.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        })
  }

  override fun getCode() = CODE

  override fun getName() = "阿里云"

  override suspend fun send(request: SendRequest): List<SendResult> {
    val template = request.template
    val mobiles = request.mobiles
    val params = request.params ?: mapOf()
    val code = template.providerCode
    val content = formatContent(template.content, params)
    var success: Boolean
    var message: String
    var providerSerialNumber = ""
    try {
      val url = AliyunSmsUtils
        .createUrl(
          properties.baseUrl,
          properties.regionId,
          properties.signName,
          properties.accessKeyId,
          properties.accessSecret,
          code, mobiles, params
        )
      log.debug("url: {}", url)
      val response = webClient.get().uri(url)
        .exchangeToMono { cr -> cr.bodyToMono(String::class.java) }
        .awaitSingleOrNull() ?: ""
      log.debug("阿里云短信服务响应结果: {}", response)
      val aliYunResponse = response.parseJson(AliYunResponse::class.java)
      success = true
      message = "success"
      providerSerialNumber = aliYunResponse.requestId ?: "-1"
      val respCode = aliYunResponse.code
      if (SUCCESS_CODE != respCode) {
        success = false
        message = aliYunResponse.message ?: "未知的失败原因"
      }
    } catch (e: Exception) {
      success = false
      message = e.javaClass.name + ":" + e.message
      log.info("阿里云短信发送异常: {}", message)
    }
    return mobiles.map { mobile ->
      SendResult()
        .also {
          it.success = success
          it.mobile = mobile
          it.content = content
          it.description = message
          it.templateId = template.templateId
          it.providerCode = CODE
          it.providerSerialNumber = providerSerialNumber
        }
    }
  }

  override suspend fun send(requests: Collection<SendRequest>): List<SendResult> {
    return super.send(requests)
  }
}
