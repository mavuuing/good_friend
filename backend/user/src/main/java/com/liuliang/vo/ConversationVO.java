package com.liuliang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationVO {
    private Long userId;
    private String username;
    private String avatar;
    private String lastMsg;
    private Date lastTime;

    private Integer unread;

    }
