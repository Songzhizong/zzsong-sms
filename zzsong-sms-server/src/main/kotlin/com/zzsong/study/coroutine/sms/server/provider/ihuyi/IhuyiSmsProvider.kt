package com.zzsong.study.coroutine.sms.server.provider.ihuyi

import cn.idealframework.extensions.reactor.FullWebClientOptions
import cn.idealframework.extensions.reactor.Reactors
import cn.idealframework.json.JsonUtils
import cn.idealframework.util.Asserts
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import com.zzsong.study.coroutine.sms.server.provider.SendResult
import com.zzsong.study.coroutine.sms.server.provider.SmsProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import java.time.Duration

/**
 * @author 宋志宗 on 2022/1/28
 */
@Component("smsIhuyiSmsProvider")
@ConditionalOnExpression("T(cn.idealframework.lang.StringUtils).isNotBlank('\${sms.server.provider.ihuyi.app-key:}')")
class IhuyiSmsProvider(private val properties: IhuyiProperties) : SmsProvider {
  companion object {
    const val CODE = "ihuyi"
    const val SUCCESS_CODE = "2"
    private val log: Logger = LoggerFactory.getLogger(IhuyiSmsProvider::class.java)
    private val webClient = Reactors.webClient(FullWebClientOptions()
      .also {
        it.responseTimeout = Duration.ofSeconds(2)
        it.isKeepAlive = true
      })
  }

  init {
    Asserts.notBlank(properties.baseUrl, "ihuyi baseUrl 为空")
    Asserts.notBlank(properties.appId, "ihuyi appId 为空")
    Asserts.notBlank(properties.appKey, "ihuyi appKey 为空")
  }

  override fun getCode() = CODE

  override fun getName() = "互亿无线"

  override suspend fun send(request: SendRequest): List<SendResult> {
    val template = request.template
    val mobiles = request.mobiles
    val params = request.params
    val content = template.content
    val formatContent = formatContent(content, params)
    return coroutineScope {
      mobiles.map { async { doSend(it, formatContent, template) } }.map { it.await() }
    }
  }

  /**
   * 执行短信发送操作
   *
   * @param mobile        目标手机号
   * @param formatContent 已格式化的短信内容
   * @param template      供应商模板信息
   * @author 宋志宗 on 2022/1/28
   */
  private suspend fun doSend(
    mobile: String,
    formatContent: String,
    template: ProviderTemplateDo
  ): SendResult {
    var success: Boolean
    var message: String
    var sid: String
    try {
      val body = buildBody(mobile, formatContent)
      val result = request(body)
      val ihuyiResponse = JsonUtils.parse(result, IhuyiResponse::class.java)
      message = ihuyiResponse.msg ?: "success"
      sid = ihuyiResponse.smsid ?: "-1"
      val code = ihuyiResponse.code ?: "500"
      if (SUCCESS_CODE == code) {
        success = true
        message = ihuyiResponse.msg ?: "success"
      } else {
        success = false
        message = ihuyiResponse.msg ?: "未知失败原因"
      }
      message = "$code: $message"
    } catch (e: Exception) {
      success = false
      message = e.javaClass.name + ": " + e.message
      sid = "-1"
      log.info("互亿无线调用出现异常: {}", message)
    }
    return SendResult().also {
      it.success = success
      it.mobile = mobile
      it.templateId = template.templateId
      it.content = formatContent
      it.description = message
      it.providerCode = template.providerCode
      it.providerSerialNumber = sid
    }
  }

  /**
   * 构造请求体
   *
   * @author 宋志宗 on 2022/1/28
   */
  private fun buildBody(mobile: String, content: String): MultiValueMap<String, String> {
    return LinkedMultiValueMap<String, String>()
      .also {
        it.add("method", "Submit")
        it.add("format", "json")
        it.add("account", properties.appId)
        it.add("password", properties.appKey)
        it.add("mobile", mobile)
        it.add("content", content)
      }
  }

  /**
   * 发送http请求
   *
   * @author 宋志宗 on 2022/1/28
   */
  private suspend fun request(body: MultiValueMap<String, String>) =
    webClient.post().uri(properties.baseUrl)
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(BodyInserters.fromFormData(body))
      .exchangeToMono { it.bodyToMono(String::class.java) }
      .awaitSingleOrNull() ?: "{\"code\":500,\"msg\":\"返回结果为null\",\"smsid\":\"-1\"}"
}
