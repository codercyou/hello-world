package com.changyou.community.mapper;

import com.changyou.community.model.Ad;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdMapper {

    @Select("select * from ad where gmt_start<#{l} and gmt_end >#{l} and status=1")
    public List<Ad> getAds(long l);
}
