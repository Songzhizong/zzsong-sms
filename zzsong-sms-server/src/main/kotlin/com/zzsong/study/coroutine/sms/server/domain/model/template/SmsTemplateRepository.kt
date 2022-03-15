package com.zzsong.study.coroutine.sms.server.domain.model.template

import cn.idealframework.transmission.exception.ResourceNotFoundException
import com.zzsong.study.coroutine.sms.server.domain.model.template.args.QueryTemplateArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page

/**
 * @author 宋志宗 on 2022/1/27
 */
interface SmsTemplateRepository {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SmsTemplateRepository::class.java)
  }

  suspend fun save(smsTemplateDo: SmsTemplateDo): SmsTemplateDo

  /** 通过id查询 */
  suspend fun findById(id: Long): SmsTemplateDo?

  suspend fun findRequiredById(id: Long): SmsTemplateDo {
    val templateDo = findById(id)
    if (templateDo == null) {
      log.info("短信模板: {} 不存在", id)
      throw ResourceNotFoundException("短信模板不存在")
    }
    return templateDo
  }

  /** 通过唯一编码查询 */
  suspend fun findByCode(code: String): SmsTemplateDo?

  /** 通过编码列表批量查询 */
  suspend fun findAllByCodeIn(codes: Collection<String>): List<SmsTemplateDo>

  /** 获取所有的短信模板 */
  suspend fun findAll(): List<SmsTemplateDo>

  /** 删除模板 */
  suspend fun delete(templateDo: SmsTemplateDo)

  /** 分页查询, 按创建时间倒排 */
  suspend fun query(args: QueryTemplateArgs): Page<SmsTemplateDo>
}
