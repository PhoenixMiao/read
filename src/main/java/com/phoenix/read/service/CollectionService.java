package com.phoenix.read.service;

import com.phoenix.read.common.Page;
import com.phoenix.read.common.PageParam;
import com.phoenix.read.controller.request.CollectionResult;
import com.phoenix.read.entity.Collection;

public interface CollectionService {
    Long collect(Long passageId,Long userId);

    Integer isCollect(Long passageId,Long userId);

    Page<CollectionResult> getCollectionList(Long userId, PageParam pageParam);
}
