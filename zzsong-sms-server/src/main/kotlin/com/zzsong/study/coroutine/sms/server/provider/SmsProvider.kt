package com.zzsong.study.coroutine.sms.server.provider

import cn.idealframework.lang.StringUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * @author 宋志宗 on 2022/1/27
 */
interface SmsProvider {

  /**
   * 获取短信服务提供者唯一编码
   *
   * @author 宋志宗 on 2022/1/27
   */
  fun getCode(): String

  fun getName(): String

  /**
   * 请求单发
   * <pre>
   *   该方法的实现应当尽量避免抛出异常,
   *   若实现内部出现异常或发送失败, 应当为每个手机号构建发送失败的结果并发返回
   * </pre>
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun send(request: SendRequest): List<SendResult>


  /**
   * 批量发送请求
   * <pre>
   *   该方法的实现应当尽量避免抛出异常,
   *   若实现内部出现异常或发送失败, 应当为每个手机号构建发送失败的结果并发返回
   * </pre>
   *
   * @author 宋志宗 on 2022/1/28
   */
  suspend fun send(requests: Collection<SendRequest>): List<SendResult> {
    if (requests.isEmpty()) {
      return emptyList()
    }
    return coroutineScope {
      val map = requests
        .map { request ->
          async {
            send(request)
          }
        }
      map.map { it.await() }.flatten()
    }
  }

  fun formatContent(content: String, params: Map<String, String>?): String {
    if (content.isBlank() || params == null || params.isEmpty()) {
      return content
    }
    var result = content
    val entries = params.entries
    for (entry in entries) {
      val key = entry.key
      val value = entry.value
      result = StringUtils.replace(content, "\${$key}", value)
    }
    return result
  }
}
