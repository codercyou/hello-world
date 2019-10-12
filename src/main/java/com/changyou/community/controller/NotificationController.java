package com.changyou.community.controller;

import com.changyou.community.dto.NotificationDTO;
import com.changyou.community.dto.User;
import com.changyou.community.enums.NotificationTypeEnum;
import com.changyou.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        try {
            NotificationDTO notificationDTO = notificationService.read(id, user);
            if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                    || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
                return "redirect:/question/" + notificationDTO.getOuterId();
            } else {
                return "redirect:/";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/";
    }
}
