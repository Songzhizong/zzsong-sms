package com.zzsong.study.coroutine.sms.server.domain.model.template;

import cn.idealframework.date.DateTimes;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.util.Asserts;
import com.zzsong.study.coroutine.sms.server.provider.mock.MockSmsProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 供应商短信模板
 * <pre>
 *   <b>indexes:</b>
 *     - unique: defaultFlag + templateId
 *     - normal: templateId
 *     - normal: providerCode
 * </pre>
 *
 * @author 宋志宗 on 2022/1/27
 */
@Getter
@Setter
@Table("sms_provider_template")
public class ProviderTemplateDo {
  public static final String DEFAULT_FLAG = "0";

  /** 主键 */
  @Id
  private long id = -1;

  /** 模板id */
  private long templateId = -1;

  /** 模板编码 */
  private String templateCode = "";

  /** 是否默认模板, 与模板id形成唯一索引 */
  private String defaultFlag;

  /** 提供商编码 */
  @Nonnull
  private String providerCode = "";

  /** 服务商模板编码 */
  @Nonnull
  private String providerTemplate = "";

  /** 短信内容 */
  @Nonnull
  private String content = "";

  /** 乐观锁 */
  @Version
  private long version = 0;

  /** 创建时间 */
  @CreatedDate
  private LocalDateTime createdTime;

  /** 更新时间 */
  @LastModifiedDate
  private LocalDateTime updatedTime;

  @Nonnull
  public static ProviderTemplateDo create(boolean defaultTemplate,
                                          @Nonnull SmsTemplateDo template,
                                          @Nullable String providerCode,
                                          @Nullable String providerTemplate,
                                          @Nullable String content) {
    ProviderTemplateDo providerTemplateDo = new ProviderTemplateDo();
    providerTemplateDo.setTemplateId(template.getId());
    providerTemplateDo.setTemplateCode(template.getCode());
    if (defaultTemplate) {
      providerTemplateDo.setDefaultFlag(DEFAULT_FLAG);
    } else {
      providerTemplateDo.setDefaultFlag(UUID.randomUUID().toString().replace("-", ""));
    }
    providerTemplateDo.setProviderCode(providerCode);
    providerTemplateDo.setProviderTemplate(providerTemplate);
    providerTemplateDo.setContent(content);
    return providerTemplateDo;
  }

  @Nonnull
  public static ProviderTemplateDo createMock(@Nonnull String code) {
    ProviderTemplateDo providerTemplateDo = new ProviderTemplateDo();
    providerTemplateDo.setProviderCode(MockSmsProvider.CODE);
    providerTemplateDo.setProviderTemplate("");
    providerTemplateDo.setContent("mock");
    providerTemplateDo.setId(-1);
    providerTemplateDo.setTemplateId(-1);
    providerTemplateDo.setTemplateCode(code);
    providerTemplateDo.setDefaultFlag(DEFAULT_FLAG);
    providerTemplateDo.setVersion(0);
    LocalDateTime now = DateTimes.now();
    providerTemplateDo.setCreatedTime(now);
    providerTemplateDo.setUpdatedTime(now);
    return providerTemplateDo;
  }

  @Nonnull
  public ProviderTemplate toProviderTemplate() {
    ProviderTemplate providerTemplate = new ProviderTemplate();
    providerTemplate.setId(getId());
    providerTemplate.setTemplateId(getTemplateId());
    providerTemplate.setTemplateCode(getTemplateCode());
    providerTemplate.setDefaultImpl(DEFAULT_FLAG.equals(this.getDefaultFlag()));
    providerTemplate.setProviderCode(getProviderCode());
    providerTemplate.setProviderTemplate(getProviderTemplate());
    providerTemplate.setContent(getContent());
    providerTemplate.setCreatedTime(getCreatedTime());
    providerTemplate.setUpdatedTime(getUpdatedTime());
    return providerTemplate;
  }

  /** 设为默认模板 */
  public void setupDefault() {
    this.setDefaultFlag(DEFAULT_FLAG);
  }

  /** 取消默认模板 */
  public void cancelDefault() {
    this.setDefaultFlag(UUID.randomUUID().toString().replace("-", ""));
  }

  // ----------------------------------------- getter & setter ~ ~ ~

  public void setProviderCode(@Nullable String providerCode) {
    Asserts.notBlank(providerCode, "服务商编码不能为空");
    this.providerCode = providerCode;
  }

  public void setProviderTemplate(@Nullable String providerTemplate) {
    if (StringUtils.isBlank(providerTemplate)) {
      providerTemplate = "";
    }
    this.providerTemplate = providerTemplate;
  }

  public void setContent(@Nullable String content) {
    if (StringUtils.isBlank(content)) {
      content = "";
    }
    this.content = content;
  }
}
