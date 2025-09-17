package com.liuliang.map;

import com.liuliang.pojo.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface PostMap {

    @Insert("insert into post(user_id, content, images) values(#{userId}, #{content}, #{images})")
    @Options(useGeneratedKeys = true, keyProperty = "id")// 设置主键回填 什么作用 ?
    int insert(Post post);

    @Select("select * from post where id=#{id}")
    Post findById(Long id);

    @Delete("delete from post where id=#{id} and user_id=#{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Update("update post set likes = likes + 1 where id=#{postId}")
    int like(Long postId);

    @Update("update post set likes = case when likes>0 then likes-1 else 0 end where id=#{postId}")
    int unlike(Long postId);
//offset 翻译为偏移量
    List<Post> findFeedWithoutLimit(@Param("order") String order);

    int countAll();

    List<Post> findByUserIdWithoutLimit(@Param("userId") Long userId, @Param("order") String order);

    int countByUserId(@Param("userId") Long userId);
    List<Post> searchPostsWithoutLimit(@Param("keyword") String keyword);
    int countSearchPosts(@Param("keyword") String keyword);
}


