package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShareTrade extends BaseDomain {

    private BigDecimal shareId;

    private BigDecimal userId;

    private BigDecimal netUserId;

    private Integer shareType;

    private Integer isOriginalStake;

    private BigDecimal price;

    private BigDecimal shares;

    private BigDecimal sellShares;

    private Integer splitCount;

    private String remark;

    private Date cdate;

    private Date lastSellDate;
}
