package com.sparetime.domian;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/27.
 */
@Data
public class Message extends BaseDomain{

    private Long msgId;

    private BigDecimal toUserId;

    @NotEmpty(message = "标题不能为空")
    private String title;

    @NotEmpty(message = "内容不能为空")
    private String body;

    private Integer type;

    private BigDecimal sendUserId;

    private BigDecimal adminUserId;

    private Integer isDelete;

    private Integer isRead;

    private String ip;

    private Date cdate;
}
