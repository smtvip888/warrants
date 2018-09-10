package com.sparetime.service.impl;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.common.util.CommonUtil;
import com.sparetime.common.util.FieldUtil;
import com.sparetime.dao.mapper.MessageMapper;
import com.sparetime.domian.Manager;
import com.sparetime.domian.Message;
import com.sparetime.domian.MessageImage;
import com.sparetime.service.ManagerService;
import com.sparetime.service.MessageImageService;
import com.sparetime.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 17/7/27.
 */
@Service
public class MessageServiceIpml implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MessageImageService messageImageService;

    @Override
    public List<Message> queryListByExample(Message message, Page page) {
        FieldUtil.spaceToNull(message);
        List<Message> messageList =  messageMapper.selectListByExample(message, page);


        messageList.forEach(msg -> {
            List<String> imgIdList = CommonUtil.extractImgSrc(msg.getBody());
            imgIdList.forEach(imgId -> {
                MessageImage image = messageImageService.queryByKey(Long.valueOf(imgId.replace("message_image_", "")));
                if (image != null){
                    msg.setBody(msg.getBody().replace(imgId, image.getImgPath()));
                }
            });
        });

        return messageList;
    }

    @Override
    @Transactional
    public Long addMessage(Message message) {

        String managerName = ((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);

        if (manager != null)
            message.setAdminUserId(new BigDecimal(manager.getId()));

        message.setCdate(new Date());

        List<String> srcList = CommonUtil.extractImgSrc(message.getBody());
        srcList.forEach(src -> {
            MessageImage image = new MessageImage();
            image.setImgPath(src);
            image.setCdate(new Date());
            messageImageService.insert(image);
            message.setBody(message.getBody().replace(src, "message_image_" + image.getId()));
        });

        messageMapper.insert(message);
        return message.getId();
    }

    @Override
    public Message queryByKey(BigDecimal id) {
        Message message = messageMapper.selectByKey(id);

        List<String> imgIdList = CommonUtil.extractImgSrc(message.getBody());
        imgIdList.forEach(imgId -> {
            MessageImage image = messageImageService.queryByKey(Long.valueOf(imgId.replace("message_image_", "")));
            if (image != null){
                message.setBody(message.getBody().replace(imgId, image.getImgPath()));
            }
        });

        return message;
    }

    @Override
    @Transactional
    public int updateSelective(Message message) {

        List<String> srcList = CommonUtil.extractImgSrc(message.getBody());
        srcList.forEach(src -> {
            MessageImage image = new MessageImage();
            image.setImgPath(src);
            image.setCdate(new Date());
            messageImageService.insert(image);
            message.setBody(message.getBody().replace(src, "message_image_" + image.getId()));
        });
        return messageMapper.updateSelective(message);
    }

    @Override
    public List<Message> readMsg(BigDecimal id) {

        Message message = messageMapper.selectByKey(id);

        if (message.getMsgId() != null){
            message = messageMapper.selectByKey(new BigDecimal(message.getMsgId()));
        }

        Long msgId = message.getId();

        List<Message> messageList = messageMapper.selectListByMsgId(msgId);
        messageList.add(0, message);

        messageList.forEach(msg -> {
            msg.setIsRead(1);
            messageMapper.updateSelective(msg);
            List<String> imgIdList = CommonUtil.extractImgSrc(msg.getBody());
            imgIdList.forEach(imgId -> {
                MessageImage image = messageImageService.queryByKey(Long.valueOf(imgId.replace("message_image_", "")));
                if (image != null){
                    msg.setBody(msg.getBody().replace(imgId, image.getImgPath()));
                }
            });
        });
        return messageList;
    }

    @Override
    @Transactional
    public int insert(Message message) {

        List<String> srcList = CommonUtil.extractImgSrc(message.getBody());
        srcList.forEach(src -> {
            MessageImage image = new MessageImage();
            image.setImgPath(src);
            image.setCdate(new Date());
            messageImageService.insert(image);
            message.setBody(message.getBody().replace(src, "message_image_" + image.getId()));
        });

        message.setCdate(new Date());
        return messageMapper.insert(message);
    }
}
