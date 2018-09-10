package com.sparetime.domian.extend;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by muye on 18/2/12.
 */
@Data
public class Notice {

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "概要不能为空")
    private String summary;

    @NotEmpty(message = "内容不能为空")
    private String body;

    private Long adminUserId;

    private boolean show;
    private String lang;
}
