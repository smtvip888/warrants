package com.sparetime.service.impl;

import com.sparetime.dao.mapper.MessageImageMapper;
import com.sparetime.domian.MessageImage;
import com.sparetime.service.MessageImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by muye on 17/11/7.
 */
@Service
public class MessageImageServiceImpl implements MessageImageService {

    @Autowired
    private MessageImageMapper messageImageMapper;

    @Override
    public int insert(MessageImage image) {
        return messageImageMapper.insert(image);
    }

    @Override
    public MessageImage queryByKey(Long id) {
        return messageImageMapper.selectByKey(id);
    }
}
