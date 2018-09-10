package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserAccount extends BaseDomain {

    private BigDecimal userId;

    private BigDecimal clientUserId;

    private Integer status;

    private Date cdate;
}
