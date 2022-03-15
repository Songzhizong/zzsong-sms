package com.zzsong.study.coroutine.sms.server.provider.mock

import com.zzsong.study.coroutine.sms.server.configure.SmsServerProperties
import com.zzsong.study.coroutine.sms.server.provider.SendRequest
import com.zzsong.study.coroutine.sms.server.provider.SendResult
import com.zzsong.study.coroutine.sms.server.provider.SmsProvider
import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

/**
 * @author 宋志宗 on 2022/1/29
 */
@Component("smsMockSmsProvider")
@ConditionalOnExpression("\${sms.server.mock.enabled:false}")
class MockSmsProvider(smsServerProperties: SmsServerProperties) : SmsProvider {
  companion object {
    const val CODE = "mock"
  }

  private val minDelay =
    min(smsServerProperties.mock.minDelay, smsServerProperties.mock.maxDelay)
  private val maxDelay =
    max(smsServerProperties.mock.minDelay, smsServerProperties.mock.maxDelay)

  override fun getCode() = CODE

  override fun getName() = CODE

  override suspend fun send(request: SendRequest): List<SendResult> {
    val random = ThreadLocalRandom.current().nextLong(minDelay, maxDelay)
    delay(random)
    val mobiles = request.mobiles
    val template = request.template
    val templateId = template.templateId
    val content = template.content
    val uuid = UUID.randomUUID().toString()
    return mobiles.map { mobile ->
      SendResult().also {
        it.success = true
        it.mobile = mobile
        it.templateId = templateId
        it.content = content
        it.description = "success"
        it.providerCode = CODE
        it.providerSerialNumber = uuid
      }
    }
  }
}
