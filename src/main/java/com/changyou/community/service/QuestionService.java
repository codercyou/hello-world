package com.changyou.community.service;

import com.changyou.community.controller.IndexController;
import com.changyou.community.dto.PaginationDTO;
import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.dto.User;
import com.changyou.community.exception.CustomizeErrorCode;
import com.changyou.community.exception.CustomizeException;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;



@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(QuestionService.class);
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

    public PaginationDTO list(String tag,String search, Integer page, Integer size) {


        logger.info("**************88888888888888888888888888888888888888888888888888**********************8");
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount=null;
        try {
            if (!StringUtils.isEmpty(search)) {
                totalCount = questionMapper.countForSearch(search);
            } else if (!StringUtils.isEmpty(tag)) {
                System.out.println();
                totalCount = questionMapper.countForTag(tag);
            }else {
                totalCount = questionMapper.count();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("totalCount:"+totalCount);

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

        System.out.println(search+","+offset+","+page);

        List<Question> questions = null;
        if(!StringUtils.isEmpty(search)){
            System.out.println("111111111111111111111111111111111111");
            try {
                questions = questionMapper.listForSearch(search, offset, size);
            }catch(Exception e){
                e.printStackTrace();
            }

        }else{
            System.out.println("222222222222222222222222222222******************************");
            questions = questionMapper.list(offset, size);
        }

        if(StringUtils.isEmpty(search)) {
            if (!StringUtils.isEmpty(tag)) {
                System.out.println("111111111111111111111");
                try {
                    System.out.println();
                    questions = questionMapper.listForTag(tag, offset, size);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("333333333333333333333333333******************************");
                questions = questionMapper.list(offset, size);
            }
        }

        System.out.println("questions.size():"+questions.size());

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            if(question.getDescription().length()>300){
                questionDTO.setDescriptionSimple(question.getDescription().substring(0,299)+"...");
            }else{
                questionDTO.setDescriptionSimple(question.getDescription());
            }
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        System.out.println("^^66666666666666666666666666666666666666666666");
        System.out.println("questionDTOList.size():"+questionDTOList.size());
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
        questionDTO.setTag(questionDTO.getTag().replace(",","|").replace("+","\\+"));
        List<QuestionDTO> questionDTOList = questionMapper.getRelatedQuestion(questionDTO);
        questionDTO.setTag(questionDTO.getTag().replace("|",","));
        return questionDTOList;
    }
}
