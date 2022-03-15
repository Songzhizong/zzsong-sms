package com.zzsong.study.coroutine.sms.server.domain.model.task;

import cn.idealframework.lang.StringUtils;
import com.zzsong.study.coroutine.sms.server.provider.SendResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

/**
 * 短信发送日志
 * <pre>
 *   <b>indexes:</b>
 *     - normal: taskId
 *     - normal: templateId
 *     - normal: mobile
 * </pre>
 *
 * @author 宋志宗 on 2022/1/28
 */
@Getter
@Setter
@Table("sms_send_log")
public class SendLogDo {

  /** 主键 */
  @Id
  private long id;

  /** 任务id */
  private long taskId;

  /** 模板id */
  private long templateId;

  /** 是否成功发发送 */
  private boolean success;

  /** 手机号码 */
  @Nonnull
  private String mobile;

  /** 发送内容 */
  @Nonnull
  private String content;

  /** 描述信息 */
  @Nonnull
  private String message;

  /** 提供商编码 */
  @Nonnull
  private String providerCode;

  /** 提供商流水号 */
  @Nonnull
  private String providerSerialNumber;

  /** 创建时间 */
  private LocalDateTime createdTime;

  @Nonnull
  public static SendLogDo create(long taskId,
                                 @Nonnull SendResult result) {
    SendLogDo sendLogDo = new SendLogDo();
    sendLogDo.setContent(result.getContent());
    sendLogDo.setMessage(result.getDescription());
    sendLogDo.setTaskId(taskId);
    sendLogDo.setTemplateId(result.getTemplateId());
    sendLogDo.setSuccess(result.getSuccess());
    sendLogDo.setMobile(result.getMobile());
    sendLogDo.setProviderCode(result.getProviderCode());
    sendLogDo.setProviderSerialNumber(result.getProviderSerialNumber());
    return sendLogDo;
  }

  public void setContent(@Nullable String content) {
    if (StringUtils.isBlank(content)) {
      content = "";
    }
    this.content = content;
  }

  public void setMessage(@Nullable String message) {
    if (StringUtils.isBlank(message)) {
      message = "";
    }
    this.message = message;
  }
}
