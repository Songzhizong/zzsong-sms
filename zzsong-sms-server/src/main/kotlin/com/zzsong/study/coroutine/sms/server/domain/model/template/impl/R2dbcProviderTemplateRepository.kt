package com.zzsong.study.coroutine.sms.server.domain.model.template.impl

import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author 宋志宗 on 2022/1/27
 */
@Repository
interface R2dbcProviderTemplateRepository : R2dbcRepository<ProviderTemplateDo, Long> {

  fun findAllByDefaultFlag(flag: String): Flux<ProviderTemplateDo>

  fun findAllByTemplateId(templateId: Long): Flux<ProviderTemplateDo>

  fun findByTemplateIdAndDefaultFlag(templateId: Long, flag: String): Mono<ProviderTemplateDo>
}
