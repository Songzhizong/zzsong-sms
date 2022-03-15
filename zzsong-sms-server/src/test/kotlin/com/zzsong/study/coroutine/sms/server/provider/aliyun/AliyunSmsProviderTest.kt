package com.zzsong.study.coroutine.sms.server.provider.aliyun

import cn.idealframework.kotlin.toJsonString
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Ignore

/**
 * @author 宋志宗 on 2022/1/28
 */
@Ignore
class AliyunSmsProviderTest {
  companion object {
    private val properties = AliYunProperties()
      .also {
        it.signName = "xxx"
        it.accessKeyId = "xxx"
        it.accessSecret = "xxx"
      }
    private val provider: AliyunSmsProvider = AliyunSmsProvider(properties)
  }

  @org.junit.Test
  fun send() = runBlocking {
    val template = ProviderTemplateDo()
      .also {
        it.templateId = 269454731720523776
        it.setProviderCode("ali_yun")
        it.setProviderTemplate("SMS_201125008")
        it.setContent("验证码\${code}，您正在进行身份验证，打死不要告诉别人哦！")
      }
    val request = SendRequest()
      .also {
        it.template = template
        it.mobiles = listOf("18256928780")
        it.params = mapOf("code" to "1825")
      }
    val send = provider.send(request)
    println(send.toJsonString())
  }
}
