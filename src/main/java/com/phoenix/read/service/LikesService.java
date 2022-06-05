package com.phoenix.read.service;

public interface LikesService {

    Long like(Long passageId,Long userId);

    Integer isLike(Long userId,Long passageId);
}
