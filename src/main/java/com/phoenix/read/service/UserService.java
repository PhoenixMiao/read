package com.phoenix.read.service;

import com.phoenix.read.common.Page;
import com.phoenix.read.controller.request.UpdateUserRequest;
import com.phoenix.read.controller.response.UserResponse;
import com.phoenix.read.dto.BriefUser;
import com.phoenix.read.dto.SessionData;
import com.phoenix.read.entity.User;
import com.qcloud.cos.model.UploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    /**
     * 登录
     *
     * @param code
     * @return
     */
    SessionData login(String code);

    Page<BriefUser> getBriefUserList(int pageSize, int pageNum,Long userId);

    Long inputStuIdAndName(Long userId,String studentId,String name);

    void toAdmin(Long userId,Long adminId);

    void backToUser(Long userId,Long adminId);

    User getUserById(Long userId);

    void UpdateUser(Long userId, UpdateUserRequest updateUserRequest);

    void classifyUser(Long organizerId,Long userId,Long adminId);

    void mute(Long userId,Long adminId);

    String uploadPortrait(Long userId, MultipartFile multipartFile);

    UserResponse getUser(Long userId);
}
