package com.phoenix.read.service.impl;

import com.phoenix.read.common.CommonException;
import com.phoenix.read.entity.Likes;
import com.phoenix.read.mapper.LikesMapper;
import com.phoenix.read.service.LikesService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesServiceImpl implements LikesService {

    @Autowired
    private LikesMapper likesMapper;

    @Override
    public Long like(Long passageId,Long userId) throws CommonException {
        List<Likes> likes = likesMapper.isLike(passageId,userId);
        if(likes.size() == 0) {
            Likes likes2 = new Likes(null, userId, passageId, TimeUtil.getCurrentTimestamp());
            likesMapper.insert(likes2);
            return (long)1;
        }else{
            for(Likes like:likes){
                likesMapper.deleteByPrimaryKey(like.getId());
            }
            return (long)0;
        }

    }

    @Override
    public Integer isLike(Long passageId,Long userId){
        List<Likes> likes = likesMapper.isLike(passageId, userId);
        if(likes.size() == 0)  return 1;
        else return 0;
    }
}
