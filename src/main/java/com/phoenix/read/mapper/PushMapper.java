package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Push;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PushMapper extends MyMapper<Push> {
    @Insert("INSERT INTO push(title,activityId,publishDate,content,type,picture,source) VALUES (#{title},#{activityId},#{publishDate},#{content},#{type},#{picture},#{source});")
    Long newPush(@Param("title")String title,@Param("activityId")Long activityId,@Param("publishDate")String publishDate,@Param("content")String content,@Param("type")Integer type,@Param("picture")String picture,@Param("source")String source);

    @Select("SELECT * FROM push WHERE id=#{id}")
    Push getPushById(@Param("id")Long id);
}
