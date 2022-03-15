package com.zzsong.study.coroutine.sms.server.application

import cn.idealframework.id.IDGenerator
import cn.idealframework.id.IDGeneratorFactory
import cn.idealframework.transmission.exception.BadRequestException
import cn.idealframework.transmission.exception.ResourceNotFoundException
import cn.idealframework.util.NumberSystemConverter
import com.zzsong.study.coroutine.sms.server.application.args.CreateTemplateArgs
import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateDo
import com.zzsong.study.coroutine.sms.server.domain.model.template.SmsTemplateRepository
import com.zzsong.study.coroutine.sms.server.domain.model.template.args.QueryTemplateArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author 宋志宗 on 2022/1/27
 */
@Service
class SmsTemplateService(
  idGeneratorFactory: IDGeneratorFactory,
  private val smsTemplateRepository: SmsTemplateRepository
) {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SmsTemplateService::class.java)
  }

  /** 模板编码生成器 */
  private val idGenerator: IDGenerator = idGeneratorFactory.getGenerator("sms_code")

  /**
   * 创建短信模板
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun create(args: CreateTemplateArgs): SmsTemplateDo {
    var code = args.code
    if (code != null && code.isNotBlank()) {
      val templateDo = smsTemplateRepository.findByCode(code)
      if (templateDo != null) {
        log.info("短信模板编码: {} 已存在", code)
        throw BadRequestException("模板编码已存在")
      }
    } else {
      val generate = idGenerator.generate()
      code = "SMS_" + NumberSystemConverter.tenSystemTo(generate, 36)
      code = code.uppercase(Locale.getDefault())
    }
    val templateDo = SmsTemplateDo
      .create(code, args.name, args.params, args.description)
    smsTemplateRepository.save(templateDo)
    log.info("新增短信模板: {} {}", templateDo.id, templateDo.code)
    return templateDo
  }

  /**
   * 更新模板信息
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun update(id: Long): SmsTemplateDo {
    val templateDo = smsTemplateRepository.findById(id)
    if (templateDo == null) {
      log.info("短信模板: {} 不存在", id)
      throw ResourceNotFoundException("短信模板不存在")
    }
    log.info("修改短信模板信息: [{} {}]", templateDo.id, templateDo.code)
    smsTemplateRepository.save(templateDo)
    return templateDo
  }

  suspend fun delete(id: Long) {
    val templateDo = smsTemplateRepository.findById(id)
    if (templateDo == null) {
      log.info("短信模板: {} 不存在", id)
      return
    }
    smsTemplateRepository.delete(templateDo)
  }

  /**
   * 分页查询短信模板
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun query(args: QueryTemplateArgs): Page<SmsTemplateDo> {
    return smsTemplateRepository.query(args)
  }
}
