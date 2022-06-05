package com.phoenix.read.service.impl;

import com.phoenix.read.common.CommonException;
import com.phoenix.read.entity.Likes;
import com.phoenix.read.mapper.LikesMapper;
import com.phoenix.read.service.LikesService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesServiceImpl implements LikesService {

    @Autowired
    private LikesMapper likesMapper;

    @Override
    public Long like(Long passageId,Long userId) throws CommonException {
        Likes likes = likesMapper.isLike(userId,passageId);
        if(likes ==null) {
            Likes likes2 = new Likes(null, userId, passageId, TimeUtil.getCurrentTimestamp());
            likesMapper.insert(likes2);
            return likes2.getId();
        }else{
            likesMapper.deleteByPrimaryKey(likes.getId());
            return likes.getId();
        }

    }

    @Override
    public Integer isLike(Long userId,Long passageId){
        if(likesMapper.isLike(userId,passageId)!=null) return 1;
        else return 0;
    }
}
