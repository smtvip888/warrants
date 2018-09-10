package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.User;
import com.sparetime.domian.UserCashSell;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashSellService {

    List<UserCashSell> queryListByExample(UserCashSell query, BigDecimal start, BigDecimal end, Page page);

    UserCashSell queryByKey(BigDecimal sellId);

    void sell(User user, UserCashSell sell, String ip) throws Exception;

    void gathering(UserCashSell sell, String ip);

    int update(UserCashSell sell);
}
