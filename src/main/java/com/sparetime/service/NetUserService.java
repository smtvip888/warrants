package com.sparetime.service;

import com.sparetime.domian.NetUser;

import java.math.BigDecimal;

/**
 * Created by muye on 17/8/14.
 */
public interface NetUserService {

    int insert(NetUser netUser);

    NetUser queryByUserId(BigDecimal userId);
}
