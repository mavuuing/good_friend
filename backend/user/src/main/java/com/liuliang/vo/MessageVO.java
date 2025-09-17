package com.liuliang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private Boolean isRead;
    private Date createdAt;
}
