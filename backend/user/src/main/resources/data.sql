create database good_friend ;
use good_friend;


-- 用户表
CREATE TABLE `user` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `username` VARCHAR(50) UNIQUE NOT NULL,
                        `password` VARCHAR(100) NOT NULL,
                        `email` VARCHAR(100) UNIQUE,
                        `phone` VARCHAR(20) UNIQUE,
                        `avatar` VARCHAR(255), -- 头像URL
                        `gender` TINYINT, -- 0未知 1男 2女
                        `age` INT,
                        `bio` TEXT, -- 自我介绍
                        `interests` JSON, -- 兴趣爱好标签
                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 动态表
CREATE TABLE `post` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `user_id` BIGINT NOT NULL,
                        `content` TEXT NOT NULL,
                        `images` JSON, -- 图片URL数组
                        `likes` INT DEFAULT 0,
                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

-- 好友关系表
CREATE TABLE `friend` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `friend_id` BIGINT NOT NULL,
                          `status` TINYINT DEFAULT 0, -- 0申请中 1已好友 2拒绝
                          `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                          FOREIGN KEY (`friend_id`) REFERENCES `user`(`id`),
                          UNIQUE KEY (`user_id`, `friend_id`)
);

-- 私信表
CREATE TABLE `message` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `sender_id` BIGINT NOT NULL,
                           `receiver_id` BIGINT NOT NULL,
                           `content` TEXT NOT NULL,
                           `is_read` BOOLEAN DEFAULT FALSE,
                           `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`),
                           FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`)
);

-- 非好友私信限制表（记录非好友已发消息）
CREATE TABLE `message_limit` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `sender_id` BIGINT NOT NULL,
                                 `receiver_id` BIGINT NOT NULL,
                                 `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`),
                                 FOREIGN KEY (`receiver_id`) REFERENCES `user`(`id`),
                                 UNIQUE KEY (`sender_id`, `receiver_id`)
);


-- 插入用户示例
INSERT INTO `user` (`username`, `password`, `email`, `gender`, `age`, `bio`)
VALUES
    ('小明', '123456', 'user1@test.com', 1, 25, '喜欢旅行和摄影'),
    ('小红', '123456', 'user2@test.com', 2, 28, '健身爱好者');

-- 插入动态示例
INSERT INTO `post` (`user_id`, `content`, `images`)
VALUES
    (1, '今天去了海边！', '["/images/beach1.jpg"]'),
    (2, '健身打卡Day 10', '["/images/gym1.jpg"]');

-- 插入好友关系（user1和user2成为好友）
INSERT INTO `friend` (`user_id`, `friend_id`, `status`)
VALUES (1, 2, 1);