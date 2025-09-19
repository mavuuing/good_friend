package com.liuliang.controller;

import com.liuliang.pojo.Result;
import com.liuliang.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 好友申请
     * @param usernameHeader
     * @param usernameParam
     * @param body
     * @return
     */
    @PostMapping("/apply")
    public Result<?> apply(@RequestHeader(value = "username", required = false) String usernameHeader,
                           @RequestParam(value = "username", required = false) String usernameParam,
                           @RequestBody Map<String, Object> body) {
        // 优先使用请求头中的用户名
        String username = usernameHeader != null ? usernameHeader : usernameParam;
        if (username == null || username.trim().isEmpty())
            return Result.error("缺少用户名");

        Long friendId = Long.parseLong(body.get("friendId").toString());

        String message = body.get("message") == null ? null : body.get("message").toString();

        return friendService.sendFriendRequest(username, friendId, message)
                ? Result.success("好友申请发送成功") : Result.error("好友申请发送失败");
    }

    /**
     * 处理好友申请
     * @param usernameHeader
     * @param usernameParam
     * @param body
     * @return
     */
    @PostMapping("/handle")
    public Result<?> handle(@RequestHeader(value = "username", required = false) String usernameHeader,
                            @RequestParam(value = "username", required = false) String usernameParam,
                            @RequestBody Map<String, Object> body) {
        try {
            // 验证用户名
            String username = usernameHeader != null ? usernameHeader : usernameParam;
            if (username == null || username.trim().isEmpty()) {
                System.out.println("FriendController.handle: Missing username parameter");
                return Result.error("缺少用户名");
            }
            
            // 验证请求体
            if (body == null) {
                System.out.println("FriendController.handle: Empty request body for user: " + username);
                return Result.error("请求体不能为空");
            }
            
            // 验证并解析requestId
            if (!body.containsKey("requestId")) {
                System.out.println("FriendController.handle: Missing requestId parameter for user: " + username);
                return Result.error("缺少请求ID");
            }
            
            Long requestId;
            try {
                requestId = Long.parseLong(body.get("requestId").toString());
            } catch (NumberFormatException e) {
                System.out.println("FriendController.handle: Invalid requestId format: " + body.get("requestId") + " for user: " + username);
                return Result.error("请求ID格式错误");
            }
            
            // 验证并解析status
            if (!body.containsKey("status")) {
                System.out.println("FriendController.handle: Missing status parameter for user: " + username + ", requestId: " + requestId);
                return Result.error("缺少状态参数");
            }
            
            Integer status;
            try {
                status = Integer.parseInt(body.get("status").toString());
                if (status != 1 && status != 2) {
                    System.out.println("FriendController.handle: Invalid status value: " + status + " for user: " + username + ", requestId: " + requestId);
                    return Result.error("状态值必须是1(接受)或2(拒绝)");
                }
            } catch (NumberFormatException e) {
                System.out.println("FriendController.handle: Invalid status format: " + body.get("status") + " for user: " + username + ", requestId: " + requestId);
                return Result.error("状态参数格式错误");
            }
            
            // 调用服务处理好友请求
            System.out.println("FriendController.handle: Processing friend request - user: " + username + ", requestId: " + requestId + ", status: " + status);
            boolean ok = friendService.handleFriendRequest(username, requestId, status == 1);
            
            if (ok) {
                System.out.println("FriendController.handle: Friend request processed successfully - user: " + username + ", requestId: " + requestId + ", status: " + status);
                return Result.success("好友申请处理成功");
            } else {
                System.out.println("FriendController.handle: Failed to process friend request - user: " + username + ", requestId: " + requestId + ", status: " + status);
                return Result.error("好友申请处理失败，请检查请求ID是否存在且属于您");
            }
        } catch (Exception e) {
            System.out.println("FriendController.handle: Exception occurred - " + e.getMessage());
            e.printStackTrace();
            return Result.error("处理好友申请时发生错误: " + e.getMessage());
        }
    }

    /**
     * 获取好友列表
     * @param usernameHeader
     * @param usernameParam
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public Result<?> list(@RequestHeader(value = "username", required = false) String usernameHeader,
                          @RequestParam(value = "username", required = false) String usernameParam,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        String username = usernameHeader != null ? usernameHeader : usernameParam;
        if (username == null || username.trim().isEmpty()) return Result.error("缺少用户名");
        return Result.success(friendService.getFriendList(username, page, size));
    }

    /**
     * 好友申请列表
     * @param usernameHeader
     * @param usernameParam
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/applications")
    public Result<?> applications(@RequestHeader(value = "username", required = false) String usernameHeader,
                                  @RequestParam(value = "username", required = false) String usernameParam,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        String username = usernameHeader != null ? usernameHeader : usernameParam;
        if (username == null || username.trim().isEmpty()) return Result.error("缺少用户名");
        return Result.success(friendService.getFriendApplications(username, page, size));
    }

    /**
     * 删除好友
     * @param usernameHeader
     * @param usernameParam
     * @param friendId
     * @return
     */
    @DeleteMapping("/{friendId}")
    public Result<?> delete(@RequestHeader(value = "username", required = false) String usernameHeader,
                            @RequestParam(value = "username", required = false) String usernameParam,
                            @PathVariable Long friendId) {
        String username = usernameHeader != null ? usernameHeader : usernameParam;
        if (username == null || username.trim().isEmpty()) return Result.error("缺少用户名");
        return friendService.deleteFriend(username, friendId)
                ? Result.success("好友删除成功") : Result.error("好友删除失败");
    }

    /**
     * 查询好友状态
     * @param usernameHeader
     * @param usernameParam
     * @param friendId
     * @return
     */
    @GetMapping("/status/{friendId}")
    public Result<?> status(@RequestHeader(value = "username", required = false) String usernameHeader,
                            @RequestParam(value = "username", required = false) String usernameParam,
                            @PathVariable Long friendId) {
        String username = usernameHeader != null ? usernameHeader : usernameParam;
        if (username == null || username.trim().isEmpty()) return Result.error("缺少用户名");
        return Result.success(friendService.checkFriendStatus(username, friendId));
    }
}


