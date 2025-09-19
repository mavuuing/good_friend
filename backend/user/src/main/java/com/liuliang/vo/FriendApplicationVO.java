package com.liuliang.vo;

import lombok.Data;

@Data
public class FriendApplicationVO {
    private Long id;          // 申请记录ID
    private Long userId;      // 申请人ID
    private String username;  // 申请人用户名
    private String avatar;    // 申请人头像
    private String bio;       // 申请人简介
    private String message;   // 申请备注
    private String createdAt; // 申请时间
}


