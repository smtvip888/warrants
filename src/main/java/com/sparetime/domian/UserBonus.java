package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/15.
 */
@Data
public class UserBonus extends BaseDomain {

    private BigDecimal userId;

    private BigDecimal netUserId;

    private BigDecimal fromUserId;

    private Integer tradeType;

    private Integer bonusType;

    private Integer inOrOut;

    private BigDecimal amount;

    private BigDecimal cashBonus;

    private BigDecimal DTFTBonus;

    private BigDecimal LYJFBonus;

    private BigDecimal JTFTBonus;

    private BigDecimal SCJFBonus;

    private BigDecimal JYFBonus;

    private String remark;

    private String ip;

    private Date cdate;
}
