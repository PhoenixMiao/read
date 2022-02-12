package com.phoenix.read.service.impl;

import com.phoenix.read.controller.request.CommentRequest;
import com.phoenix.read.entity.Comment;
import com.phoenix.read.mapper.CommentMapper;
import com.phoenix.read.service.CommentService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TimeUtil timeUtil;

    @Override
    public Comment getCommentById(Long id){
        Comment comment=commentMapper.selectByPrimaryKey(id);
        return comment;
    }

    @Override
    public List<Long> getItsCommentsIdById(Long commentId){
        return commentMapper.getCommentList(commentId,1);
    }

    @Override
    public String addComment(CommentRequest commentRequest,Long userId){
        if(commentRequest.getObjectType().equals(1)){
            Comment comment=commentMapper.selectByPrimaryKey(commentRequest.getObjectId());
            if(comment.getObjectType().equals(1)){
                return "不能评论此评论";
            }
        }
        commentMapper.addComment(commentRequest.getObjectId(),commentRequest.getObjectType(),userId,timeUtil.getCurrentTimestamp(),commentRequest.getComment());
        return "评论成功";
    }
}