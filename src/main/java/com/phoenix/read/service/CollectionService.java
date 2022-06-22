package com.phoenix.read.service;

public interface CollectionService {
    Long collect(Long passageId,Long userId);

    Integer isCollect(Long userId,Long passageId);
}
