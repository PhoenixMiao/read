package com.phoenix.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.common.Page;
import com.phoenix.read.common.PageParam;
import com.phoenix.read.entity.Collection;
import com.phoenix.read.mapper.CollectionMapper;
import com.phoenix.read.service.CollectionService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;


    @Override
    public Long collect(Long passageId,Long userId) throws CommonException {
        Collection collections = collectionMapper.isCollect(userId,passageId);
        if(collections ==null) {
            Collection collections2 = new Collection(null, userId, passageId, TimeUtil.getCurrentTimestamp());
            collectionMapper.insert(collections2);
            return collections2.getId();
        }else{
            collectionMapper.deleteByPrimaryKey(collections.getId());
            return collections.getId();
        }

    }

    @Override
    public Integer isCollect(Long userId,Long passageId){
        if(collectionMapper.isCollect(userId,passageId)!=null) return 1;
        else return 0;
    }

    @Override
    public Page<Collection> getCollectionList(Long userId, PageParam pageParam){
        PageHelper.startPage(pageParam.getPageNum(),pageParam.getPageSize(),pageParam.getOrderBy());
        return new Page<>(new PageInfo<>(collectionMapper.getCollectionList(userId)));
    }
}
