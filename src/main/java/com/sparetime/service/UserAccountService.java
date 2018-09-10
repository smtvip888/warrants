package com.sparetime.service;

import com.sparetime.domian.UserAccount;

import java.math.BigDecimal;
import java.util.List;

public interface UserAccountService {

    List<UserAccount> queryListByUserId(BigDecimal userId);

    List<UserAccount> queryListByChild(BigDecimal child);

    int add(UserAccount userAccount);

    int delete(Long id);
}
