package com.liuliang.controller;

import com.liuliang.pojo.Post;
import com.liuliang.pojo.Result;
import com.liuliang.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liuliang.vo.PageResult;
import java.util.List;
@Log4j2
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    /**
     * 创建帖子
     * @return
     */
    @PostMapping("/create")
    public Result<String> createPost(
        @RequestHeader(value = "username", required = false) String  usernameHeader,
          @RequestParam("content") String content,
          @RequestParam(value = "images", required = false) String images,
          @RequestParam(value = "username", required = false) String usernameParam

    ) {
        try {
            String username = usernameHeader != null ? usernameHeader : usernameParam;
            if (username == null || username.trim().isEmpty()) {
                return Result.error("缺少用户名：请在Header或参数中提供 username");
            }
            Long postId = postService.createPost(username, content, images);
            return Result.success("创建帖子成功，ID=" + postId);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("创建失败");
        }
    }

    
    /**
     * 获取动态详情
     */
    @GetMapping("/{postId}")
    public Result<Post> detail(@PathVariable Long postId) {
        try {
            return Result.success(postService.getById(postId));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取动态详情（Query参数版本） 示例：GET /post?postId=1
     */
    @GetMapping
    public Result<Post> detailByQuery(@RequestParam("postId") Long postId) {
        try {
            return Result.success(postService.getById(postId));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除帖子
     * @param postId
     * @return
     */
    @DeleteMapping()
    public Result<String> delete(Long postId,Long userid) {


        postService.deleteByIdAndUserId(postId, userid);
        return Result.success("删除成功");


    }

    /**
     * 广场
     * @param page
     * @param size
     * @param order
     * @return
     */
    @GetMapping("/feed")
    /*
     * - 查询参数:
     *   - `page` Integer 默认1
     *   - `size` Integer 默认10，最大50
     *   - `order` String 可选，默认`newest`，可选值：`newest`|`hot`（后续扩展）
     */
    public Result<PageResult<Post>> feed(
           @RequestParam(value = "page", defaultValue = "1") Integer page,
           @RequestParam(value = "size", defaultValue = "10") Integer size,
           @RequestParam (value = "order", defaultValue = "newest")String order)
    {
        try {
            List<Post> posts = postService.searchPost( page, size,order);
            int total = postService.countAll();
            PageResult<Post> data = new PageResult<>();
            data.setList(posts);
            data.setPage(page);
            data.setSize(size);
            data.setTotal(total);
            return Result.success(data);
        } catch (Exception e) {
            log.error("搜索用户失败", e);
            return Result.error("搜索失败");
        }
    }

/**
 * 用户个人动态
 *
 */
//- 方法/路径: `GET /post/user/{userId}`
//            - 路径参数: `userId` Long 必填
//- 查询参数: `page`、`size` 同上
//- 响应体结构同“全站Feed”
@GetMapping("/user/{userId}")
    public Result<PageResult<Post>> userFeed(
           @PathVariable Long userId,
           @RequestParam(value = "page", defaultValue = "1") Integer page,
           @RequestParam(value = "size", defaultValue = "10") Integer size,
           @RequestParam (value = "order", defaultValue = "newest")String order)
    {
        try {
            List<Post> posts = postService.searchUserPost( userId,page, size,order);
            //用处在是，这里返回的total是该用户动态的总数，而不是全站动态的总数
            int total = postService.countByUserId(userId);
            PageResult<Post> data = new PageResult<>();
            data.setList(posts);
            data.setPage(page);
            data.setSize(size);
            data.setTotal(total);
            return Result.success(data);
        }
        catch (Exception e) {
            log.error("搜索用户失败", e);
            return Result.error("搜索失败");
        }

    }
    /**
     * 点赞动态
     * @param postId
     * @return
     */
    @PostMapping("/{postId}/like")
    public Result<String> like(@PathVariable Long postId) {
        try {
            postService.like(postId);
            return Result.success("点赞成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("点赞失败", e);
            return Result.error("点赞失败");
        }
    }

    /**
     * 取消点赞
     * @param postId
     * @return
     */
    @PostMapping("/{postId}/unlike")
    public Result<String> unlike(@PathVariable Long postId) {
        try {
            postService.unlike(postId);
            return Result.success("取消点赞成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("取消点赞失败", e);
            return Result.error("取消点赞失败");
        }
    }



    /**
     * 搜索动态
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public Result<PageResult<Post>> search(
           @RequestParam("keyword") String keyword,
           @RequestParam(value = "page", defaultValue = "1") Integer page,
           @RequestParam(value = "size", defaultValue = "10") Integer size)
    {
        try {
            List<Post> posts = postService.searchPosts(keyword, page, size);
            int total = postService.countSearchPosts(keyword);
            PageResult<Post> data = new PageResult<>();
            data.setList(posts);
            data.setSize(size);
            data.setTotal(total);
            data.setPage(page);
            return Result.success(data);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("搜索动态失败", e);
            return Result.error("搜索失败");
        }
    }



}
