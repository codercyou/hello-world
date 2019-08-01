package com.changyou.community.mapper;

import com.changyou.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {


    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values(#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    public void insert(Comment comment);

    @Select("select * from comment where parent_id = #{parentId}")
    Comment getCommentByParentId(Long parentId);

    @Select("select * from comment where parent_id = #{parentId}")
    List<Comment> getListById(Integer parentId);
}
