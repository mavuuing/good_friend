package com.liuliang.service.imp;

import com.liuliang.map.UserMap;
import com.liuliang.pojo.User;
import com.liuliang.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class  UserServiceImp implements UserService {
    @Autowired
    private UserMap userMap;

    /**
     * 添加用户
     * @param username
     * @param password
     * @param email
     */
    public void addUser(String username, String password, String email) {
            userMap.addUser(username,password,email);
            log.info("添加用户成功");
    }

    /**
     * 更新用户资料
     */
    @Override
    public void updateUserProfile(String username, String email, String phone, String avatar, 
                                 Integer gender, Integer age, String bio, String interests) {
        userMap.updateUserProfile(username, email, phone, avatar, gender, age, bio, interests);
        log.info("更新用户资料成功: {}", username);
    }

    /**
     * 修改密码
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        int result = userMap.changePassword(username, oldPassword, newPassword);
        if (result > 0) {
            log.info("修改密码成功: {}", username);
            return true;
        } else {
            log.warn("修改密码失败: {}", username);
            return false;
        }
    }

    /**
     * 根据ID获取用户信息
     */
    @Override
    public User getUserById(Long userId) {
        return userMap.findById(userId);
    }

    /**
     * 搜索用户
     */
    @Override
    public List<User> searchUsers(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return userMap.searchUsers(keyword, offset, size);
    }

    /**
     * 获取搜索结果总数
     */
    @Override
    public int countSearchUsers(String keyword) {
        return userMap.countSearchUsers(keyword);
    }
}
