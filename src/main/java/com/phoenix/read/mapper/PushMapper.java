package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.dto.BriefPush;
import com.phoenix.read.entity.Push;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushMapper extends MyMapper<Push> {
    @Select("SELECT id,title,picture,type,activity_id FROM push WHERE activity_type=#{activity_type}")
    List<BriefPush> getPushList(@Param("activity_type")Integer activityType);

    @Select("SELECT id,title,picture,type,activity_id FROM push WHERE activity_id=#{activity_id}")
    List<BriefPush> getPushByActivityId(@Param("activity_id")Long activityId);

    @Select("SELECT * FROM push WHERE activity_id=#{activity_id}")
    List<Push> getPushListByActivityId(@Param("activity_id")Long activityId);
}
