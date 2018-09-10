package com.sparetime.service;

import com.sparetime.domian.UserCashBuyImage;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashImageService {

    List<UserCashBuyImage> queryListByBuyId(BigDecimal buyId);

    int insert(UserCashBuyImage image);

    int deleteByBuyId(BigDecimal buyId);

}
