package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Collection;
import com.phoenix.read.entity.Likes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionMapper extends MyMapper<Collection> {

    @Select("SELECT * FROM collection WHERE user_id = #{userId} AND passage_id = #{passageId} LIMIT 1")
    Collection isCollect(@Param("userId")Long userId, @Param("passageId")Long passageId);

    @Select("SELECT * FROM collection WHERE user_id = #{userId}")
    List<Collection> getCollectionList(@Param("userId")Long userId);
}
