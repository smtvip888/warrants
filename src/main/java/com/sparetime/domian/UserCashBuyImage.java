package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserCashBuyImage extends BaseDomain{

    private BigDecimal buyId;

    private String imgPath;

    private String ip;

    private Date cdate;
}
