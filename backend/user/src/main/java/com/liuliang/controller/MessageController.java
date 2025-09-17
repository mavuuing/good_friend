package com.liuliang.controller;

import com.liuliang.pojo.Result;
import com.liuliang.service.MessageService;
import com.liuliang.vo.ConversationVO;
import com.liuliang.vo.MessageVO;
import com.liuliang.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/msg")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送私信
     */
    @PostMapping("/send")
    public Result<?> send(@RequestHeader("username") String username,
                          @RequestBody Map<String, Object> body) {
        try {
            Long toUserId = Long.parseLong(body.get("toUserId").toString());
            String content = body.get("content").toString();

            if (content == null || content.trim().isEmpty()) {
                return Result.error("4002,内容不能为空");
            }

            boolean success = messageService.sendMessage(username, toUserId, content);
            return success ? Result.success("发送成功") : Result.error("发送失败");
        } catch (Exception e) {
            return Result.error("发送失败");
        }
    }

    /**
     * 拉取与某人的会话消息（分页）
     */
    @GetMapping("/session/{userId}")
    public Result<PageResult<MessageVO>> getSessionMessages(
            @RequestHeader("username") String username,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResult<MessageVO> result = messageService.getSessionMessages(username, userId, page, size);
        return Result.success(result);
    }

    /**
     * 会话列表（最近联系人 + 最后一条消息 + 未读数）
     */
    @GetMapping("/conversations")
    public Result<List<ConversationVO>> getConversations(@RequestHeader("username") String username) {
        List<ConversationVO> conversations = messageService.getConversations(username);
        return Result.success(conversations);
    }

    /**
     * 标记会话消息为已读
     */
    @PostMapping("/read/{userId}")
    public Result<?> markAsRead(@RequestHeader("username") String username,
                                @PathVariable Long userId) {
        boolean success = messageService.markAsRead(username, userId);
        return success ? Result.success("已读完成") : Result.error("标记失败");
    }

    /**
     * 未读数
     */
    @GetMapping("/unread_count")
    public Result<Map<String, Object>> getUnreadCount(@RequestHeader("username") String username) {
        int count = messageService.getUnreadCount(username);
        Map<String, Object> data = new HashMap<>();
        data.put("total", count);
        return Result.success(data);
    }
}
