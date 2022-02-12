package com.phoenix.read.service;

import com.phoenix.read.common.Page;
import com.phoenix.read.dto.SessionData;
import com.phoenix.read.entity.User;

public interface UserService {
    /**
     * 登录
     *
     * @param code
     * @return
     */
    SessionData login(String code);
}
