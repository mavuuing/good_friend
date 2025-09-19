package com.liuliang.map;

import com.liuliang.pojo.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMap {

    @Insert("insert into friend(user_id, friend_id, status, message) values(#{userId}, #{friendId}, #{status}, #{message})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addFriendRequest(Friend friend);

    @Update("update friend set status = #{status} where id = #{id}")
    int updateFriendRequestStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("select * from friend where id = #{id}")
    Friend findFriendRequestById(Long id);

    @Select("select * from friend where user_id = #{userId} and friend_id = #{friendId}")
    Friend checkFriendStatus(@Param("userId") Long userId, @Param("friendId") Long friendId);

    List<Friend> findFriendsByUserId(@Param("userId") Long userId);

    @Select("select count(*) from friend where user_id = #{userId} and status = 1")
    int countFriendsByUserId(Long userId);

    List<Friend> findFriendRequestsByUserId(@Param("userId") Long userId);

    @Select("select count(*) from friend where friend_id = #{userId} and status = 0")
    int countFriendRequestsByUserId(Long userId);

    int deleteFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);
}


