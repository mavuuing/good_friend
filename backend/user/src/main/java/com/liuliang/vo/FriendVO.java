package com.liuliang.vo;

import lombok.Data;

@Data
public class FriendVO {
    private Long id;            // 好友用户ID
    private String username;
    private String avatar; // 头像
    private String bio;//个人简介
    private String lastOnline;  // 暂用关系创建时间
    private Boolean online;     // 先占位
}


