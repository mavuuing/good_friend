## 社交网站 - 动态模块 API 接口文档

- 模块名: 动态（Post）
- 基础URL: `/post`
- 统一返回: 复用用户模块 `Result<T>` 结构：`{ code: 1|0, msg: string|null, data: any|null }`
- 鉴权方案: 现阶段通过请求头 `username` 识别用户（后续建议改为 JWT）

### 数据表对齐
- 表: `post`
  - `id` BIGINT PK AI
  - `user_id` BIGINT NOT NULL（发帖人，外键到 `user.id`）
  - `content` TEXT NOT NULL（动态内容）
  - `images` JSON（图片URL数组，如 `["/images/1.jpg"]`）
  - `likes` INT DEFAULT 0（点赞计数）
  - `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP

说明：当前仅有点赞计数字段，并无“谁点过赞”的明细表。因此“点赞/取消点赞”接口仅对计数进行加减，无法保证幂等与去重；扩展建议见文末。

---

### 1) 发布动态
- 方法/路径: `POST /post/create`
- 说明: 创建一条动态
- 请求头:
  - `username` String 必填
- 请求参数（表单或 JSON 均可）:
  - `content` String 必填，动态内容
  - `images` String 可选，JSON 数组字符串，如 `"[\"/images/1.jpg\", \"/images/2.jpg\"]"`
- 成功响应示例:
```json
{
  "code": 1,
  "msg": "发布成功",
  "data": { "postId": 123 }
}
```
- 失败码/文案:
  - `0` -> 参数错误/发布失败
  - `2003` -> 内容为空

### 2) 删除动态
- 方法/路径: `DELETE /post/{postId}`
- 说明: 仅作者可删除
- 请求头: `username` 必填
- 路径参数: `postId` Long 必填
- 成功响应:
```json
{ "code": 1, "msg": "删除成功", "data": null }
```
- 失败码/文案:
  - `2001` -> 动态不存在
  - `2002` -> 无权限（非作者）

### 3) 获取动态详情
- 方法/路径: `GET /post/{postId}`
- 路径参数: `postId` Long 必填
- 成功响应示例:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 123,
    "userId": 1,
    "content": "今天去了海边！",
    "images": ["/images/beach1.jpg"],
    "likes": 10,
    "createdAt": "2024-01-01 12:00:00"
  }
}
```
- 失败码: `2001` 动态不存在

### 4) 全站Feed（时间线）
- 方法/路径: `GET /post/feed`
- 查询参数:
  - `page` Integer 默认1
  - `size` Integer 默认10，最大50
  - `order` String 可选，默认`newest`，可选值：`newest`|`hot`（后续扩展）
- 响应示例:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "list": [
      {
        "id": 123,
        "userId": 1,
        "content": "今天去了海边！",
        "images": ["/images/beach1.jpg"],
        "likes": 10,
        "createdAt": "2024-01-01 12:00:00"
      }
    ],
    "page": 1,
    "size": 10,
    "total": 100
  }
}
```

### 5) 用户个人Feed
- 方法/路径: `GET /post/user/{userId}`
- 路径参数: `userId` Long 必填
- 查询参数: `page`、`size` 同上
- 响应体结构同“全站Feed”

### 6) 点赞动态（非幂等，仅计数）
- 方法/路径: `POST /post/{postId}/like`
- 请求头: `username` 必填
- 说明: `likes = likes + 1`
- 成功响应:
```json
{ "code": 1, "msg": "点赞成功", "data": { "likes": 11 } }
```
- 失败码: `2001` 动态不存在

### 7) 取消点赞（非幂等，仅计数）
- 方法/路径: `POST /post/{postId}/unlike`
- 请求头: `username` 必填
- 说明: `likes = max(likes - 1, 0)`
- 成功响应:
```json
{ "code": 1, "msg": "已取消点赞", "data": { "likes": 10 } }
```
- 失败码: `2001` 动态不存在

### 8) 搜索动态
- 方法/路径: `GET /post/search`
- 查询参数:
  - `keyword` String 必填，在 `content` 中模糊匹配
  - `page` Integer 默认1
  - `size` Integer 默认10
- 响应体结构同“全站Feed”

---

### 错误码（动态模块）
- `2001`: 动态不存在
- `2002`: 无权限（尝试删除他人动态）
- `2003`: 内容为空
- `2004`: 点赞状态不可判定（当前无明细表，仅计数）

---

### 安全与性能建议
- 鉴权：尽快引入 JWT，`username` 仅用于过渡期开发联调
- 点赞去重：新增表 `post_like(post_id, user_id, created_at)`，并在点赞时做幂等判重
- 图片上传：提供 `/post/upload`（`multipart/form-data`）返回URL，`create` 时传回URL数组
- 内容过滤：接入敏感词与XSS过滤，限制内容长度（如 1~2000 字）
- 分页与索引：为 `post(user_id, created_at)` 建复合索引；搜索可引入全文索引或ES
- 限流：发布、点赞接口增加限流（如IP与用户双维度）

---

### 字段校验建议
- `content`: 必填，非空白
- `images`: 可选，需验证为合法的JSON数组且元素为URL
- `page/size`: 合理范围校验，`size` 建议上限50

---

文档版本: v1.0  
最后更新: 2025-09-15  
维护: 开发团队


