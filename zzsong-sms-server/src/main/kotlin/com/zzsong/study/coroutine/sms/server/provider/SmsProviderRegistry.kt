package com.zzsong.study.coroutine.sms.server.provider

import cn.idealframework.transmission.exception.ResourceNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * @author 宋志宗 on 2022/1/28
 */
@Component
class SmsProviderRegistry(
  private val applicationContext: ApplicationContext
) : SmartInitializingSingleton {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SmsProviderRegistry::class.java)
  }

  private val providerMap = HashMap<String, SmsProvider>()
  private val lock1 = Any()
  private val lock2 = Any()

  @Volatile
  private var providers = listOf<Provider>()

  @Volatile
  private var providerCodes = setOf<String>()

  private fun register(provider: SmsProvider) {
    providers = listOf()
    providerCodes = setOf()
    val code = provider.getCode()
    val name = provider.getName()
    log.info("注册短信服务提供商: [{} {}]", code, name)
    val put = providerMap.put(code, provider)
    if (put != null) {
      log.error("重复的短信服务提供商编码: {} -> {}", code, provider::class.java.name)
    }
  }

  fun get(code: String): SmsProvider? = providerMap[code]

  fun getRequired(code: String): SmsProvider {
    val provider = get(code)
    if (provider == null) {
      log.error("无法通过编码: {} 获取短信服务提供商", code)
      throw ResourceNotFoundException("无效的服务商编码: $code")
    }
    return provider
  }

  /**
   * 获取所有可用的提供商信息
   *
   * @author 宋志宗 on 2022/1/28
   */
  fun getAllProvider(): List<Provider> {
    if (providerMap.isEmpty()) {
      return listOf()
    }
    if (providers.isEmpty()) {
      synchronized(lock1) {
        if (providers.isEmpty()) {
          providers = providerMap.values.map { p ->
            Provider().also {
              it.code = p.getCode()
              it.name = p.getName()
            }
          }
        }
      }
    }
    return providers
  }

  fun getAllProviderCodes(): Set<String> {
    if (providerMap.isEmpty()) {
      return setOf()
    }
    if (providerCodes.isEmpty()) {
      synchronized(lock2) {
        providerCodes = providerMap.values.map { it.getCode() }.toHashSet()
      }
    }
    return providerCodes
  }

  /**
   * 判断提供商编码是否可用
   *
   * @author 宋志宗 on 2022/1/28
   */
  fun isActive(providerCode: String): Boolean {
    return getAllProviderCodes().contains(providerCode)
  }

  /** 自动注册bean */
  override fun afterSingletonsInstantiated() {
    val beans = applicationContext.getBeansOfType(SmsProvider::class.java)
    beans.forEach { (_, provider) ->
      this.register(provider)
    }
  }
}
