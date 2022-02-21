package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.dto.BriefPassage;
import com.phoenix.read.entity.Organizer;
import com.phoenix.read.entity.Passage;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassageMapper extends MyMapper<Passage> {
    @Select("SELECT * FROM passage WHERE type=#{type} AND subtype=#{subtype}")
    List<Passage> getPassageList(@Param("type") Integer type, @Param("subtype")Integer subtype);
}

