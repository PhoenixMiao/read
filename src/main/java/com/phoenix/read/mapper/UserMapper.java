package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.dto.BriefUser;
import com.phoenix.read.entity.User;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yannis
 * @version 2020/11/7 9:17
 */

@Repository
public interface UserMapper extends MyMapper<User> {
    @Select("SELECT id,name,nickname FROM user")
    List<BriefUser> getBriefUser();

    @Update("UPDATE user SET type=#{type} WHERE id=#{id}")
    void toAdmin(@Param("type")Integer type,@Param("id")Long id);

    @Update("UPDATE user SET nickname=#{nickname},department=#{department},major=#{major},grade=#{grade},telephone=#{telephone},QQ=#{QQ},wechatNum=#{wechatNum},portrait=#{portrait} WHERE id=#{id};")
    void updateUser(@Param("nickname")String nickname,@Param("department")String department,@Param("major")String major,@Param("grade")String grade,@Param("telephone")String telephone,@Param("QQ")String QQ,@Param("wechatNum")String wechatNum,@Param("portrait")String portrait,@Param("id")Long id);

    @Update("UPDATE user SET isMute=#{isMute} WHERE id=#{id}")
    void muteUser(@Param("isMute")Integer isMute,@Param("id")Long id);

    @Update("UPDATE user SET organizerId=#{organizerId} WHERE id=#{id}")
    void classifyUser(@Param("organizerId")Long organizerId,@Param("id")Long id);
}
