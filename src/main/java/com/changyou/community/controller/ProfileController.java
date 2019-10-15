package com.changyou.community.controller;

import com.changyou.community.dto.PaginationDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.service.NotificationService;
import com.changyou.community.service.QuestionService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}/{page}/{size}")
    public String profile(@PathVariable(name="action") String action, @PathVariable(name = "page") Integer page,
                          @PathVariable(name="size") Integer size, Model model, HttpServletRequest request){
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");

        }else if("replys".equals(action)){
            model.addAttribute("section","replys");
            model.addAttribute("sectionName","最新回复");
        }

        /*List<QuestionDTO> questionDTOs = questionService.list();

        model.addAttribute("questions",questionDTOs);
        System.out.println("questionDTOs.size:"+questionDTOs.size());
        for (QuestionDTO questionDTO:questionDTOs
                ) {
            System.out.println(questionDTO);
        }*/

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        String search = "";
        PaginationDTO pagination = questionService.list(search,page, size);
        model.addAttribute("pagination", pagination);


        return "profile";
    }


    @GetMapping("/profile/{action}")
    public String profile_1(@PathVariable(name="action") String action,
                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(name = "size", defaultValue = "2") Integer size,
                            Model model, HttpServletRequest request){
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            System.out.println();
            model.addAttribute("sectionName","我的提问");
            User user = (User) request.getSession().getAttribute("user");

            if (user == null){
                model.addAttribute("errorMessage","未登录");
                return "redirect:/";
            }

            Long unreadCount = notificationService.unreadCount(user.getAccountId());
            System.out.println("unreadcount:"+unreadCount);
            model.addAttribute("unreadCount", unreadCount);
            System.out.println("user.getId:"+user.getId());
            PaginationDTO pagination = questionService.list(user.getId(),page, size);
            if(pagination!=null) {
                model.addAttribute("pagination", pagination);
            }

        }else if("replys".equals(action)) {
            //这是注释
            model.addAttribute("section", "replys");
            model.addAttribute("sectionName", "最新回复");

            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                model.addAttribute("errorMessage", "未登录");
                return "redirect:/";
            }
            try
            {
                System.out.println("111111user.getName():"+user.getName());
                Long unreadCount = notificationService.unreadCount(user.getAccountId());
                System.out.println("unreadcount:"+unreadCount);
                model.addAttribute("unreadCount", unreadCount);

                System.out.println("22222222user.getName:" + user.getName());
                PaginationDTO pagination = notificationService.list(user.getAccountId(), page, unreadCount.intValue());
                if(pagination!=null) {
                    model.addAttribute("pagination", pagination);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }




        return "profile";
    }
}
