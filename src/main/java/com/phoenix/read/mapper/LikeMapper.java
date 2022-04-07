package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Like;
import com.phoenix.read.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMapper  extends MyMapper<Like> {

    @Insert("INSERT INTO likes(user_id,passage_id) VALUES (#{user_id},#{passage_id}")
    Long like(@Param("user_id")Long userId,
                    @Param("passage_id") Long passageId
    );
}
