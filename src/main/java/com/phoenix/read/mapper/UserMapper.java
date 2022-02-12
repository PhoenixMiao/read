package com.phoenix.read.mapper;

import com.phoenix.read.MyMapper;
import com.phoenix.read.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author yannis
 * @version 2020/11/7 9:17
 */

@Repository
public interface UserMapper extends MyMapper<User> {

}
