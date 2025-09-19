package com.liuliang.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuliang.map.FriendMap;
import com.liuliang.map.UserMap;
import com.liuliang.pojo.Friend;
import com.liuliang.pojo.User;
import com.liuliang.service.FriendService;
import com.liuliang.vo.FriendApplicationVO;
import com.liuliang.vo.FriendVO;
import com.liuliang.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FriendServiceImp implements FriendService {

    @Autowired
    private FriendMap friendMap;
    @Autowired
    private UserMap userMap;

    /**
     * 发送好友请求
     * @param username
     * @param friendId
     * @param note
     * @return
     */
    @Override
    @Transactional
    public boolean sendFriendRequest(String username, Long friendId, String note) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null || friendId == null) return false;
        if ((long) currentUser.id == friendId) return false;
        User target = userMap.findById(friendId);
        if (target == null) return false;
        if (friendMap.checkFriendStatus((long) currentUser.id, friendId) != null) return false;

        Friend friend = new Friend();
        friend.setUserId((long) currentUser.id);
        friend.setFriendId(friendId);
        friend.setStatus(0);
        friend.setMessage(note);
        return friendMap.addFriendRequest(friend) > 0;
    }

    /**
     * 处理好友请求
     * @param username
     * @param applicationId
     * @param accept
     * @return
     */
    @Override
    @Transactional
    public boolean handleFriendRequest(String username, Long applicationId, Boolean accept) {
        // 检查输入参数
        if (username == null || applicationId == null || accept == null) {
            System.out.println("handleFriendRequest: Missing required parameters");
            return false;
        }
        
        // 查找当前用户
        User currentUser = userMap.findByName(username);
        if (currentUser == null) {
            System.out.println("handleFriendRequest: User not found: " + username);
            return false;
        }
        
        // 查找好友请求
        Friend request = friendMap.findFriendRequestById(applicationId);
        if (request == null) {
            System.out.println("handleFriendRequest: Friend request not found: " + applicationId);
            return false;
        }
        
        // 检查friendId是否为null以及是否匹配当前用户
        if (request.getFriendId() == null) {
            System.out.println("handleFriendRequest: friendId is null for request: " + applicationId);
            return false;
        }
        
        if (!request.getFriendId().equals((long) currentUser.id)) {
            System.out.println("handleFriendRequest: Friend ID mismatch - request.friendId=" + request.getFriendId() + ", currentUser.id=" + currentUser.id);
            return false;
        }

        int status = Boolean.TRUE.equals(accept) ? 1 : 2;
        int updated = friendMap.updateFriendRequestStatus(applicationId, status);
        if (!Boolean.TRUE.equals(accept))
            return updated > 0;

        // ensure reverse relation is friend
        Friend reverse = friendMap.checkFriendStatus(request.getFriendId(), request.getUserId());
        if (reverse == null) {
            Friend r = new Friend();
            r.setUserId(request.getFriendId());
            r.setFriendId(request.getUserId());
            r.setStatus(1);
            friendMap.addFriendRequest(r);
        } else if (reverse.getStatus() != 1) {
            friendMap.updateFriendRequestStatus(reverse.getId(), 1);
        }
        return true;
    }

    /**
     * 获取好友列表
     * @param username
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<FriendVO> getFriendList(String username, int page, int size) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return new PageResult<>();
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 50);
        PageHelper.startPage(p, s);
        List<Friend> rows = friendMap.findFriendsByUserId((long) currentUser.id);
        PageInfo<Friend> info = new PageInfo<>(rows);
        List<FriendVO> list = new ArrayList<>();
        for (Friend f : rows) {
            User u = userMap.findById(f.getFriendId());
            if (u == null) continue;
            FriendVO vo = new FriendVO();
            vo.setId((long) u.id);
            vo.setUsername(u.username);
            vo.setAvatar(u.avatar);
            vo.setBio(u.bio);
            vo.setLastOnline(String.valueOf(f.getCreatedAt()));
            vo.setOnline(Boolean.TRUE);
            list.add(vo);
        }
        PageResult<FriendVO> pr = new PageResult<>();
        pr.setList(list);
        pr.setPage(p);
        pr.setSize(s);
        pr.setTotal((int) info.getTotal());
        return pr;
    }

    /**
     * 获取好友申请列表
     * @param username
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<FriendApplicationVO> getFriendApplications(String username, int page, int size) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return new PageResult<>();
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 50);
        PageHelper.startPage(p, s);
        List<Friend> rows = friendMap.findFriendRequestsByUserId((long) currentUser.id);
        PageInfo<Friend> info = new PageInfo<>(rows);
        List<FriendApplicationVO> list = new ArrayList<>();
        for (Friend f : rows) {
            User u = userMap.findById(f.getUserId());
            if (u == null) continue;
            FriendApplicationVO vo = new FriendApplicationVO();
            vo.setId(f.getId());
            vo.setUserId((long) u.id);
            vo.setUsername(u.username);
            vo.setAvatar(u.avatar);
            vo.setBio(u.bio);
            vo.setMessage(f.getMessage());
            vo.setCreatedAt(String.valueOf(f.getCreatedAt()));
            list.add(vo);
        }
        PageResult<FriendApplicationVO> pr = new PageResult<>();
        pr.setList(list);
        pr.setPage(p);
        pr.setSize(s);
        pr.setTotal((int) info.getTotal());
        return pr;
    }

    /**
     * 删除好友
     * @param username
     * @param friendId
     * @return
     */
    @Override
    public boolean deleteFriend(String username, Long friendId) {
        User currentUser = userMap.findByName(username);
        if (currentUser == null) return false;
        Friend existing = friendMap.checkFriendStatus((long) currentUser.id, friendId);
        if (existing == null || existing.getStatus() != 1) return false;
        return friendMap.deleteFriendship((long) currentUser.id, friendId) > 0;
    }

    /**
     * 检查好友状态
     * @param username
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> checkFriendStatus(String username, Long userId) {
        Map<String, Object> res = new HashMap<>();
        User currentUser = userMap.findByName(username);
        if (currentUser == null) { res.put("status", "none"); return res; }
        if ((long) currentUser.id == userId) { res.put("status", "self"); return res; }
        User target = userMap.findById(userId);
        if (target == null) { res.put("status", "none"); return res; }
        Friend f = friendMap.checkFriendStatus((long) currentUser.id, userId);
        if (f != null) {
            res.put("status", f.getStatus() == 1 ? "friend" : (f.getStatus() == 0 ? "applying" : "rejected"));
        } else {
            Friend rf = friendMap.checkFriendStatus(userId, (long) currentUser.id);
            if (rf != null && rf.getStatus() == 0) res.put("status", "applied"); else res.put("status", "none");
        }
        res.put("friendId", userId);
        return res;
    }
}


