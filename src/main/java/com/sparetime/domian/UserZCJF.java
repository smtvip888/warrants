package com.sparetime.domian;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserZCJF extends BaseDomain {

    private BigDecimal userId;

    private BigDecimal netUserId;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    private BigDecimal fromUserId;

    private Integer inOrOut;

    private Integer isdel;

    private String remark;

    private String ip;

    private Date cdate;
}
