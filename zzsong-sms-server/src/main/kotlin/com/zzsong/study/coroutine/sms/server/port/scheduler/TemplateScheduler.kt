package com.zzsong.study.coroutine.sms.server.port.scheduler

import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateCache
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * 模板定时任务调度
 *
 * @author 宋志宗 on 2022/1/26
 */
@Component
class TemplateScheduler(private val providerTemplateCache: ProviderTemplateCache) {

  /**
   * 每分钟刷新一次缓存
   *
   * @author 宋志宗 on 2022/1/29
   */
  @DelicateCoroutinesApi
  @Scheduled(initialDelay = 60, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
  fun refreshTemplate() {
    GlobalScope.launch(Dispatchers.Default) {
      providerTemplateCache.refresh()
    }
  }
}
