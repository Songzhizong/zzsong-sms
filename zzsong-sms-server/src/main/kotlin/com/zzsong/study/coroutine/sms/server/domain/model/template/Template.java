package com.zzsong.study.coroutine.sms.server.domain.model.template;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 宋志宗 on 2021/7/30
 */
@Getter
@Setter
public class Template {
  private long id;

  private String code;

  private String name;

  private String description;

  private List<TemplateParam> params;

  private LocalDateTime createdTime;

  private LocalDateTime updatedTime;
}
