package com.zzsong.study.coroutine.sms.server.domain.model.task

/**
 * @author 宋志宗 on 2022/1/28
 */
interface SendLogRepository {

  suspend fun saveAll(logs: Collection<SendLogDo>): List<SendLogDo>
}
