package com.phoenix.read.service;

import com.phoenix.read.common.Page;
import com.phoenix.read.controller.request.NewPushRequest;
import com.phoenix.read.controller.response.NewPushResponse;
import com.phoenix.read.dto.BriefPush;
import com.phoenix.read.entity.Push;

public interface PushService {
    NewPushResponse newPush(NewPushRequest newPushRequest, Long userId);

    Push getPushById(Long pushId);

    NewPushResponse summarizePush(NewPushRequest newPushRequest,Long userId);

    Page<BriefPush> getPushListByActivityType(int pageSize, int pageNum, Integer activityType);
}
