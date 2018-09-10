package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Message;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/27.
 */
public interface MessageService {

    List<Message> queryListByExample(Message message, Page page);

    Long addMessage(Message message);

    Message queryByKey(BigDecimal id);

    int updateSelective(Message message);

    List<Message> readMsg(BigDecimal id);

    int insert(Message message);
}
