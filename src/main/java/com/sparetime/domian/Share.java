package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Share extends BaseDomain {

    private BigDecimal shareId;

    private BigDecimal buyId;

    private BigDecimal userId;

    private BigDecimal netUserId;

    private Integer shareType;

    private Integer isOriginalStake ;

    private BigDecimal price;

    private BigDecimal shares;

    private BigDecimal outShares;

    private Integer splitCount;

    private BigDecimal lastSalePrice;

    private Date lastOutDate;

    private String remark;

    private Date cdate;
}
