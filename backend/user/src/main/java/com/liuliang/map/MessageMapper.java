package com.liuliang.map;

import com.liuliang.pojo.Message;
import com.liuliang.vo.ConversationVO;
import com.liuliang.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    int insert(Message message);

    List<MessageVO> findMessagesBetweenUsers(@Param("userId1") Long userId1,
                                             @Param("userId2") Long userId2,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    int countMessagesBetweenUsers(@Param("userId1") Long userId1,
                                  @Param("userId2") Long userId2);

    List<ConversationVO> findConversationsByUserId(@Param("userId") Long userId);

    int markAsRead(@Param("userId") Long userId,
                   @Param("otherUserId") Long otherUserId);

    int countUnreadMessages(@Param("userId") Long userId);
}
