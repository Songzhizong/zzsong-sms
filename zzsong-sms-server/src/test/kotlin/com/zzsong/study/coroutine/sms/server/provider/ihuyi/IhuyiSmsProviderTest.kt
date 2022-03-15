package com.zzsong.study.coroutine.sms.server.provider.ihuyi

import cn.idealframework.kotlin.toJsonString
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

/**
 * @author 宋志宗 on 2022/1/28
 */
@Ignore
class IhuyiSmsProviderTest {
  companion object {
    private val properties = IhuyiProperties().also {
      it.appId = "xxx";it.appKey = "xxx"
    }
    private val provider = IhuyiSmsProvider(properties)
  }

  @Test
  fun send() = runBlocking {
    val template = ProviderTemplateDo()
      .also {
        it.templateId = 269454731720523776
        it.setProviderCode("ihuyi")
        it.setProviderTemplate("")
        it.setContent("您的验证码是：\${code}。请不要把验证码泄露给其他人。")
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
