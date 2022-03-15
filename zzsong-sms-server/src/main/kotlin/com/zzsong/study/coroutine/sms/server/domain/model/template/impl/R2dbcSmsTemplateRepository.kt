package com.zzsong.study.coroutine.sms.server.domain.model.template.impl

import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateDo
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author 宋志宗 on 2022/1/27
 */
@Repository
interface R2dbcSmsTemplateRepository : R2dbcRepository<SmsTemplateDo, Long> {

  fun findByCode(code: String): Mono<SmsTemplateDo>

  fun findAllByCodeIn(codes: Collection<String>): Flux<SmsTemplateDo>
}
