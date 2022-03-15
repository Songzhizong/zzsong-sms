package com.zzsong.study.coroutine.sms.server.domain.model.template;

import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.StringUtils;
import cn.idealframework.util.Asserts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 短信模板
 * <pre>
 *   <b>indexes:</b>
 *     - unique: code
 * </pre>
 *
 * @author 宋志宗 on 2022/1/27
 */
@SuppressWarnings("unused")
@Getter
@Setter
@Table("sms_template")
public class SmsTemplateDo {
  @org.springframework.data.annotation.Transient
  private transient volatile boolean changed = false;

  /** 主键 */
  @Id
  private long id = -1;

  /** 模板编码 */
  @Nonnull
  private String code = "";

  /** 模板名称 */
  @Nonnull
  private String name = "";

  /** 参数列表 */
  @Nonnull
  private String params = "";

  /** 描述信息 */
  @Nonnull
  private String description = "";

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
  public static SmsTemplateDo create(@Nullable String code,
                                     @Nullable String name,
                                     @Nullable Collection<TemplateParam> params,
                                     @Nullable String description) {
    if (params == null) {
      params = Collections.emptyList();
    }
    for (TemplateParam param : params) {
      Asserts.notBlank(param.getParam(), "模板参数不能名不能为空");
    }
    SmsTemplateDo smsTemplateDo = new SmsTemplateDo();
    smsTemplateDo.setCode(code);
    smsTemplateDo.setName(name);
    smsTemplateDo.setParams(JsonUtils.toJsonString(params));
    smsTemplateDo.setDescription(description);
    return smsTemplateDo;
  }

  @Nonnull
  public Template toTemplate() {
    Template template = new Template();
    template.setId(getId());
    template.setCode(getCode());
    template.setName(getName());
    template.setDescription(getDescription());
    template.setParams(JsonUtils.parseList(getParams(), TemplateParam.class));
    template.setCreatedTime(getCreatedTime());
    template.setUpdatedTime(getUpdatedTime());
    return template;
  }

  @Transient
  @org.springframework.data.annotation.Transient
  public boolean isChanged() {
    return this.changed;
  }

  public void setCode(@Nullable String code) {
    Asserts.notBlank(code, "短信模板编码不能为空");
    if (code.equals(this.code)) {
      return;
    }
    this.changed = true;
    this.code = code;
  }

  public void setName(@Nullable String name) {
    if (StringUtils.isBlank(name)) {
      name = "";
    }
    if (name.equals(this.getName())) {
      return;
    }
    this.changed = true;
    this.name = name;
  }

  public void setParams(@Nullable String params) {
    if (StringUtils.isBlank(params)) {
      params = "";
    }
    if (params.equals(this.getParams())) {
      return;
    }
    this.changed = true;
    this.params = params;
  }

  public void setDescription(@Nullable String description) {
    if (StringUtils.isBlank(description)) {
      description = "";
    }
    if (description.equals(this.getDescription())) {
      return;
    }
    this.changed = true;
    this.description = description;
  }
}
