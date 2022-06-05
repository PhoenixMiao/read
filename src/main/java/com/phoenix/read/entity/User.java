package com.phoenix.read.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * @author lishuai
 * @version 2021/2/2 21:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("User 用户")
public class User {

    @Id
    @ApiModelProperty("用户id")
    @GeneratedValue(generator = "JDBC",strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("会话id")
    private String sessionId;

    @ApiModelProperty("用户唯一标识")
    private String openId;

    @ApiModelProperty("unionid")
    private String unionId;

    @ApiModelProperty("会话密钥")
    private String sessionKey;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("学院")
    private String department;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("年级")
    private String grade;

    @ApiModelProperty("学号")
    private String studentId;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("QQ")
    private String QQ;

    @ApiModelProperty("微信号")
    private String wechatNum;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("禁言与否（1为禁言，0为未禁言）")
    private Integer isMute;

    @ApiModelProperty("主办方id")
    private Long organizerId;

    @Version
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("用户信息乐观锁组件")
    private Integer version;

}
