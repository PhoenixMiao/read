package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Likes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesMapper extends MyMapper<Likes> {

    @Insert("INSERT INTO likes(user_id,passage_id) VALUES (#{user_id},#{passage_id}")
    Long like(@Param("user_id")Long userId,
                    @Param("passage_id") Long passageId
    );

    @Select("SELECT * FROM likes WHERE user_id = #{userId} AND passage_id = #{passageId} LIMIT 1")
    Likes isLike(@Param("userId")Long userId, @Param("passageId")Long passageId);
}
