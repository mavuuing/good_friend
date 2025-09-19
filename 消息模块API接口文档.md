## 社交网站 - 消息模块 API 接口文档

- 模块名: 消息（Message）
- 基础URL: `/msg`
- 返回结构: `Result<T>`；分页用 `PageResult<T>`
- 鉴权: 过渡期通过 `username`，后续建议 JWT

### 相关表
- `message`
  - `id` BIGINT PK AI
  - `sender_id` BIGINT
  - `receiver_id` BIGINT
  - `content` TEXT
  - `is_read` BOOLEAN DEFAULT FALSE
  - `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
- `message_limit`（非好友限流，后续可选）

---

### 1) 发送私信
- 方法/路径: `POST /msg/send`
- Header: `username` 发送方
- Body(JSON):
  - `toUserId` Long 必填
  - `content` String 必填
- 限制: 可选地对非好友进行限流/限制（视产品策略）
- 成功响应: `{ "code": 1, "msg": "发送成功" }

### 2) 拉取与某人的会话消息（分页）
- 方法/路径: `GET /msg/session/{userId}`
- Header: `username` 当前用户
- Query: `page`、`size`
- 返回: `PageResult<MessageVO>`（按时间倒序）
  - `MessageVO` 字段: `id, fromUserId, toUserId, content, isRead, createdAt`

### 3) 会话列表（最近联系人 + 最后一条消息 + 未读数）
- 方法/路径: `GET /msg/conversations`
- Header: `username`
- 返回: `List<ConversationVO>`
  - `ConversationVO` 字段: `userId, username, avatar, lastMsg, lastTime, unread`

### 4) 标记会话消息为已读
- 方法/路径: `POST /msg/read/{userId}`
- Header: `username`
- 行为: 将我与 `userId` 的未读消息标记为已读
- 成功响应: `{ "code": 1, "msg": "已读完成" }

### 5) 未读数
- 方法/路径: `GET /msg/unread_count`
- Header: `username`
- 返回: `{ total: 3 }`

---

### 错误码建议
- 4001 目标用户不存在
- 4002 内容为空
- 4003 非好友受限（如开启限制）

### 说明
- 分页使用 PageHelper；SQL 使用 MyBatis XML
- 大文本/图片等，可扩展 `message_attachment` 表


