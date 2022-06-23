package com.phoenix.read.service;

import com.phoenix.read.common.Page;
import com.phoenix.read.common.PageParam;
import com.phoenix.read.entity.Collection;

public interface CollectionService {
    Long collect(Long passageId,Long userId);

    Integer isCollect(Long userId,Long passageId);

    Page<Collection> getCollectionList(Long userId, PageParam pageParam);
}
