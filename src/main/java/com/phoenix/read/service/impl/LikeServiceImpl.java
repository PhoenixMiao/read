package com.phoenix.read.service.impl;

import com.phoenix.read.entity.Like;
import com.phoenix.read.mapper.LikeMapper;
import com.phoenix.read.service.LikeService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private TimeUtil timeUtil;

    @Override
    public Long like(Long passageId,Long userId){
        Like like=new Like(null,userId,passageId,timeUtil.getCurrentTimestamp());
        likeMapper.insert(like);
        return like.getId();
    }

    @Override
    public  String cancelLike(Long id){
        likeMapper.deleteByPrimaryKey(id);
        return "取消成功";
    }
}
