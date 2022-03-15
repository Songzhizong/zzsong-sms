package com.zzsong.study.coroutine.sms.server.domain.model.template.impl

import cn.idealframework.id.IDGenerator
import cn.idealframework.id.IDGeneratorFactory
import cn.idealframework.kotlin.toPageable
import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateDo
import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateRepository
import com.zzsong.study.coroutine.sms.server.domain.model.template.args.QueryTemplateArgs
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository

/**
 * @author 宋志宗 on 2022/1/27
 */
@Repository
class SmsTemplateRepositoryImpl(
  idGeneratorFactory: IDGeneratorFactory,
  private val template: R2dbcEntityTemplate,
  private val repository: R2dbcSmsTemplateRepository
) : SmsTemplateRepository {
  private val idGenerator: IDGenerator = idGeneratorFactory.getGenerator("database")

  override suspend fun save(smsTemplateDo: SmsTemplateDo): SmsTemplateDo {
    if (smsTemplateDo.id < 1) {
      smsTemplateDo.id = idGenerator.generate()
      return template.insert(smsTemplateDo).awaitSingle()
    }
    return template.update(smsTemplateDo).awaitSingle()
  }

  override suspend fun findById(id: Long): SmsTemplateDo? {
    return repository.findById(id).awaitSingleOrNull()
  }

  override suspend fun findByCode(code: String): SmsTemplateDo? {
    return repository.findByCode(code).awaitSingleOrNull()
  }

  override suspend fun findAllByCodeIn(codes: Collection<String>): List<SmsTemplateDo> {
    return repository.findAllByCodeIn(codes)
      .collectList().awaitSingleOrNull() ?: listOf()
  }

  override suspend fun findAll(): List<SmsTemplateDo> {
    return repository.findAll().collectList().awaitSingleOrNull() ?: emptyList()
  }

  override suspend fun delete(templateDo: SmsTemplateDo) {
    repository.delete(templateDo).awaitSingleOrNull()
  }

  override suspend fun query(args: QueryTemplateArgs): Page<SmsTemplateDo> {
    val paging = args.paging
    val code = args.code
    var criteria = Criteria.empty()
    if (code != null && code.isNotBlank()) {
      criteria = criteria.and("code").like("$code%")
    }
    val countQuery = Query.query(criteria)
    val selectQuery = countQuery
      .sort(Sort.by("id").descending())
      .offset(paging.offset)
      .limit(paging.pageSize)
    return coroutineScope {
      val count = async {
        template
          .count(countQuery, SmsTemplateDo::class.java)
          .awaitSingleOrNull() ?: 0
      }
      val content = async {
        template.select(selectQuery, SmsTemplateDo::class.java)
          .collectList().awaitSingleOrNull() ?: listOf()
      }
      PageImpl(content.await(), paging.toPageable(), count.await())
    }
  }
}
