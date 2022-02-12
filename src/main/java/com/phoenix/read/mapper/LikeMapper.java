package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Like;
import com.phoenix.read.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMapper  extends MyMapper<Like> {

    @Insert("INSERT INTO likes(userId,passageId) VALUES (#{userId},#{passageId}")
    Long like(@Param("userId")Long userId,
                    @Param("passageId") Long passageId
    );
}
