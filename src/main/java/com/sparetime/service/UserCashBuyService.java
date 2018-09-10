package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.User;
import com.sparetime.domian.UserCashBuy;
import com.sparetime.domian.UserCashSell;

import java.math.BigDecimal;
import java.util.List;

public interface UserCashBuyService {

    UserCashBuy buy(User user, UserCashSell sell, String ip);

    List<UserCashBuy> queryListByExample(UserCashBuy query, Page page);

    UserCashBuy queryByKey(BigDecimal buyId);

    void buyImgCommit(BigDecimal buyId, List<String> paths, String ip);

    int update(UserCashBuy buy);
}
