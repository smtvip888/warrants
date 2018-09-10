package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * Created by muye on 17/7/26.
 */
@Data
public class News extends BaseDomain {

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "概要不能为空")
    private String summary;

    @NotEmpty(message = "内容不能为空")
    private String body;

    private Long adminUserId;

    private Integer isDelete;

    private Integer isPop;

    private Date cdate;

    private String lang;
}
