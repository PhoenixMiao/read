package com.phoenix.read.service;

import com.phoenix.read.controller.request.NewPushRequest;
import com.phoenix.read.controller.response.NewPushResponse;
import com.phoenix.read.entity.Push;

public interface PushService {
    NewPushResponse newPush(NewPushRequest newPushRequest, Long userId);

    Push getPushById(Long pushId);

    NewPushResponse summarizePush(NewPushRequest newPushRequest,Long userId);
}
