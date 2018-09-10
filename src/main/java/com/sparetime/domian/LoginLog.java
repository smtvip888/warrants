package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 17/8/21.
 */
@Data
public class LoginLog extends BaseDomain{

    private BigDecimal userId;

    private int type;

    private String ip;

    private Date loginTime;
}
