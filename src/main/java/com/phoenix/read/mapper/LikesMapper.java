package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Likes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesMapper extends MyMapper<Likes> {

    @Select("SELECT * FROM likes WHERE user_id = #{userId} AND passage_id = #{passageId}")
    List<Likes> isLike(@Param("userId")Long userId, @Param("passageId")Long passageId);
}
