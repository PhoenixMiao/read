package com.phoenix.read.service;

public interface LikeService {

    Long like(Long passageId,Long userId);

    String cancelLike(Long id);
}
