package com.liuliang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private Long id;
    private Long userId;
    private Long friendId;
    private Integer status; // 0-申请中，1-已成为好友，2-已拒绝
    private String message; // 附加消息
    private LocalDateTime createdAt;
}