package com.changyou.community.service;

import com.changyou.community.dto.CommentDTO;
import com.changyou.community.dto.User;
import com.changyou.community.enums.CommentTypeEnum;
import com.changyou.community.enums.NotificationStatusEnum;
import com.changyou.community.enums.NotificationTypeEnum;
import com.changyou.community.exception.CustomizeErrorCode;
import com.changyou.community.exception.CustomizeException;
import com.changyou.community.mapper.CommentMapper;
import com.changyou.community.mapper.NotificationMapper;
import com.changyou.community.mapper.QuestionMapper;
import com.changyou.community.mapper.UserMapper;
import com.changyou.community.model.Comment;
import com.changyou.community.model.Notification;
import com.changyou.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    public void insert(Comment comment,User commentator) {
        System.out.println("0000000000000000000000000000000000000000");
        if(comment.getParentId()== null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        System.out.println("11111111111111111111111111111111111111111111");
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_NOT_EXIST);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论

            Comment commentByParentId = commentMapper.getCommentByParentId(comment.getParentId());
            if(commentByParentId == null){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_EXISIT);
            }else {
                commentMapper.insert(comment);

                //插入notification
                Question question = questionMapper.getById(Integer.parseInt(commentByParentId.getParentId()+""));
                if(question == null){
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                //创建通知
                createNotify(comment,Long.parseLong(commentByParentId.getCommentator()+""),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_COMMENT,Long.parseLong(question.getId()+""));

            }

        }else{
            //回复问题
            System.out.println("2222222222222222222222222222222222222222222222");
            Integer parentId=new Integer(comment.getParentId().intValue());
            Question question = questionMapper.getById(parentId);
            if(question == null){
                System.out.println("33333333333333333333333333333333333333333333");
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }else {
                System.out.println("comment:"+comment);
                commentMapper.insert(comment);
                System.out.println("question:"+question);
                questionMapper.updateCommentCount(question);//增加评论数

                System.out.println("commentator.getName():"+commentator.getName());
                System.out.println("commentator.getAccountId():"+commentator.getAccountId());
                System.out.println("question.getId():"+question.getId());

                try {
                    createNotify(comment, Long.parseLong(question.getCreator() + ""), commentator.getAccountId(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId().longValue());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }

    public List<CommentDTO> getCommentListById(Integer id){
        List<Comment> commentList = commentMapper.getListById(id);
        System.out.println("commentList:"+commentList);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        User user = new User();

        for (Comment comment : commentList) {
            //Integer parentId=new Integer(comment.getParentId().intValue());
            user = userMapper.findByAccountId(String.valueOf(comment.getCommentator()));
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public List<CommentDTO> listByQuestionId(Integer id) {
        return this.getCommentListById(id);
    }

    public List<CommentDTO> listByCommentId(Integer id) {
        return this.getCommentListById(id);
    }

    public void addCommentCountById(Long id){
        commentMapper.addCommentCountById(id);
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(Long.parseLong(comment.getCommentator()+""));
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        notificationMapper.insert(notification);
    }
}
