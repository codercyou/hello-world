package com.changyou.community.mapper;

import com.changyou.community.dto.QuestionDTO;
import com.changyou.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void createQuestion(Question question);

    @Select("select * from question")
    public List<Question> list1();

    @Select("select * from question order by id desc limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);


    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{id} order by id desc limit #{offset},#{size} ")
    List<Question> listByUser(@Param(value = "id") Integer id, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator=#{id}")
    Integer countByUser(Integer id);

    @Select("select * from question where id = #{id}")
    Question getById(Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag},view_count = #{viewCount} where id = #{id}")
    int update(Question question1);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag},view_count = view_count +1 where id = #{id}")
    int updateViewCount(Question question1);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag},comment_count = comment_count +1 where id = #{id}")
    int updateCommentCount(Question question1);

    @Select("select * from question where id != #{id} and tag regexp #{tag}")
    List<QuestionDTO> getRelatedQuestion(QuestionDTO questionDTO);


    @Select("select * from question where title regexp #{search} order by id desc limit #{offset},#{size}")
    List<Question> listForSearch(@Param(value = "search") String search, @Param(value = "offset") Integer offset, @Param(value = "size" ) Integer size);

    @Select("select count(1) from question where title regexp #{search} ")
    Integer countForSearch(String search);
}