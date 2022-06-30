package com.phoenix.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.common.Page;
import com.phoenix.read.common.PageParam;
import com.phoenix.read.controller.request.CollectionResult;
import com.phoenix.read.entity.Collection;
import com.phoenix.read.mapper.CollectionMapper;
import com.phoenix.read.mapper.PassageMapper;
import com.phoenix.read.service.CollectionService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private PassageMapper passageMapper;


    @Override
    public Long collect(Long passageId,Long userId) throws CommonException {
        List<Collection> collections = collectionMapper.isCollect(passageId, userId);
        if(collections.size() == 0) {
            Collection collections2 = new Collection(null, userId, passageId, TimeUtil.getCurrentTimestamp());
            collectionMapper.insert(collections2);
            return (long)1;
        }else{
            for(Collection collection:collections)
                collectionMapper.deleteByPrimaryKey(collection.getId());
            return (long)0;
        }

    }

    @Override
    public Integer isCollect(Long passageId,Long userId){
        if(collectionMapper.isCollect(passageId,userId).size()!=0) return 1;
        else return 0;
    }

    @Override
    public Page<CollectionResult> getCollectionList(Long userId, PageParam pageParam){
        PageHelper.startPage(pageParam.getPageNum(),pageParam.getPageSize(),pageParam.getOrderBy());
        ArrayList<CollectionResult> collectionResults = new ArrayList<>();
        List<Collection> collections = collectionMapper.getCollectionList(userId);
        for(Collection collection:collections){
            collectionResults.add(CollectionResult.builder()
                    .passage(passageMapper.selectByPrimaryKey(collection.getPassageId()))
                    .collectTime(collection.getCollectTime())
                    .build());
        }
        return new Page<>(new PageInfo<>(collectionResults));
    }
}
