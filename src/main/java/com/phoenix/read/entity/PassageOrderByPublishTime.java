package com.phoenix.read.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import java.util.Comparator;

public class PassageOrderByPublishTime implements Comparator<Passage> {


    public int compare(Passage passageA,Passage passageB)
    {
        return passageA.getPublishTime().compareTo(passageB.getPublishTime());
    }

}
