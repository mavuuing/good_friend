## 社交网站 - 好友模块 API 接口文档

- 模块名: 好友（Friend）
- 基础URL: `/friend`
- 返回结构: `Result<T>`，分页统一使用 `PageResult<T>`
- 鉴权: 过渡期通过请求头 `username` 标识当前用户（后续建议改 JWT）

### 相关表
- `friend`
  - `id` BIGINT PK AI
  - `user_id` BIGINT（发起方/所属方）
  - `friend_id` BIGINT（对方）
  - `status` TINYINT（0申请中 1已好友 2已拒绝）
  - `message` VARCHAR(255)（申请备注，可空）
  - `created_at` DATETIME 默认当前时间
  - 唯一键 (`user_id`, `friend_id`)

---

### 1) 发送好友申请
- 方法/路径: `POST /friend/apply`
- Header: `username` 当前用户
- Body(JSON):
  - `friendId` Long 必填
  - `message` String 可选
- 成功响应:
```json
{ "code": 1, "msg": "好友申请发送成功", "data": null }
```
- 失败场景: 自己加自己、目标不存在、重复申请/已是好友、唯一键冲突

### 2) 处理好友申请（接受/拒绝）
- 方法/路径: `POST /friend/handle`
- Header: `username` 被申请人（处理者）
- Body(JSON):
  - `requestId` Long 必填（申请记录ID）
  - `status` Integer 必填（1=接受，2=拒绝）
- 行为: 
  - 接受：当前记录置 `status=1`；反向关系存在则置 `1`，不存在则插入 `1`
  - 拒绝：当前记录置 `status=2`；不建立反向关系
- 成功响应:
```json
{ "code": 1, "msg": "好友申请处理成功", "data": null }
```

### 3) 获取好友列表（分页）
- 方法/路径: `GET /friend/list`
- Header: `username`
- Query:
  - `page` Integer 默认1
  - `size` Integer 默认10，最大50
- 响应(`PageResult<Map>`):
```json
{
  "code": 1,
  "data": {
    "list": [ { "id": 2, "username": "abc", "avatar": "...", "bio": "...", "lastOnline": "2025-09-15 12:00:00", "isOnline": true } ],
    "page": 1, "size": 10, "total": 3
  }
}
```

### 4) 获取收到的好友申请列表（分页）
- 方法/路径: `GET /friend/applications`
- Header: `username`
- Query: `page`、`size` 同上
- 响应(`PageResult<Map>`): 每项含申请 `id`、对方 `userId/username/avatar/bio`、`message`、`createdAt`

### 5) 删除好友
- 方法/路径: `DELETE /friend/{friendId}`
- Header: `username`
- 行为: 双向删除（`user_id=当前 AND friend_id=对方` 或 `user_id=对方 AND friend_id=当前`）
- 成功响应: `{ "code": 1, "msg": "好友删除成功" }

### 6) 查询好友关系状态
- 方法/路径: `GET /friend/status/{friendId}`
- Header: `username`
- 返回(`Map`): `{ "status": "self|friend|applying|applied|rejected|none", "friendId": 2 }`

---

### 错误码建议
- 3001 目标用户不存在
- 3002 不允许加自己为好友
- 3003 已存在申请或已是好友
- 3004 非法的申请状态
- 3005 申请不属于当前处理人

### 说明
- 分页使用 PageHelper；SQL 放在 MyBatis XML
- 接口返回统一 `Result<T>`；列表返回 `PageResult<T>`


