package com.changyou.community.service;

import com.changyou.community.dto.PaginationDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.exception.CustomizeErrorCode;
import com.changyou.community.exception.CustomizeException;
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
        List<Question>questions = questionMapper.list1();
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

    public PaginationDTO list(Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        if(totalCount == 0){return null;}
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }

        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer id, Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUser(id);
        System.out.println("--------->totalCount:"+totalCount);
        if (totalCount == 0){
            return null;
        }
        paginationDTO.setPagination(totalCount, page, size);


        if (page < 1) {
            page = 1;
        }

        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUser(id,offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;

    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);

        User user =  userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrupdate(Question question1) {

        if(question1.getId()==null){

            question1.setGmtCreate(System.currentTimeMillis());
            question1.setGmtModified(question1.getGmtCreate());
            questionMapper.createQuestion(question1);
        }else{
            Question question2 = questionMapper.getById(question1.getId());
            question1.setViewCount(question2.getViewCount());
            question1.setGmtModified(question1.getGmtCreate());
            int updated = questionMapper.update(question1);
            if(updated!=1){
                throw new CustomizeException((CustomizeErrorCode.QUESTION_NOT_FOUND));
            }
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id);
        //Question question1  = new Question();
        //question.setViewCount(question.getViewCount()+1);


        questionMapper.updateViewCount(question);
    }

    public List<QuestionDTO> getRelatedQuestion(QuestionDTO questionDTO) {
        questionDTO.setTag(questionDTO.getTag().replace(",","|"));
        List<QuestionDTO> questionDTOList = questionMapper.getRelatedQuestion(questionDTO);
        questionDTO.setTag(questionDTO.getTag().replace("|",","));
        return questionDTOList;
    }
}
