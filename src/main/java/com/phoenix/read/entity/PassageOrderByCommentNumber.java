package com.phoenix.read.entity;

import com.phoenix.read.mapper.CommentMapper;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import java.util.Comparator;
import java.util.List;

public class PassageOrderByCommentNumber implements Comparator<Passage> {

    @Autowired
    private CommentMapper commentMapper;

    public int compare(Passage passageA,Passage passageB)
    {
        List<Long> thisCommentList =commentMapper.getCommentList(passageA.getId(),0);
        List<Long> anotherCommentList =commentMapper.getCommentList(passageB.getId(),0);
        return thisCommentList.size()-anotherCommentList.size();
    }

}
