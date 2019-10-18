package com.changyou.community.controller;

import com.changyou.community.dto.CommentDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.service.CommentService;
import com.changyou.community.service.NotificationService;
import com.changyou.community.service.QuestionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request){
        try {
            QuestionDTO questionDTO = questionService.getById(id);
            questionDTO.setTag(questionDTO.getTag().replace("+","\\+"));
            List<QuestionDTO> relatedQuestions = questionService.getRelatedQuestion(questionDTO);
            System.out.println("relatedQuestions:" + relatedQuestions);
            List<CommentDTO> comments = commentService.listByQuestionId(id);
            System.out.println("################################################" + comments);
            System.out.println("-------------------------------------------------------->id:" + id);
            questionService.incView(id);//累加阅读数
            questionDTO.setTag(questionDTO.getTag().replace("\\\\+","+"));
            model.addAttribute("question", questionDTO);
            model.addAttribute("comments", comments);
            model.addAttribute("relatedQuestions", relatedQuestions);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                request.getSession().setAttribute("errorMessage", "未登录");
                User loginUser = new User();
                loginUser.setAvatarUrl("/images/grape.PNG");
                model.addAttribute("loginUser", loginUser);

                //return "redirect:/";
            } else {

                model.addAttribute("loginUser", user);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println("--------------------------------------------------------->questionDTO.getViewCount():"+questionDTO.getViewCount());
        return "question";
    }
}
