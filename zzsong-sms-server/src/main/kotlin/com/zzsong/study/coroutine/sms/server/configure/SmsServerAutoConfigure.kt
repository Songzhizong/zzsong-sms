package com.zzsong.study.coroutine.sms.server.configure

import com.zzsong.study.coroutine.sms.server.application.ProviderService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author 宋志宗 on 2022/1/26
 */
@Configuration
@EnableScheduling
@EnableR2dbcAuditing
@ComponentScan("com.zzsong.study.coroutine.sms.server")
@EntityScan("com.zzsong.study.coroutine.sms.server.domain.model")
@EnableR2dbcRepositories("com.zzsong.study.coroutine.sms.server.domain.model")
class SmsServerAutoConfigure(private val providerService: ProviderService) :
  SmartInitializingSingleton {

  override fun afterSingletonsInstantiated() {
    runBlocking {
      providerService.refreshCache()
    }
  }
}
