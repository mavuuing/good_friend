package com.liuliang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private String content;
    // JSON 数组字符串，如 ["/images/1.jpg"]
    private String images;
    private Integer likes;
    private String createdAt;
}
