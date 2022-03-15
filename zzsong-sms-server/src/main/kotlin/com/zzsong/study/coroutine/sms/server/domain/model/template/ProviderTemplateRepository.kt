package com.zzsong.study.coroutine.sms.server.domain.model.template

/**
 * @author 宋志宗 on 2022/1/27
 */
interface ProviderTemplateRepository {

  /** 保存 */
  suspend fun save(providerTemplateDo: ProviderTemplateDo): ProviderTemplateDo

  /** 获取所有默认服务商模板 */
  suspend fun findAllDefault(): List<ProviderTemplateDo>

  /** 获取指定短信模板的默认服务商模板 */
  suspend fun findTemplateDefault(templateId: Long): ProviderTemplateDo?

  /** 获取指定模板的所有实现 */
  suspend fun findAllByTemplateId(templateId: Long): List<ProviderTemplateDo>

  /** 删除 */
  suspend fun delete(providerTemplateDo: ProviderTemplateDo)
}
