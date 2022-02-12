package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.dto.BriefPassage;
import com.phoenix.read.entity.Comment;
import com.phoenix.read.entity.Organizer;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends MyMapper<Comment> {
    @Select("SELECT id FROM comment WHERE objectId=#{objectId} AND objectType=#{objectType}")
    List<Long> getCommentList(@Param("objectId") Long objectId, @Param("objectType")Integer objectType);

    @Insert("INSERT INTO comment(objectId,objectType,userId,commentTime,comment) VALUES (#{objectId},#{objectType},#{userId},#{commentTime},#{comment}")
    Long addComment(@Param("objectId")Long objectId,
                    @Param("objectType") Integer objectType,
                    @Param("userId") Long userId,
                    @Param("commentTime") String commentTime,
                    @Param("comment") String comment
                    );

}
