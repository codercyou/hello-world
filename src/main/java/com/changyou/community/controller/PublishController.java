package com.changyou.community.controller;

import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.model.Question;
import com.changyou.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(Model model) {
        //model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(String title, String description, String tag,Integer id, HttpServletRequest request,Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title==null || "".equals(title)){
            model.addAttribute("errorMessage","标题不能为空");
            return "publish";
        }
        if(description==null || "".equals(description)){
            model.addAttribute("errorMessage","补充问题不能为空");
            return "publish";
        }
        if(tag==null || "".equals(tag)){
            model.addAttribute("errorMessage","标签不能为空");
            return "publish";
        }

        User user=(User)request.getSession().getAttribute("user");

        if (user == null){
            model.addAttribute("errorMessage","未登录");
            return "publish";
        }

        Question question1 = new Question();
        question1.setTitle(title);
        question1.setDescription(description);
        question1.setTag(tag);
        question1.setCreator(user.getId());
        question1.setGmtCreate(System.currentTimeMillis());
        question1.setGmtModified(question1.getGmtCreate());
        question1.setId(id);

        //questionMapper.createQuestion(question1);

        questionService.createOrupdate(question1);
        model.addAttribute("errorMessage","提交成功");
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,Model model){
        QuestionDTO question = questionService.getById(id);

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }


}
