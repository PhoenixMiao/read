package com.phoenix.read.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phoenix.read.common.*;
import com.phoenix.read.config.YmlConfig;
import com.phoenix.read.controller.request.UpdateUserRequest;
import com.phoenix.read.dto.BriefUser;
import com.phoenix.read.dto.SessionData;
import com.phoenix.read.dto.WxSession;
import com.phoenix.read.entity.User;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.service.UserService;
import com.phoenix.read.util.*;
import com.phoenix.read.config.YmlConfig;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.util.SessionUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import javafx.scene.input.TransferMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.*;

import static com.phoenix.read.common.CommonConstants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private YmlConfig ymlConfig;

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private TransferManager transferManager;

    @Autowired
    private COSClient cosClient;


    @Override
    public SessionData login(String code) {

        //shadow test
        if(CommonConstants.SHADOW_TEST.equals(code)){
            sessionUtils.setSessionId(CommonConstants.SHADOW_TEST);
            return new SessionData();
        }

        WxSession wxSession = Optional.ofNullable(
                getWxSessionByCode(code))
                .orElse(new WxSession());

        checkWxSession(wxSession);

        String sessionId = sessionUtils.generateSessionId();

        User user = User.builder()
                .openId(wxSession.getOpenId())
                .build();
        user = userMapper.selectOne(user);

        if(user != null){
            sessionUtils.setSessionId(user.getSessionId());
            return new SessionData(user);
        }


        user = User.builder()
                .createTime(TimeUtil.getCurrentTimestamp())
                .openId(wxSession.getOpenId())
                .unionId(wxSession.getUnionId())
                .sessionKey(wxSession.getSessionKey())
                .sessionId(sessionId)
                .type(0)
                .isMute(0)
                .nickname("花狮用户")
                .build();
        userMapper.insert(user);

        return new SessionData(user);
    }

    @Override
    public Page<BriefUser> getBriefUserList(int pageSize, int pageNum,Long userId) {
        if(userMapper.selectByPrimaryKey(userId).getType()!=2) throw new CommonException(CommonErrorCode.USER_NOT_SUPERADMIN);
        PageHelper.startPage(pageNum,pageSize,"id asc");
        return new Page<>(new PageInfo<>(userMapper.getBriefUser()));
    }

    @Override
    public void toAdmin(Long userId,Long adminId) {
        if(userMapper.selectByPrimaryKey(adminId).getType()!=2) throw new CommonException(CommonErrorCode.USER_NOT_SUPERADMIN);
        if(userMapper.selectByPrimaryKey(userId).getType()!=0) throw new CommonException(CommonErrorCode.USER_IS_ADMIN);
        userMapper.toAdmin(1,userId);
    }

    @Override
    public void backToUser(Long userId, Long adminId) {
        if(userMapper.selectByPrimaryKey(adminId).getType()!=2) throw new CommonException(CommonErrorCode.USER_NOT_SUPERADMIN);
        if(userMapper.selectByPrimaryKey(userId).getType()!=1) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        userMapper.toAdmin(0,userId);
        userMapper.classifyUser(null,userId);
    }

    @Override
    public Long inputStuIdAndName(Long userId,String studentId,String name) throws CommonException{
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getName()!=null && user.getStudentId() != null){
            throw new CommonException(CommonErrorCode.HAVE_WRITTEN_STUID_AND_NAME);
        }
        userMapper.updateByPrimaryKey(User.builder()
                .id(userId)
                .name(name)
                .studentId(studentId)
                .build());
        user = userMapper.selectByPrimaryKey(userId);
        redisUtil.del(user.getSessionId());
        redisUtil.set(user.getSessionId(),new SessionData(user));
        return userId;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void UpdateUser(Long userId, UpdateUserRequest updateUserRequest) {
        userMapper.updateUser(updateUserRequest.getNickname(),updateUserRequest.getDepartment(),updateUserRequest.getMajor(),updateUserRequest.getGrade(),updateUserRequest.getTelephone(),updateUserRequest.getQQ(),updateUserRequest.getWechatNum(),updateUserRequest.getPortrait(),userId);
    }

    @Override
    public void classifyUser(Long organizerId, Long userId, Long adminId) {
        if(userMapper.selectByPrimaryKey(adminId).getType()!=2) throw new CommonException(CommonErrorCode.USER_NOT_SUPERADMIN);
        if(userMapper.selectByPrimaryKey(userId).getType()!=1) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        userMapper.classifyUser(organizerId,userId);
    }


    private WxSession getWxSessionByCode(String code){
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", ymlConfig.getAppId());
//        requestUrlParam.put("appid", "wx22fa1182d4e66c4a");
        //小程序secret
        requestUrlParam.put("secret", ymlConfig.getAppSecret());
//        requestUrlParam.put("secret", "200e82982f7ec2a2812fc3ae9f2d5f15");
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数：授权类型
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(CommonConstants.WX_SESSION_REQUEST_URL, requestUrlParam);
//        String result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", requestUrlParam);

        return JsonUtil.toObject(result, WxSession.class);
    }

    private void checkWxSession(WxSession wxSession){
        if(wxSession.getErrcode() != null) {
            AssertUtil.isFalse(-1 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_BUSY, wxSession.getErrmsg());
            AssertUtil.isFalse(40029 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_INVALID_CODE, wxSession.getErrmsg());
            AssertUtil.isFalse(45011 == wxSession.getErrcode(), CommonErrorCode.WX_LOGIN_FREQUENCY_REFUSED, wxSession.getErrmsg());
            AssertUtil.isTrue(wxSession.getErrcode() == 0, CommonErrorCode.WX_LOGIN_UNKNOWN_ERROR,wxSession.getErrmsg());
        }
    }


    @Override
    public void mute(Long userId,Long adminId){
        if(userMapper.selectByPrimaryKey(adminId).getType()<1) throw new CommonException(CommonErrorCode.USER_NOT_ADMIN);
        if(userMapper.selectByPrimaryKey(userId)==null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        userMapper.updateByPrimaryKeySelective(User.builder().id(userId).isMute(1).build());
        new MemberThead(TimeUtil.getCurrentTimestamp(),2,userId).updateStatus();
    }

    @Override
    public String uploadPortrait(Long userId, MultipartFile multipartFile) throws CommonException{

        User user = userMapper.selectByPrimaryKey(userId);
        String portrait = user.getPortrait();

        try{
            if(user.getPortrait()!=null){
                cosClient.deleteObject(COS_BUCKET_NAME,portrait.substring(portrait.indexOf(user.getSessionId())));
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

            PutObjectRequest putObjectRequest = new PutObjectRequest(COS_BUCKET_NAME, user.getSessionId() + extension, multipartFile.getInputStream(), objectMetadata);

            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            uploadResult = upload.waitForUploadResult();

            res =  cosClient.getObjectUrl(COS_BUCKET_NAME,user.getSessionId()).toString()+extension;

            user.setPortrait(res);

            userMapper.updateByPrimaryKeySelective(user);

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