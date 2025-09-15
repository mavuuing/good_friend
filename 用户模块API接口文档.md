# 社交网站 - 用户模块 API 接口文档

## 项目概述
- **项目名称**: Good Friend 社交网站
- **模块**: 用户模块
- **技术栈**: Spring Boot + MyBatis + MySQL
- **基础URL**: `http://localhost:8080/user`

## 数据库设计
基于现有的数据库表结构，用户模块涉及以下表：
- `user` - 用户基础信息表
- `friend` - 好友关系表
- `message` - 私信表
- `message_limit` - 非好友私信限制表

## 统一响应格式

### Result 响应结构
```json
{
    "code": 1,        // 状态码：1-成功，0-失败
    "msg": "操作成功",  // 提示信息
    "data": {}        // 返回数据
}
```

## 用户模块接口

### 1. 用户注册
- **接口地址**: `POST /user/register`
- **功能描述**: 用户注册新账号
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名，唯一 |
  | password | String | 是 | 密码 |
  | email | String | 是 | 邮箱，唯一 |

- **请求示例**:
```json
{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com"
}
```

- **响应示例**:
```json
// 成功
{
    "code": 1,
    "msg": "注册成功",
    "data": null
}

// 失败
{
    "code": 0,
    "msg": "用户已存在",
    "data": null
}
```

### 2. 用户登录
- **接口地址**: `POST /user/login`
- **功能描述**: 用户登录验证
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名 |
  | password | String | 是 | 密码 |

- **请求示例**:
```json
{
    "username": "testuser",
    "password": "123456"
}
```

- **响应示例**:
```json
// 成功
{
    "code": 1,
    "msg": "登录成功",
    "data": null
}

// 失败
{
    "code": 0,
    "msg": "用户不存在",
    "data": null
}
```

### 3. 获取用户信息
- **接口地址**: `GET /user/getUserInfo`
- **功能描述**: 获取当前用户详细信息
- **请求头**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名 |

- **响应示例**:
```json
{
    "code": 1,
    "msg": null,
    "data": {
        "id": 1,
        "username": "testuser",
        "password": "123456",
        "email": "test@example.com",
        "phone": null,
        "avatar": null,
        "gender": null,
        "age": 0,
        "bio": null,
        "interests": null,
        "createdAt": "2024-01-01 12:00:00"
    }
}
```

### 4. 更新用户信息
- **接口地址**: `PUT /user/updateProfile`
- **功能描述**: 更新用户个人资料
- **请求头**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名（用于身份验证） |
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | email | String | 否 | 邮箱 |
  | phone | String | 否 | 手机号 |
  | avatar | String | 否 | 头像URL |
  | gender | Integer | 否 | 性别：0-未知，1-男，2-女 |
  | age | Integer | 否 | 年龄 |
  | bio | String | 否 | 个人简介 |
  | interests | String | 否 | 兴趣爱好（JSON字符串） |

- **请求示例**:
```
PUT /user/updateProfile
Headers: username: testuser
Body: email=test@example.com&phone=13800138000&gender=1&age=25&bio=喜欢旅行
```

- **响应示例**:
```json
{
    "code": 1,
    "msg": "更新成功",
    "data": null
}
```

### 5. 修改密码
- **接口地址**: `PUT /user/changePassword`
- **功能描述**: 修改用户密码
- **请求头**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名 |
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | oldPassword | String | 是 | 原密码 |
  | newPassword | String | 是 | 新密码 |

- **请求示例**:
```
PUT /user/changePassword
Headers: username: testuser
Body: oldPassword=123456&newPassword=654321
```

- **响应示例**:
```json
{
    "code": 1,
    "msg": "密码修改成功",
    "data": null
}
```

### 6. 搜索用户
- **接口地址**: `GET /user/search`
- **功能描述**: 根据用户名搜索用户
- **请求参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | keyword | String | 是 | 搜索关键词 |
  | page | Integer | 否 | 页码，默认1 |
  | size | Integer | 否 | 每页数量，默认10 |

- **请求示例**:
```
GET /user/search?keyword=test&page=1&size=10
```

- **响应示例**:
```json
{
    "code": 1,
    "msg": null,
    "data": {
        "users": [
            {
                "id": 1,
                "username": "testuser",
                "email": "test@example.com",
                "avatar": null,
                "gender": 1,
                "age": 25,
                "bio": "喜欢旅行",
                "interests": null,
                "createdAt": "2024-01-01 12:00:00"
            }
        ],
        "total": 1,
        "page": 1,
        "size": 10
    }
}
```

### 7. 获取用户公开信息
- **接口地址**: `GET /user/profile/{userId}`
- **功能描述**: 获取指定用户的公开信息（不包含敏感信息）
- **路径参数**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | userId | Long | 是 | 用户ID |

- **请求示例**:
```
GET /user/profile/1
```

- **响应示例**:
```json
{
    "code": 1,
    "msg": null,
    "data": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "phone": "13800138000",
        "avatar": null,
        "gender": 1,
        "age": 25,
        "bio": "喜欢旅行",
        "interests": null,
        "createdAt": "2024-01-01 12:00:00"
    }
}
```

### 8. 用户登出
- **接口地址**: `POST /user/logout`
- **功能描述**: 用户登出
- **请求头**:
  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | username | String | 是 | 用户名 |

- **请求示例**:
```
POST /user/logout
Headers: username: testuser
```

- **响应示例**:
```json
{
    "code": 1,
    "msg": "登出成功",
    "data": null
}

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1 | 操作成功 |
| 0 | 操作失败 |
| 1001 | 用户不存在 |
| 1002 | 密码错误 |
| 1003 | 用户已存在 |
| 1004 | 参数错误 |
| 1005 | 权限不足 |

## 数据模型

### User 实体类
```java
public class User {
    private Long id;           // 用户ID
    private String username;   // 用户名
    private String password;   // 密码
    private String email;      // 邮箱
    private String phone;      // 手机号
    private String avatar;     // 头像URL
    private Integer gender;    // 性别：0-未知，1-男，2-女
    private Integer age;       // 年龄
    private String bio;        // 个人简介
    private String interests;  // 兴趣爱好（JSON字符串）
    private String createdAt; // 创建时间
}
```

### UserProfileVO 视图对象
```java
public class UserProfileVO {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Integer gender;
    private Integer age;
    private String bio;
    private List<String> interests; // 兴趣爱好列表
}
```

## 安全建议

1. **密码加密**: 建议使用BCrypt等安全算法对密码进行加密存储
2. **JWT Token**: 建议实现JWT token机制进行身份验证
3. **参数验证**: 添加输入参数验证，防止SQL注入和XSS攻击
4. **敏感信息**: 返回用户信息时过滤掉密码等敏感字段
5. **限流**: 对登录和注册接口添加限流机制

## 后续扩展建议

1. **头像上传**: 实现头像文件上传功能
2. **邮箱验证**: 注册时发送邮箱验证码
3. **手机验证**: 支持手机号注册和验证
4. **第三方登录**: 集成微信、QQ等第三方登录
5. **用户状态**: 添加在线/离线状态管理
6. **隐私设置**: 用户隐私权限控制

---

**文档版本**: v1.0  
**最后更新**: 2024年1月  
**维护人员**: 开发团队
