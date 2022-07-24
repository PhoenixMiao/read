package com.phoenix.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phoenix.read.common.CommonErrorCode;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.common.Page;
import com.phoenix.read.controller.request.NewPushRequest;
import com.phoenix.read.controller.request.SearchRequest;
import com.phoenix.read.controller.response.NewPushResponse;
import com.phoenix.read.dto.BriefActivity;
import com.phoenix.read.dto.BriefPush;
import com.phoenix.read.dto.Order;
import com.phoenix.read.entity.Activity;
import com.phoenix.read.entity.Push;
import com.phoenix.read.entity.User;
import com.phoenix.read.mapper.ActivityMapper;
import com.phoenix.read.mapper.PushMapper;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.service.PushService;
import com.phoenix.read.util.AssertUtil;
import com.phoenix.read.util.TimeUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import io.netty.channel.socket.ChannelOutputShutdownEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.phoenix.read.common.CommonConstants.COS_BUCKET_NAME;

@Service
public class PushServiceImpl implements PushService {

    @Autowired
    private PushMapper pushMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private COSClient cosClient;

    @Autowired
    private TransferManager transferManager;

    @Override
    public Page<BriefPush> searchPush(SearchRequest searchRequest){
        Example exampleOne = new Example(Push.class);

        if (!StringUtils.isEmpty(searchRequest.getTitle())) {
            Example.Criteria nameCriteria = exampleOne.createCriteria();
            nameCriteria.orLike("title", "%" + searchRequest.getTitle() + "%");
            exampleOne.and(nameCriteria);
        }

        if (!StringUtils.isEmpty(searchRequest.getContent())){
            Example.Criteria contentCriteria = exampleOne.createCriteria();
            contentCriteria.orLike("content","%" + searchRequest.getContent() + "%");
            exampleOne.and(contentCriteria);
        }

        PageHelper.startPage(searchRequest.getPageParam().getPageNum(),
                searchRequest.getPageParam().getPageSize(),
                searchRequest.getPageParam().getOrderBy());
        List<Push> pushListOne = pushMapper.selectByExample(exampleOne);

        Example exampleTwo = new Example(User.class);

        if (!StringUtils.isEmpty(searchRequest.getPublisher())) {
            Example.Criteria nameCriteria = exampleTwo.createCriteria();
            nameCriteria.orLike("nickname", "%" + searchRequest.getPublisher() + "%");
            exampleTwo.and(nameCriteria);
        }
        if (!StringUtils.isEmpty(searchRequest.getOrganizer())) {
            Example.Criteria nameCriteria = exampleTwo.createCriteria();
            nameCriteria.orLike("nickname", "%" + searchRequest.getPublisher() + "%");
            exampleTwo.and(nameCriteria);
        }
        List<User> userList = userMapper.selectByExample(exampleTwo);
        List<Long> activityIdList=new ArrayList<>();
        for(User user:userList){
            activityIdList.addAll(activityMapper.getRelevantActivityIdByUser(user.getId()));
        }
        List<Push> pushListTwo=new ArrayList<>();
        for(Long activityId:activityIdList){
            pushListTwo.addAll(pushMapper.getPushListByActivityId(activityId));
        }

        PageHelper.startPage(searchRequest.getPageParam().getPageNum(),
                searchRequest.getPageParam().getPageSize(),
                searchRequest.getPageParam().getOrderBy());
        pushListOne.addAll(pushListTwo);
        Page page = new Page(new PageInfo(pushListOne));

        ArrayList<BriefPush> responseList = new ArrayList<>();
        for (Push ele : pushListOne) {
            responseList.add(new BriefPush(ele.getId(), ele.getTitle(), ele.getPicture(), ele.getType(), ele.getActivityId(), ele.getActivityType()));
        }
        return new Page<>(searchRequest.getPageParam(), page.getTotal(), page.getPages(), responseList);
    }

    @Override
    public NewPushResponse newPush(NewPushRequest newPushRequest, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getType()==0) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        Activity activity = Activity.builder()
                .type(newPushRequest.getType())
                .publisherId(userId)
                .build();
        activityMapper.insert(activity);
        Long activityId = activity.getId();
        Push push = Push.builder()
                .activityId(activityId)
                .title(newPushRequest.getTitle())
                .publishDate(TimeUtil.getCurrentTimestamp())
                .type(0)
                .content(newPushRequest.getContent())
                .activityType(newPushRequest.getType())
                .build();
        pushMapper.insert(push);
        Long pushId = push.getId();
        return new NewPushResponse(pushId,activityId);
    }

    @Override
    public NewPushResponse summarizePush(NewPushRequest newPushRequest,Long userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getType()==0) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        Push push = Push.builder()
                .activityId(newPushRequest.getActivityId())
                .title(newPushRequest.getTitle())
                .publishDate(TimeUtil.getCurrentTimestamp())
                .type(1)
                .content(newPushRequest.getContent())
                .activityType(newPushRequest.getType())
                .build();
        pushMapper.insert(push);
        Long pushId = push.getId();
        return new NewPushResponse(pushId,newPushRequest.getActivityId());
    }

    @Override
    public Page<BriefPush> getPushListByActivityType(int pageSize, int pageNum, Integer activityType) {
        PageHelper.startPage(pageNum,pageSize,"publish_date desc");
        //todo 用了一个返回体里没有的东西，如果不行的话就改到SQL语句里
        return new Page<>(new PageInfo<>(pushMapper.getPushList(activityType)));
    }

    @Override
    public List<BriefPush> rollingAd() {
        ArrayList<BriefPush> briefPushes = new ArrayList<>();
        for(int i=1;briefPushes.size()<4||i<10;i++) {
            PageHelper.startPage(i, 10, "end_time desc");
            //todo 分页总顺序按截止时间拍，拍完取出页来，每一页中按参与人数拍，不知道能不能做到
            Page<Activity> ActivityPage = new Page<Activity>(new PageInfo<Activity>(activityMapper.getNotEndActivity()));
            for (Activity ele : ActivityPage.getList()) {
                if (ele.getEndTime().compareTo(TimeUtil.getCurrentTimestamp()) <= 0) break;
                else {
                    List<BriefPush> briefPushList = pushMapper.getPushByActivityId(ele.getId());
                    BriefPush briefPush = new BriefPush();
                    for (BriefPush e : briefPushList) {
                        if (e.getType() == 0) briefPush = e;
                    }
                    if (briefPushes.size() < 4) briefPushes.add(briefPush);
                    else {
                        briefPushes.sort(new Comparator<BriefPush>() {
                                             @Override
                                             public int compare(BriefPush o1, BriefPush o2) {
                                                 return activityMapper.selectByPrimaryKey(o1.getActivityId()).getPeople() - activityMapper.selectByPrimaryKey(o2.getActivityId()).getPeople();
                                             }
                                         }
                        );
                        int j = 0;
                        while (j < 4 && activityMapper.selectByPrimaryKey(briefPushes.get(j).getActivityId()).getPeople() < ele.getPeople())
                            j++;
                        if (j != 0) briefPushes.set(j - 1, briefPush);
                    }
                }
            }
        }
        return briefPushes;
    }

    @Override
    public Push getPushById(Long pushId) {
        return pushMapper.selectByPrimaryKey(pushId);
    }

    @Override
    public String uploadPicture(Long pushId, MultipartFile multipartFile) throws CommonException{

        Push push = pushMapper.selectByPrimaryKey(pushId);
        String picture = push.getPicture();

        try{
            if(picture!=null){
                cosClient.deleteObject(COS_BUCKET_NAME,picture.substring(picture.indexOf("push")));
            }
        }catch (Exception e){
            throw new CommonException(CommonErrorCode.UPLOAD_FILE_FAIL);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());

        UploadResult uploadResult = null;
        String res = null;

        try {

            String name = multipartFile.getOriginalFilename();
            AssertUtil.notNull(name,CommonErrorCode.FILENAME_CAN_NOT_BE_NULL);
            String extension = name.substring(name.lastIndexOf("."));

            PutObjectRequest putObjectRequest = new PutObjectRequest(COS_BUCKET_NAME, "push"+pushId + extension, multipartFile.getInputStream(), objectMetadata);

            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            uploadResult = upload.waitForUploadResult();

            res =  cosClient.getObjectUrl(COS_BUCKET_NAME,"push"+pushId).toString()+extension;

            push.setPicture(res);

            pushMapper.updateByPrimaryKeySelective(push);

        } catch (Exception e){
            //e.printStackTrace();
            throw new CommonException(CommonErrorCode.UPLOAD_FILE_FAIL);
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        transferManager.shutdownNow(true);

        return res;
    }
}
