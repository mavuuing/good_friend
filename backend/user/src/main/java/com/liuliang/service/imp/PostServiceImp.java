package com.liuliang.service.imp;

import com.liuliang.map.PostMap;
import com.liuliang.map.UserMap;
import com.liuliang.pojo.Post;
import com.liuliang.pojo.User;
import com.liuliang.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

@Slf4j
@Service
public class PostServiceImp implements PostService {

    @Autowired
    private PostMap postMap;
    @Autowired
    private UserMap userMap;

    /**
     * 发贴子
     * @param username
     * @param content
     * @param images
     * @return
     */
    public Long createPost(String username, String content, String images) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("内容不能为空");
        }
        User user = userMap.findByName(username);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        Post post = new Post();
        post.setUserId((long) user.getId());
        post.setContent(content);
        post.setImages(images);
        post.setCreatedAt(java.time.LocalDateTime.now().toString());
        post.setLikes(0);
        postMap.insert(post);
        log.info("用户{}创建动态成功, postId={}", username, post.getId());
        return post.getId();
    }

    /**
     * 删帖子
     * @param postId
     * @param userid
     */
    public void deleteByIdAndUserId(Long postId, Long userid) {
        int rows = postMap.deleteByIdAndUserId(postId, userid);
        if (rows == 0) {
            throw new IllegalArgumentException("删除失败：不存在或无权限");
        }
        log.info("用户{}删除动态成功, postId={}", userid, postId);
    }

    @Override
    public Post getById(Long postId) {
        Post post = postMap.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("动态不存在");
        }
        return post;
    }

    /**
     * 获取动态列表
     * @param page
     * @param size
     * @param order
     * @return
     */
    public List<Post> searchPost(Integer page, Integer size, String order) {
        int pageSafe = (page == null || page < 1) ? 1 : page;
        int sizeSafe = (size == null || size < 1) ? 10 : Math.min(size, 50);
        PageHelper.startPage(pageSafe, sizeSafe);
        // 这里不需要 limit，交给 PageHelper
        List<Post> list = postMap.findFeedWithoutLimit(order);
        return new PageInfo<>(list).getList();
    }

    @Override
    public int countAll() {
        // PageHelper 已能提供总数，但为了兼容现有返回，仍保留 countAll
        return postMap.countAll();
    }

    /**
     * 获取个人动态列表
     * @param page
     * @param size
     * @param order
     * @return
     */
    @Override
    public List<Post> searchUserPost(Long userId ,Integer page, Integer size, String order) {
        // 可选的用户存在性校验
        User user = userMap.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        int pageSafe = (page == null || page < 1) ? 1 : page;
        int sizeSafe = (size == null || size < 1) ? 10 : Math.min(size, 50);
        PageHelper.startPage(pageSafe, sizeSafe);
        List<Post> list = postMap.findByUserIdWithoutLimit(userId, order);
        return new PageInfo<>(list).getList();
    }

    @Override
    public int countByUserId(Long userId) {
        return postMap.countByUserId(userId);
    }

    /**
     * 点赞
     * @param postId
     */
    @Override
    public void like(Long postId) {
        Post post = postMap.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("动态不存在");
        }
        // 直接点赞数+1，不判断是否已点赞（当前设计是计数模式）
        int rows = postMap.like(postId);
        if (rows == 0) {
            throw new IllegalArgumentException("点赞失败");
        }
        log.info("动态{}点赞成功", postId);
    }

    /**
     * 取消点赞
     * @param postId
     */
    @Override
    public void unlike(Long postId) {
        Post post = postMap.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("动态不存在");
        }
        // 点赞数-1，但不能小于0
        int rows = postMap.unlike(postId);
        if (rows == 0) {
            throw new IllegalArgumentException("取消点赞失败");
        }
        log.info("动态{}取消点赞成功", postId);
    }

    /**
     * 查询动态
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<Post> searchPosts(String keyword, Integer page, Integer size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("搜索关键词不能为空");
        }
        int pageSafe = (page == null || page < 1) ? 1 : page;
        int sizeSafe = (size == null || size < 1) ? 10 : Math.min(size, 50);
        PageHelper.startPage(pageSafe, sizeSafe);
        List<Post> list = postMap.searchPostsWithoutLimit(keyword);
        return new PageInfo<>(list).getList();
    }

    @Override
    public int countSearchPosts(String keyword) {
        return postMap.countSearchPosts(keyword);
    }
}


