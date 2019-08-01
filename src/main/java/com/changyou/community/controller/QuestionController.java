package com.changyou.community.controller;

import com.changyou.community.dto.CommentDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.service.CommentService;
import com.changyou.community.service.QuestionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);

        List<CommentDTO> comments = commentService.listByQuestionId(id);
        System.out.println("################################################"+comments);
        System.out.println("-------------------------------------------------------->id:"+id);
        questionService.incView(id);//累加阅读数
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        System.out.println("--------------------------------------------------------->questionDTO.getViewCount():"+questionDTO.getViewCount());
        return "question";
    }
}
