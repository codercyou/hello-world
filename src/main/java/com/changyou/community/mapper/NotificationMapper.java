package com.changyou.community.mapper;

import com.changyou.community.dto.User;
import com.changyou.community.model.Notification;
import com.changyou.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification (notifier,receiver,outerId,type,gmt_create,status,NOTIFIER_NAME,OUTER_TITLE) values (#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    public void insert(Notification notification);


    @Select("select count(*) from notification where NOTIFIER_NAME = #{userId}")
    public Integer countByUserId(String userId);

    @Select("select count(*) from notification where NOTIFIER_NAME = #{userId} and status = #{status}")
    Long countUnreadCount(String userId, int status);


    @Select("select * from notification where NOTIFIER_NAME = #{userId} order by id desc limit #{offset},#{size} ")
    List<Notification> listByUserId(String userId, Integer offset, Integer size);


    @Select("select * from notification where id = #{id}")
    Notification selectByPrimaryKey(Long id);

    @Update("update notification set notifier = #{notifier}, receiver = #{receiver}, outerId = #{outerId}, type = #{type},gmt_create = #{gmtCreate}, status=#{status},NOTIFIER_NAME=#{notifierName},OUTER_TITLE = #{outerTitle} where id = #{id}")
    void updateByPrimaryKey(Notification notification);
}
