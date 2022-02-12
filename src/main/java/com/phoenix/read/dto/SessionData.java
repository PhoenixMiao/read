package com.phoenix.read.dto;

import com.phoenix.read.common.CommonErrorCode;
import com.phoenix.read.entity.User;
import com.phoenix.read.util.AssertUtil;
import com.phoenix.read.util.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * session缓存实体
 * @author yan on 2020-02-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("SessionData 会话实体")
public class SessionData implements Serializable {

    /**
     * {@link User}
     */
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("会话id")
    private String sessionId;

    @ApiModelProperty("创建时间")
    private String create_time;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("学校")
    private String school;

    @ApiModelProperty("学院")
    private String department;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("年级")
    private String grade;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("微信号")
    private String wecharNum;

    @ApiModelProperty("QQ")
    private String QQ;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("类型")
    private int type;

    public SessionData(User user){
        AssertUtil.notNull(user, CommonErrorCode.USER_NOT_EXIST);
//        if(user == null){
//            create_time = TimeUtil.getCurrentTimestamp();
//            nickname = "读书汇用户";
//            gender = 0;
//            return;
//        }
        id = user.getId();
        sessionId = user.getSessionId();
        create_time = user.getCreateTime();
        nickname = user.getNickname();
        gender = user.getGender();
        school = user.getSchool();
        department = user.getDepartment();
        major = user.getMajor();
        grade = user.getGrade();
        telephone = user.getTelephone();
        QQ = user.getQQ();
        portrait = user.getPortrait();
        type = user.getType();
    }
}
