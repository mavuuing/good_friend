package com.liuliang.controller;
import com.liuliang.map.UserMap;
import com.liuliang.pojo.Result;
import com.liuliang.pojo.User;
import com.liuliang.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService Userservice;
    @Autowired
    private UserMap userMap;

    /**
     *  注册
     * @param username
     * @param password
     * @param email
     */
    @PostMapping ("/register")
    public Result<String> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email){
        if(userMap.findByName(username)!=null){
            log.info("用户已存在");
            return Result.error("用户已存在");
        }else {
            Userservice.addUser(username, password, email);
            return Result.success("注册成功");
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password){
        if(userMap.findByName(username)==null){
            log.info("用户不存在");
            return Result.error("用户不存在");
        }else if(!userMap.findByName(username).getPassword().equals(password)){
            log.info("密码错误");
            return Result.error("密码错误");
        }
        return Result.success("登录成功");
    }

    /**
     * 获取当前用户资料的接口
     * @param username
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result<User> getUserInfo(@RequestHeader String username){
        return Result.success(userMap.findByName(username));
    }

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
     * @return
     */
    @PutMapping("/updateProfile")
    public Result<String> updateProfile(
            @RequestHeader String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "interests", required = false) String interests){
        try {
            Userservice.updateUserProfile(username, email, phone, avatar, gender, age, bio, interests);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新用户资料失败", e);
            return Result.error("更新失败");
        }
    }

    /**
     * 修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PutMapping("/changePassword")
    public Result<String> changePassword(
            @RequestHeader String username,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword){
        if (Userservice.changePassword(username, oldPassword, newPassword)) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("原密码错误或修改失败");
        }
    }

    /**
     * 搜索用户
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public Result<Map<String, Object>> searchUsers(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        try {
            List<User> users = Userservice.searchUsers(keyword, page, size);
            int total = Userservice.countSearchUsers(keyword);
            
            Map<String, Object> result = new HashMap<>();
            result.put("users", users);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("搜索用户失败", e);
            return Result.error("搜索失败");
        }
    }

    /**
     * 获取用户公开信息
     * @param userId
     * @return
     */
    @GetMapping("/profile/{userId}")
    public Result<User> getUserProfile(@PathVariable Long userId){
        User user = Userservice.getUserById(userId);
        if (user != null) {
            // 创建公开信息对象，不包含密码等敏感信息
            User publicUser = new User();
            publicUser.setId(user.getId());
            publicUser.setUsername(user.getUsername());
            publicUser.setEmail(user.getEmail());
            publicUser.setPhone(user.getPhone());
            publicUser.setAvatar(user.getAvatar());
            publicUser.setGender(user.getGender());
            publicUser.setAge(user.getAge());
            publicUser.setBio(user.getBio());
            publicUser.setInterests(user.getInterests());
            publicUser.setCreatedAt(user.getCreatedAt());
            // 不设置密码字段
            
            return Result.success(publicUser);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 用户登出
     * @param username
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader String username){
        // 这里可以添加登出逻辑，比如清除token等
        log.info("用户登出: {}", username);
        return Result.success("登出成功");
    }
}
