package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCashBuy extends BaseDomain {

    private BigDecimal buyId;

    private String tradeSN;

    private BigDecimal userId;

    private BigDecimal amount;

    private String currencyName;

    private BigDecimal exchangeRate;

    private BigDecimal sellId;

    private BigDecimal sellUserId;

    private String ip;

    private Integer status;

    private Date cdate;

    private String payIp;

    private Date payDate;
}
