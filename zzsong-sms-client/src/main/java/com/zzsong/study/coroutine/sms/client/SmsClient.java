package com.zzsong.study.coroutine.sms.client;

import cn.idealframework.transmission.exception.ResultException;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/2/1
 */
public interface SmsClient {

  long send(@Nonnull SendSmsArgs args) throws ResultException;

  long send(@Nonnull Collection<SendSmsArgs> argsList) throws ResultException;
}
