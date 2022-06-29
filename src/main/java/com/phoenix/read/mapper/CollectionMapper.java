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

    @Select("SELECT * FROM collection WHERE passage_id = #{passageId}  AND user_id = #{userId}LIMIT 1")
    Collection isCollect(@Param("passageId")Long passageId,@Param("userId")Long userId);

    @Select("SELECT * FROM collection WHERE user_id = #{userId}")
    List<Collection> getCollectionList(@Param("userId")Long userId);
}
