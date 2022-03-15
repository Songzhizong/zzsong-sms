package com.zzsong.study.coroutine.sms.server.application

import cn.idealframework.util.Asserts
import com.zzsong.study.coroutine.sms.server.application.args.CreateProviderTemplateArgs
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateCache
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateRepository
import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateRepository
import com.zzsong.study.coroutine.sms.server.provider.Provider
import com.zzsong.study.coroutine.sms.server.provider.SmsProviderRegistry
import org.springframework.stereotype.Service

/**
 * @author 宋志宗 on 2022/1/28
 */
@Service
class ProviderService(
  private val providerRegistry: SmsProviderRegistry,
  private val smsTemplateRepository: SmsTemplateRepository,
  private val providerTemplateCache: ProviderTemplateCache,
  private val providerTemplateRepository: ProviderTemplateRepository
) {

  /**
   * 获取所有可用的服务商
   *
   * @author 宋志宗 on 2022/1/28
   */
  fun findAllActiveProvider(): List<Provider> {
    return providerRegistry.getAllProvider()
  }

  suspend fun createTemplate(args: CreateProviderTemplateArgs): ProviderTemplateDo {
    val templateId = args.templateId
    Asserts.nonnull(templateId, "短信模板id不能为空")
    val templateDo = smsTemplateRepository.findRequiredById(templateId!!)
    val defaultImpl = providerTemplateRepository.findTemplateDefault(templateId)
    val providerTemplateDo = ProviderTemplateDo.create(
      defaultImpl == null,
      templateDo,
      args.providerCode,
      args.providerTemplate,
      args.content
    )
    providerTemplateRepository.save(providerTemplateDo)
    return providerTemplateDo
  }

  suspend fun refreshCache() {
    providerTemplateCache.refresh()
  }
}
