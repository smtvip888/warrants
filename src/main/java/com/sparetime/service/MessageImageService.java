package com.sparetime.service;

import com.sparetime.domian.MessageImage;

/**
 * Created by muye on 17/11/7.
 */
public interface MessageImageService {

    int insert(MessageImage image);

    MessageImage queryByKey(Long id);
}
