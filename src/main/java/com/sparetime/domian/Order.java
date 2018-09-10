package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order extends BaseDomain {

    private BigDecimal orderId;

    private BigDecimal userId;

    private BigDecimal netUserId;

    private BigDecimal productId;

    private BigDecimal price;

    private BigDecimal payAmount;

    private int isUp;

    private BigDecimal payUserId;

    private Date cdate;
}
