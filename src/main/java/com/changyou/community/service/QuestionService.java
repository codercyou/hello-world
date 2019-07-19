package com.changyou.community.service;

import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public List<QuestionDTO> list(){
        List<Question>questions = questionMapper.list();
        List<QuestionDTO> questionDTOS = new ArrayList<QuestionDTO>();
        for (Question question:questions){
            System.out.println("question:"+question);
            User user = userMapper.getUserById(question.getCreator());
            System.out.println("user:"+user.toString());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }
}
