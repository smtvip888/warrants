package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/7/24.
 */
@Data
public class UserDTLYBonus extends BaseDomain {

    private BigDecimal userId;

    private BigDecimal netUserId;

    private Integer tradeType;

    private Integer bonusType;

    private BigDecimal amount;

    private BigDecimal fromUserId;

    private Integer inOrOut;

    private String remark;

    private Date cdate;
}
