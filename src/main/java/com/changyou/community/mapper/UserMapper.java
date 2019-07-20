package com.changyou.community.mapper;

import com.changyou.community.dto.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);

    @Select("select * from user where token = #{token}")
    User getUserByToken(String token);

    @Select("select * from user where id = #{id}")
    User getUserById(Integer id);

    @Select("select * from user where id = #{id}")
    User findById(Integer id);
}
