package com.liuliang.service.imp;

import com.liuliang.map.MessageMapper;
import com.liuliang.map.UserMap;
import com.liuliang.pojo.Message;
import com.liuliang.pojo.User;
import com.liuliang.service.MessageService;
import com.liuliang.vo.ConversationVO;
import com.liuliang.vo.MessageVO;
import com.liuliang.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMap userMap;

    /**
     * 发送信息
     * @param fromUsername
     * @param toUserId
     * @param content
     * @return
     */
    @Override
    public boolean sendMessage(String fromUsername, Long toUserId, String content) {
        User sender = userMap.findByName(fromUsername);
        User receiver = userMap.findById(toUserId);

        if (sender == null || receiver == null || content == null || content.trim().isEmpty()) {
            return false;
        }

        Message msg = new Message();
        msg.setSenderId((long) sender.id);
        msg.setReceiverId(toUserId);
        msg.setContent(content);
        msg.setIsRead(false);
        //获取当前时间

        msg.setCreatedAt(new Date());

        return messageMapper.insert(msg) > 0;
    }

    /**
     * 获取会话消息
     * @param username
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<MessageVO> getSessionMessages(String username, Long userId, int page, int size) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return new PageResult<>();

        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 50);
        int offset = (p - 1) * s;

        List<MessageVO> messages = messageMapper.findMessagesBetweenUsers(
                (long) currentUser.id, userId, offset, s);
        int total = messageMapper.countMessagesBetweenUsers((long) currentUser.id, userId);

        PageResult<MessageVO> result = new PageResult<>();
        result.setList(messages);
        result.setPage(p);
        result.setSize(s);
        result.setTotal(total);
        return result;
    }

    @Override
    public List<ConversationVO> getConversations(String username) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return List.of();
        return messageMapper.findConversationsByUserId((long) currentUser.id);
    }

    /**
     * 标记为已读
     * @param username
     * @param userId
     * @return
     */
    @Override
    public boolean markAsRead(String username, Long userId) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return false;
        return messageMapper.markAsRead((long) currentUser.id, userId) >= 0;
    }

    /**
     *
     * 获取未读消息数
     * @param username
     * @return
     */
    @Override
    public int getUnreadCount(String username) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return 0;
        return messageMapper.countUnreadMessages((long) currentUser.id);
    }
}
