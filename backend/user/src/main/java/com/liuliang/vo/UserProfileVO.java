package com.liuliang.vo;


import lombok.Data;

import java.util.List;

// 用户信息
@Data
public class UserProfileVO {
        private Long id;
        private String username;
        private String email;
        private String avatar;
        private Integer gender;
        private Integer age;
        private String bio;
        private List<String> interests; // JSON数组转换为List
    }

