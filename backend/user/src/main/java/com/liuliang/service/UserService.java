package com.liuliang.service;

import com.liuliang.pojo.User;
import java.util.List;

public interface UserService {

    /**
     * 添加用户
     * @param username
     * @param password
     * @param email
     */
    void addUser(String username, String password, String email);

    /**
     * 更新用户资料
     * @param username
     * @param email
     * @param phone
     * @param avatar
     * @param gender
     * @param age
     * @param bio
     * @param interests
     */
    void updateUserProfile(String username, String email, String phone, String avatar, 
                          Integer gender, Integer age, String bio, String interests);

    /**
     * 修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    boolean changePassword(String username, String oldPassword, String newPassword);

    /**
     * 根据ID获取用户信息
     * @param userId
     * @return
     */
    User getUserById(Long userId);

    /**
     * 搜索用户
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    List<User> searchUsers(String keyword, int page, int size);

    /**
     * 获取搜索结果总数
     * @param keyword
     * @return
     */
    int countSearchUsers(String keyword);
}
