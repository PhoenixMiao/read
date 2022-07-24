package com.phoenix.read.controller.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("UserResponse 用户昵称和用户头像")
public class UserResponse {
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户头像")
    private String portrait;

    @ApiModelProperty("用户昵称")
    private String nickname;
}
