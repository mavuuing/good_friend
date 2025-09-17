package com.liuliang.service;

import com.liuliang.pojo.Post;

import java.util.List;

public interface PostService {
    Long createPost(String username, String content, String images);

    void deleteByIdAndUserId(Long postId, Long userid);

    Post getById(Long postId);

    java.util.List<Post> searchPost(Integer page, Integer size, String order);
    int countAll();

    List<Post> searchUserPost(Long userId,Integer page, Integer size, String order);
    int countByUserId(Long userId);

    void like(Long postId);
    void unlike(Long postId);
    List<Post> searchPosts(String keyword, Integer page, Integer size);
    int countSearchPosts(String keyword);
}


