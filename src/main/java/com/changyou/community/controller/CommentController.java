package com.changyou.community.controller;

import com.changyou.community.dto.CommentCreateDTO;
import com.changyou.community.dto.CommentDTO;
import com.changyou.community.dto.ResultDTO;
import com.changyou.community.dto.User;
import com.changyou.community.exception.CustomizeErrorCode;
import com.changyou.community.mapper.CommentMapper;
import com.changyou.community.model.Comment;
import com.changyou.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;


    @ResponseBody
    @Transactional
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object comment(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request, HttpServletResponse response){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO == null ||commentCreateDTO.getContent()==null ||commentCreateDTO.getContent()==""){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        System.out.println("commentCreateDTO:"+ commentCreateDTO);
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        commentService.addCommentCountById(commentCreateDTO.getParentId());


        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(Integer.valueOf(user.getAccountId()));
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);

        System.out.println("******************************************************");
        commentService.insert(comment);
        System.out.println("#######################################################");

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List> comment(@PathVariable(name = "id") Integer id, Model model){
        List<CommentDTO> commentDTOList = commentService.getCommentListById(id);
        return ResultDTO.okOf(commentDTOList);
    }


}
