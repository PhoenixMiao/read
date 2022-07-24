package com.phoenix.read.controller;

import com.phoenix.read.annotation.Auth;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.common.Result;
import com.phoenix.read.controller.request.UpdateUserRequest;
import com.phoenix.read.controller.response.UserResponse;
import com.phoenix.read.dto.BriefUser;
import com.phoenix.read.dto.SessionData;
import com.phoenix.read.service.UserService;
import com.phoenix.read.util.SessionUtils;
import io.netty.handler.codec.CodecException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api("用户相关操作")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtils sessionUtils;

    @GetMapping("/login/{code}")
    @ApiOperation(value = "登录",response = SessionData.class)
    @ApiImplicitParam(name = "code", value = "code", required = true, paramType = "path")
    public Result login(@NotBlank @PathVariable("code") String code){

        return Result.success(userService.login(code));

    }

    @Auth
    @GetMapping("/list")
    @ApiOperation(value = "查看所有用户",response = BriefUser.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页显示数量 (不小于0)",required = true,paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页数 (不小于0)", required = true, paramType = "query", dataType = "Integer"),})
    public Result getBriefUserList(@NotNull @RequestParam("pageSize")Integer pageSize,
                                   @NotNull @RequestParam("pageNum")Integer pageNum){
        try{
            return Result.success(userService.getBriefUserList(pageSize,pageNum,sessionUtils.getUserId()));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode(),sessionUtils.getUserId());
        }
    }

    @Auth
    @GetMapping("/admin")
    @ApiOperation(value = "超管将普通用户改为管理员",response = Long.class)
    @ApiImplicitParam(name = "userId",value = "所需要被设置的用户的id",required = true,paramType = "query")
    public Result toAdmin(@NotNull@RequestParam("userId")Long userId){
        try{
            userService.toAdmin(userId,sessionUtils.getUserId());
            return Result.success(userId);
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/mute")
    @ApiOperation(value = "管理员将别人禁言",response = Long.class)
    @ApiImplicitParam(name = "userId",value = "所需要被设置的用户的id",required = true,paramType = "query")
    public Result mute(@NotNull@RequestParam("userId")Long userId){
        try{
            userService.mute(userId,sessionUtils.getUserId());
            return Result.success(userId);
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("")
    @ApiOperation(value = "获取个人信息")
    public Result getUserById(){
        return Result.success(userService.getUserById(sessionUtils.getUserId()));
    }

    @Auth
    @PostMapping("/change")
    @ApiOperation(value = "更新用户信息")
    public Result updateUser(@NotNull @Valid @RequestBody UpdateUserRequest updateUserRequest){
        userService.UpdateUser(sessionUtils.getUserId(),updateUserRequest);
        return Result.success("ok");
    }

    @Auth
    @GetMapping("/user")
    @ApiOperation(value = "超管将管理员改为普通用户",response = Long.class)
    @ApiImplicitParam(name = "userId",value = "所需要被设置的用户的id",required = true,paramType = "query")
    public Result backToUser(@NotNull@RequestParam("userId")Long userId){
        try{
            userService.backToUser(userId,sessionUtils.getUserId());
            return Result.success(userId);
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @GetMapping("/organize")
    @ApiOperation(value = "超管将用户绑定到特定主办方中",response = Long.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "所需要被设置的用户的id",required = true,paramType = "query"),
            @ApiImplicitParam(name = "organizerId",value = "主办方id",required = true,paramType = "query")
    })
    public Result backToUser(@NotNull@RequestParam("userId")Long userId,
                             @NotNull @RequestParam("organizerId")Long organizerId){
        try{
            userService.classifyUser(organizerId,userId,sessionUtils.getUserId());
            return Result.success(userId);
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping("/stu")
    @ApiOperation(value = "填写学号和姓名",response = SessionData.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "studentId",value = "学号",required = true,paramType = "query"),
            @ApiImplicitParam(name = "name",value = "姓名",required = true,paramType = "query"),
    })
    public Result student(@NotNull @RequestParam("studentId")String stuId,
                          @NotNull @RequestParam("name")String name){
        try{
            userService.inputStuIdAndName(sessionUtils.getUserId(),stuId,name);
            return Result.success(userService.getUserById(sessionUtils.getUserId()));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }


    @Auth
    @GetMapping("/mes")
    @ApiOperation(value = "根据userId获取昵称和头像",response = UserResponse.class)
    @ApiImplicitParam(name = "userId",value = "所需要被设置的用户的id",required = true,paramType = "query")
    public Result getUser(@NotNull @RequestParam("userId")String userId){
        try{
            return Result.success(userService.getUser(Long.parseLong(userId)));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }
    }

    @Auth
    @PostMapping(value = "/upload", produces = "application/json")
    @ApiOperation(value = "上传用户头像")
    public Object uploadPortrait(MultipartFile file) {
        try{
            return Result.success(userService.uploadPortrait(sessionUtils.getUserId(), file));
        }catch (CommonException e){
            return Result.result(e.getCommonErrorCode());
        }

    }
}
