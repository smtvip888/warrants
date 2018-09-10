package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.UserZCJF;

import java.util.List;

public interface UserZCJFService {

    List<UserZCJF> queryListByExample(UserZCJF userZCJF, Page page);

    int insert(UserZCJF userZCJF);

    int del(Long id);
}
