package com.liuliang.service;

import com.liuliang.vo.ConversationVO;
import com.liuliang.vo.MessageVO;
import com.liuliang.vo.PageResult;

import java.util.List;

public interface MessageService {
    boolean sendMessage(String fromUsername, Long toUserId, String content);
    PageResult<MessageVO> getSessionMessages(String username, Long userId, int page, int size);
    List<ConversationVO> getConversations(String username);
    boolean markAsRead(String username, Long userId);
    int getUnreadCount(String username);
}
