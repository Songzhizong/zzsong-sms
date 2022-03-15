package com.zzsong.study.coroutine.sms.server.domain.model.template.impl

import cn.idealframework.id.IDGenerator
import cn.idealframework.id.IDGeneratorFactory
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

/**
 * @author 宋志宗 on 2022/1/28
 */
@Repository
class ProviderTemplateRepositoryImpl(
  idGeneratorFactory: IDGeneratorFactory,
  private val template: R2dbcEntityTemplate,
  private val repository: R2dbcProviderTemplateRepository
) : ProviderTemplateRepository {
  private val idGenerator: IDGenerator = idGeneratorFactory.getGenerator("database")

  override suspend fun save(providerTemplateDo: ProviderTemplateDo): ProviderTemplateDo {
    if (providerTemplateDo.id < 1) {
      providerTemplateDo.id = idGenerator.generate()
      return template.insert(providerTemplateDo).awaitSingle()
    }
    return template.update(providerTemplateDo).awaitSingle()
  }

  override suspend fun findAllDefault(): List<ProviderTemplateDo> {
    return repository.findAllByDefaultFlag(ProviderTemplateDo.DEFAULT_FLAG)
      .collectList().awaitSingleOrNull() ?: listOf()
  }

  override suspend fun findTemplateDefault(templateId: Long): ProviderTemplateDo? {
    val flag = ProviderTemplateDo.DEFAULT_FLAG
    return repository.findByTemplateIdAndDefaultFlag(templateId, flag).awaitSingleOrNull()
  }

  override suspend fun findAllByTemplateId(templateId: Long): List<ProviderTemplateDo> {
    return repository.findAllByTemplateId(templateId).collectList().awaitSingleOrNull() ?: listOf()
  }

  override suspend fun delete(providerTemplateDo: ProviderTemplateDo) {
    repository.delete(providerTemplateDo).awaitSingleOrNull()
  }
}
