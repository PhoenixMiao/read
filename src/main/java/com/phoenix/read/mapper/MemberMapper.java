package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMapper extends MyMapper<Member> {
    @Insert("INSERT INTO member(activityId,userId,createTime,status) VALUES(#{activityId},#{userId},#{createTime},#{status});")
    Long makeOrder(@Param("activityId")Long activityId,@Param("userId")Long userId,@Param("createTime")String createTime,@Param("status")Integer status);

    @Update("UPDATE member SET status=#{status} WHERE id=#{id}")
    void updateStatus(@Param("status")Integer status,@Param("id")Long id);

    @Select("SELECT * FROM member WHERE activityId=#{activityId} AND status=#{status};")
    List<Member> getUncommittedMemberList(@Param("activityId")Long activityId,@Param("status")Integer status);

    @Select("SELECT * FROM member WHERE userId=#{userId}")
    List<Member> getMemberListByUserId(@Param("userId")Long userId);

    @Select("SELECT * FROM member WHERE userId=#{userId} AND activityId=#{activityId} LIMIT 1")
    Member getMemberByUserIdAndActivityId(@Param("userId")Long userId,@Param("activityId")Long activityId);

    @Select("SELECT * FROM member WHERE activityId=#{activityId}")
    List<Member> getMemberListByActivityId(@Param("activityId")Long activityId);
}
