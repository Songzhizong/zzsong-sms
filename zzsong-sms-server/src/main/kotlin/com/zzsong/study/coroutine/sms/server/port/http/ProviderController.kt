package com.zzsong.study.coroutine.sms.server.port.http

import cn.idealframework.transmission.Result
import com.zzsong.study.coroutine.sms.server.application.ProviderService
import com.zzsong.study.coroutine.sms.server.application.args.CreateProviderTemplateArgs
import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplate
import com.zzsong.study.coroutine.sms.server.provider.Provider
import org.springframework.web.bind.annotation.*

/**
 * 提供商管理
 *
 * @author 宋志宗 on 2022/1/28
 */
@RestController
@RequestMapping("/cloud/sms/provider")
class ProviderController(private val providerService: ProviderService) {

  /**
   * 获取所有可用提供商
   *
   * @author 宋志宗 on 2022/1/28
   */
  @GetMapping("/actives")
  suspend fun findAllActiveProvider(): Result<List<Provider>> {
    val providers = providerService.findAllActiveProvider()
    return Result.data(providers)
  }

  @GetMapping("/cache/refresh")
  suspend fun refreshCache(): Result<Void> {
    providerService.refreshCache()
    return Result.success()
  }

  /**
   * 创建提供商模板
   *
   * @author 宋志宗 on 2022/1/28
   */
  @PostMapping("/template/create")
  suspend fun createTemplate(@RequestBody args: CreateProviderTemplateArgs): Result<ProviderTemplate> {
    val templateDo = providerService.createTemplate(args)
    val template = templateDo.toProviderTemplate()
    return Result.data(template)
  }
}
