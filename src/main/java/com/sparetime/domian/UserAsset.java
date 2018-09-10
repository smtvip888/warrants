package com.sparetime.domian;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAsset extends BaseDomain {

    private BigDecimal userId;

    private int teamNum;

    private BigDecimal teamTurnover;

    private BigDecimal leftTurnoverSurplus;

    private BigDecimal rightTurnoverSurplus;

    private BigDecimal leftTotalTurnover;

    private BigDecimal rightTotalTurnover;

    private BigDecimal totalBonus;

    private BigDecimal bonusSurplus;

    private BigDecimal sharesIntegral;

    private BigDecimal DTFTJJ;

    private BigDecimal DTFTJJYE;

    private BigDecimal JTFTJJ;

    private BigDecimal JTFTJJYE;

    private BigDecimal LYJF;

    private BigDecimal LYJFYE;

    private BigDecimal SCJF;

    private BigDecimal SCJFYE;

    private BigDecimal ZCJF;

    private BigDecimal ZCJFYYE;
}
