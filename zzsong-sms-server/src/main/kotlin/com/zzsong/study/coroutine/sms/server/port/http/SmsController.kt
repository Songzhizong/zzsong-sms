package com.zzsong.study.coroutine.sms.server.port.http

import cn.idealframework.transmission.Result
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs
import com.zzsong.study.coroutine.sms.server.application.SmsService
import kotlinx.coroutines.DelicateCoroutinesApi
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 短信接口
 *
 * @author 宋志宗 on 2022/1/27
 */
@RestController
@RequestMapping("/cloud/sms")
class SmsController(private val smsService: SmsService) {

  /**
   * 单发短信
   *
   * @param args 短信参数
   * @author 宋志宗 on 2022/1/27
   */
  @DelicateCoroutinesApi
  @PostMapping("/send")
  suspend fun send(@RequestBody args: SendSmsArgs): Result<Long> {
    val taskId = smsService.send(args)
    return Result.data(taskId)
  }

  /**
   * 批量发送短信
   *
   * @param argsList 短信参数
   * @author 宋志宗 on 2022/1/27
   */
  @DelicateCoroutinesApi
  @PostMapping("/send/batch")
  suspend fun batchSend(@RequestBody argsList: List<SendSmsArgs>): Result<Long> {
    val taskId = smsService.send(argsList)
    return Result.data(taskId)
  }
}
