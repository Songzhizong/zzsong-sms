package com.zzsong.study.coroutine.sms.server.port.http

import cn.idealframework.kotlin.toPageResult
import cn.idealframework.transmission.PageResult
import cn.idealframework.transmission.Result
import com.zzsong.study.coroutine.sms.server.application.SmsTemplateService
import com.zzsong.study.coroutine.sms.server.application.args.CreateTemplateArgs
import com.zzsong.study.coroutine.sms.server.domain.model.template.Template
import com.zzsong.study.coroutine.sms.server.domain.model.template.args.QueryTemplateArgs
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 短信模板管理
 *
 * @author 宋志宗 on 2022/1/27
 */
@RestController
@RequestMapping("/sms/template")
class TemplateController(private val smsTemplateService: SmsTemplateService) {

  /**
   * 创建短信模板
   *
   * @author 宋志宗 on 2022/1/27
   */
  @PostMapping("/create")
  suspend fun create(@RequestBody args: CreateTemplateArgs): Result<Template> {
    val templateDo = smsTemplateService.create(args)
    val template = templateDo.toTemplate()
    return Result.data(template)
  }

  /**
   * 更新模板信息
   *
   * @author 宋志宗 on 2022/1/27
   */
  @PostMapping("/update")
  suspend fun update(id: Long): Result<Template> {
    val templateDo = smsTemplateService.update(id)
    val template = templateDo.toTemplate()
    return Result.data(template)
  }

  @PostMapping("/delete")
  suspend fun delete(id: Long): Result<Void> {
    smsTemplateService.delete(id)
    return Result.success()
  }

  /**
   * 分页查询短信模板
   *
   * @author 宋志宗 on 2022/1/28
   */
  @PostMapping("/query")
  suspend fun query(@RequestBody args: QueryTemplateArgs): PageResult<Template> {
    val page = smsTemplateService.query(args)
    val map = page.map { it.toTemplate() }
    return map.toPageResult()
  }
}
