package com.phoenix.read.service.impl;

import com.phoenix.read.common.CommonErrorCode;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.controller.request.GetPassageListRequest;
import com.phoenix.read.controller.request.PostRequest;
import com.phoenix.read.controller.response.GetPassageResponse;
import com.phoenix.read.dto.BriefPassage;
import com.phoenix.read.entity.Passage;
import com.phoenix.read.mapper.CommentMapper;
import com.phoenix.read.mapper.PassageMapper;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.service.PassageService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageServiceImpl implements PassageService {

    @Autowired
    private PassageMapper passageMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TimeUtil timeUtil;

    @Override
    public List<BriefPassage> getPassageList(GetPassageListRequest getPassageListRequest){
        return passageMapper.getPassageList(getPassageListRequest.getType(),getPassageListRequest.getSubtype());
    }

    @Override
    public GetPassageResponse getPassageById(Long passageId){
        List<Long> commentsId=commentMapper.getCommentList(passageId,0);
        Passage passage=passageMapper.selectByPrimaryKey(passageId);
        GetPassageResponse getPassageResponse=new GetPassageResponse(passage,commentsId);
        return getPassageResponse;
    }

    @Override
    public Long postNewPassage(PostRequest postRequest,Long userId){
        if(userMapper.selectByPrimaryKey(userId).getIsMute()==1) throw new CommonException(CommonErrorCode.USER_IS_MUTE);
        Passage passage=new Passage(null,postRequest.getType(),postRequest.getSubtype(),userId,postRequest.getContent(),timeUtil.getCurrentTimestamp(),postRequest.getTitle());
        return (long) passageMapper.insert(passage);
    }
}
