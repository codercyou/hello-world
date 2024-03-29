package com.changyou.community.controller;

import com.alibaba.druid.support.logging.Log;
import com.changyou.community.cache.HotTagCache;
import com.changyou.community.dto.PaginationDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.model.Ad;
import com.changyou.community.model.Question;
import com.changyou.community.service.AdService;
import com.changyou.community.service.NotificationService;
import com.changyou.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



@Controller
@Slf4j
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private AdService adService;

    @GetMapping("/")
    public String index(HttpServletRequest request,Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "4") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.getUserByToken(token);
                    if(user !=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        /*List<QuestionDTO> questionDTOs = questionService.list();

        model.addAttribute("questions",questionDTOs);
        System.out.println("questionDTOs.size:"+questionDTOs.size());
        for (QuestionDTO questionDTO:questionDTOs
             ) {
            System.out.println(questionDTO);
        }*/


        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            request.getSession().setAttribute("errorMessage","未登录");
            //return "redirect:/";
        }else {
            System.out.println();

            Long unreadCount = notificationService.unreadCount(user.getAccountId());
            System.out.println("unreadcount:" + unreadCount);

            request.getSession().setAttribute("unreadCount", unreadCount);
        }

        System.out.println("search----->"+search);
        PaginationDTO pagination = questionService.list(tag,search, page, size);
        if(pagination == null){
            //return "redirect:/publish";
            model.addAttribute("errorMessage","未找到相关问题");
            //return "redirect:/";
            return "index";
        }

        List<String> tags = hotTagCache.getHots();

        List<Ad> ads = adService.list();
        System.out.println(System.currentTimeMillis());
        System.out.println("--------------------------------------------------->"+ads);

        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        request.getServletContext().setAttribute("ads", ads);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");

        //注意！！！修改、删除Cookie时，新建的Cookie除value、maxAge之外的所有属性，
        // 例如name、path、domain等，都要与原Cookie完全一样。否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    Cookie cookie1 = new Cookie(cookie.getName(), "");
                    cookie1.setMaxAge(0);
                    cookie1.setPath(request.getContextPath());
                    cookie1.setDomain(request.getServerName());
                    response.addCookie(cookie1);

                    break;
                }
            }
        }
        return "redirect:/";
    }
}
