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


    @Select("select count(*) from notification where NOTIFIER_NAME = #{userId} and status=0")
    public Integer countByUserId(@Param(value = "userId") String userId);

    @Select("select count(*) from notification where NOTIFIER_NAME = #{userId} and status = #{status}")
    Long countUnreadCount(@Param(value = "userId") String userId, @Param(value = "status") int status);


    @Select("select * from notification where NOTIFIER_NAME = #{userId} and status = 0 order by gmt_create desc limit #{offset},#{size} ")
    List<Notification> listByUserId(@Param(value = "userId") String userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);


    @Select("select * from notification where id = #{id}")
    Notification selectByPrimaryKey(Long id);

    @Update("update notification set notifier = #{notifier}, receiver = #{receiver}, outerId = #{outerId}, type = #{type},gmt_create = #{gmtCreate}, status=#{status},NOTIFIER_NAME=#{notifierName},OUTER_TITLE = #{outerTitle} where id = #{id}")
    void updateByPrimaryKey(Notification notification);
}
