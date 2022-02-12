package com.phoenix.read.service.impl;

import com.phoenix.read.common.CommonErrorCode;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.controller.request.NewPushRequest;
import com.phoenix.read.controller.response.NewPushResponse;
import com.phoenix.read.entity.Push;
import com.phoenix.read.entity.User;
import com.phoenix.read.mapper.ActivityMapper;
import com.phoenix.read.mapper.PushMapper;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.service.PushService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;

@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private PushMapper pushMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public NewPushResponse newPush(NewPushRequest newPushRequest, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getType()==0) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        Long activityId = activityMapper.newActivity(newPushRequest.getType(),userId);
        Long pushId = pushMapper.newPush(newPushRequest.getTitle(),activityId,TimeUtil.getCurrentTimestamp(),newPushRequest.getContent(),0,newPushRequest.getPicture(),newPushRequest.getSource());
        return new NewPushResponse(pushId,activityId);
    }

    @Override
    public NewPushResponse summarizePush(NewPushRequest newPushRequest,Long userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getType()==0) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        Long pushId = pushMapper.newPush(newPushRequest.getTitle(),newPushRequest.getActivityId(),TimeUtil.getCurrentTimestamp(),newPushRequest.getContent(),0,newPushRequest.getPicture(),newPushRequest.getSource());
        return new NewPushResponse(pushId,newPushRequest.getActivityId());
    }

    @Override
    public Push getPushById(Long pushId) {
        return pushMapper.getPushById(pushId);
    }

}
