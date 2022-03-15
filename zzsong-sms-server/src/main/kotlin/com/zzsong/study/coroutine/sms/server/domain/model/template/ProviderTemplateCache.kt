package com.zzsong.study.coroutine.sms.server.domain.model.template

import cn.idealframework.transmission.exception.ResourceNotFoundException
import com.zzsong.study.coroutine.sms.server.configure.SmsServerProperties
import com.zzsong.study.coroutine.sms.server.provider.SmsProviderRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author 宋志宗 on 2022/1/28
 */
@Component
class ProviderTemplateCache(
  smsServerProperties: SmsServerProperties,
  private val providerRegistry: SmsProviderRegistry,
  private val providerTemplateRepository: ProviderTemplateRepository
) {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(ProviderTemplateCache::class.java)
  }

  private val mockEnabled = smsServerProperties.mock.enabled

  @Volatile
  private var templateMap = mapOf<String, ProviderTemplateDo>()

  fun findByCode(code: String): ProviderTemplateDo {
    if (mockEnabled) {
      return mockTemplate(code)
    }
    val providerTemplateDo = templateMap[code]
    if (providerTemplateDo == null) {
      val message = "模板: $code 没有默认实现"
      log.warn(message)
      throw ResourceNotFoundException(message)
    }
    return providerTemplateDo
  }

  private fun mockTemplate(code: String): ProviderTemplateDo {
    return ProviderTemplateDo.createMock(code)
  }

  /**
   * 刷新默认模板缓存
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun refresh() {
    if (mockEnabled) {
      return
    }
    val allDefault = providerTemplateRepository.findAllDefault()
    for (templateDo in allDefault) {
      val providerCode = templateDo.providerCode
      val provider = providerRegistry.get(providerCode)
      if (provider == null) {
        log.error("无法通过提供商编码获取实现: {}", providerCode)
      }
    }
    templateMap = allDefault.associateBy { it.templateCode }
    log.debug("刷新: {}条默认模板实现", allDefault.size)
  }
}
