package com.sparetime.controller.admin;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Manager;
import com.sparetime.domian.Message;
import com.sparetime.domian.User;
import com.sparetime.service.ManagerService;
import com.sparetime.service.MessageService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/7/27.
 */
@Controller
@RequestMapping("/admin/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ManagerService managerService;

    @RequestMapping("/receivedList")
    public String receivedList(Message query, String sendUserName, String managerName, Page page, Model model) {

        query.setToUserId(BigDecimal.ZERO);

        if (!StringUtils.isEmpty(sendUserName)) {
            User user = userService.queryByName(sendUserName);
            if (user != null)
                query.setSendUserId(user.getUserId());
            else
                query.setSendUserId(BigDecimal.ZERO);
        }

        if (!StringUtils.isEmpty(managerName)) {
            Manager manager = managerService.queryManagerByName(managerName);
            query.setAdminUserId(manager == null ? BigDecimal.ZERO : new BigDecimal(manager.getId()));
        }

        List<Message> messageList = messageService.queryListByExample(query, page);
        List<User> sendUserList = new ArrayList<>();
        List<Manager> managerList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(messageList)) {
            messageList.forEach(message -> {
                User user = new User();
                Manager manager = new Manager();
                if (message.getSendUserId() != null)
                    user = userService.queryByKey(message.getSendUserId());
                if (message.getAdminUserId() != null)
                    manager = managerService.queryByKey(message.getAdminUserId().longValue());
                sendUserList.add(user);
                managerList.add(manager);
            });
        }

        model.addAttribute("query", query);
        model.addAttribute("sendUserName", sendUserName);
        model.addAttribute("managerName", managerName);
        model.addAttribute("page", page);
        model.addAttribute("messageList", messageList);
        model.addAttribute("sendUserList", sendUserList);
        model.addAttribute("managerList", managerList);
        return "admin/message/received_list";
    }

    @RequestMapping("/read")
    public String read(BigDecimal id, Model model) {

        List<Message> messageList = messageService.readMsg(id);
        List<Manager> managerList = new ArrayList<>();
        User user = new User();
        if (!CollectionUtils.isEmpty(messageList)) {
            user = userService.queryByKey(messageList.get(0).getSendUserId().equals(BigDecimal.ZERO) ? messageList.get(0).getToUserId() : messageList.get(0).getSendUserId());
            messageList.forEach(message -> {
                Manager manager = null;
                if (message.getAdminUserId() != null) {
                    manager = managerService.queryByKey(message.getAdminUserId().longValue());
                }
                managerList.add(manager == null ? new Manager() : manager);
            });
        }

        model.addAttribute("messageList", messageList);
        model.addAttribute("user", user);
        model.addAttribute("managerList", managerList);
        return "admin/message/read_msg";
    }

    @RequestMapping("/toReply")
    public String toReply(Message query, Model model) {

        Message message = messageService.queryByKey(new BigDecimal(query.getId()));

        String managerName = ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);

        User user = userService.queryByKey(message.getSendUserId().equals(BigDecimal.ZERO) ? message.getToUserId() : message.getSendUserId());

        message.setAdminUserId(new BigDecimal(manager.getId()));
        message.setToUserId(user.getUserId());
        message.setSendUserId(BigDecimal.ZERO);
        message.setMsgId(message.getId());
        message.setBody(null);

        message.setTitle("【回复】" + message.getTitle());
        model.addAttribute("message", message);
        model.addAttribute("toUserName", user.getUserName());
        return "admin/message/reply_msg";
    }

    @RequestMapping("/reply")
    public String reply(@Valid Message message, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            return "admin/message/reply_msg";
        }

        messageService.addMessage(message);
        return "redirect:/admin/message/receivedList";
    }

    @RequestMapping("/outbox")
    public String outbox(Message query, String toUserName, String managerName, Page page, Model model) {

        query.setSendUserId(BigDecimal.ZERO);

        if (!StringUtils.isEmpty(toUserName)) {
            User user = userService.queryByName(toUserName);
            if (user != null)
                query.setToUserId(user.getUserId());
            else
                query.setToUserId(BigDecimal.ZERO);
        }

        if (!StringUtils.isEmpty(managerName)) {
            Manager manager = managerService.queryManagerByName(managerName);
            query.setAdminUserId(manager == null ? BigDecimal.ZERO : new BigDecimal(manager.getId()));
        }

        List<Message> messageList = messageService.queryListByExample(query, page);
        List<User> toUserList = new ArrayList<>();
        List<Manager> managerList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(messageList)) {
            messageList.forEach(message -> {
                Manager manager = new Manager();
                User user = null;
                if (message.getToUserId() != null)
                    user = userService.queryByKey(message.getToUserId());
                if (message.getAdminUserId() != null)
                    manager = managerService.queryByKey(message.getAdminUserId().longValue());
                toUserList.add(user == null ? new User() : user );
                managerList.add(manager);
            });
        }

        model.addAttribute("query", query);
        model.addAttribute("toUserName", toUserName);
        model.addAttribute("managerName", managerName);
        model.addAttribute("page", page);
        model.addAttribute("messageList", messageList);
        model.addAttribute("toUserList", toUserList);
        model.addAttribute("managerList", managerList);

        return "admin/message/outbox";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Message message) {

        return "admin/message/add";
    }

    @RequestMapping("/add")
    public String add(@Valid Message message, BindingResult bindingResult, String toUserName, Model model) {

        User toUser = null;
        if (!StringUtils.isEmpty(toUserName))
            toUser = userService.queryByName(toUserName);
        if (toUser == null) {
            FieldError fieldError = new FieldError("message", "toUserId", "not fund user by userName:" + toUserName);
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasFieldErrors()) {
            return "admin/message/add";
        }

        message.setToUserId(toUser.getUserId());

        String managerName = ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        Manager manager = managerService.queryManagerByName(managerName);

        message.setAdminUserId(new BigDecimal(manager.getId()));
        message.setSendUserId(BigDecimal.ZERO);

        messageService.addMessage(message);
        return "redirect:/admin/message/outbox";
    }
}
