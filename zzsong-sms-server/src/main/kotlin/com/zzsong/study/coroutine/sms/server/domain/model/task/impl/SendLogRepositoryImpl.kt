package com.zzsong.study.coroutine.sms.server.domain.model.task.impl

import cn.idealframework.date.DateTimes
import cn.idealframework.id.IDGenerator
import cn.idealframework.id.IDGeneratorFactory
import cn.idealframework.kotlin.toJsonString
import com.zzsong.study.coroutine.sms.server.domain.model.task.SendLogDo
import com.zzsong.study.coroutine.sms.server.domain.model.task.SendLogRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

/**
 * @author 宋志宗 on 2022/1/28
 */
@Repository
class SendLogRepositoryImpl(
  idGeneratorFactory: IDGeneratorFactory,
  private val r2dbcEntityTemplate: R2dbcEntityTemplate
) : SendLogRepository {
  companion object {
    private val log: Logger = LoggerFactory.getLogger(SendLogRepositoryImpl::class.java)
  }

  private val idGenerator: IDGenerator = idGeneratorFactory.getGenerator("database")

  override suspend fun saveAll(logs: Collection<SendLogDo>): List<SendLogDo> {
    val now = DateTimes.now()
    logs.forEach {
      it.id = idGenerator.generate()
      it.createdTime = now
    }
    return coroutineScope {
      val deferredList = logs.map { sendLog ->
        async {
          try {
            r2dbcEntityTemplate.insert(sendLog).awaitSingleOrNull() ?: sendLog
          } catch (e: Exception) {
            // 入库失败的记录到日志文件中
            log.warn("task: {} {}", sendLog.taskId, sendLog.toJsonString())
            sendLog
          }
        }
      }
      deferredList.map { it.await() }
    }
  }
}

