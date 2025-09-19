package com.liuliang.service;

import com.liuliang.vo.FriendApplicationVO;
import com.liuliang.vo.FriendVO;
import com.liuliang.vo.PageResult;

import java.util.Map;

public interface FriendService {

    boolean sendFriendRequest(String username, Long friendId, String note);

    boolean handleFriendRequest(String username, Long applicationId, Boolean accept);

    PageResult<FriendVO> getFriendList(String username, int page, int size);

    PageResult<FriendApplicationVO> getFriendApplications(String username, int page, int size);

    boolean deleteFriend(String username, Long friendId);

    Map<String, Object> checkFriendStatus(String username, Long userId);
}


