package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.dto.BriefActivity;
import com.phoenix.read.entity.Activity;
import com.phoenix.read.entity.Push;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityMapper extends MyMapper<Activity> {
    @Insert("INSERT INTO activity(type,publisherId) VALUES (#{type},#{publisherId});")
    Long newActivity(@Param("type")Integer type,@Param("publisherId")Long publisherId);

    @Update("UPDATE activity SET name=#{name},status=#{status},organizerId=#{organizerId},startTime=#{startTime},endTime=#{endTime},place=#{place},time=#{time},orderStartTime=#{orderStartTime},orderEndTime=#{orderEndTime},people=#{people},introduction=#{introduction},isCheck=#{isCheck} WHERE id=#{id};")
    void updateActivity(@Param("name")String name, @Param("status")Integer status,@Param("organizerId")Long organizerId,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("place")String place,@Param("time")Integer time,@Param("orderStartTime")String orderStartTime,@Param("orderEndTime")String orderEndTime,
                        @Param("people")Integer people,@Param("introduction")String introduction,@Param("isCheck")Integer isCheck);


    @Select("SELECT * FROM activity WHERE id=#{id}")
    Activity getActivityById(@Param("id")Long id);

    @Update("UPDATE activity SET status=#{status} WHERE id=#{id};")
    void updateStatus(@Param("status")Integer status,@Param("id")Long id);

    @Select("SELECT id,startTime,name FROM activity WHERE organizerId=#{organizerId}")
    List<BriefActivity> getBriefActivityListByOrganizer(@Param("organizerId")Long organizerId);

    @Update("UPDATE activity SET people=#{people} WHERE id=#{id}")
    void makeOrder(@Param("people")Integer people,@Param("id")Long id);

    @Select("SELECT id,startTime,name FROM activity WHERE publisherId=#{publisherId}")
    List<BriefActivity> getBriefActivityListByPublisherId(@Param("publisherId")Long publisherId);
}
