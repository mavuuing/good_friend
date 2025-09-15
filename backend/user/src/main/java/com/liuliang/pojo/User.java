package com.liuliang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class User {
//id username password email phone avatar gender age
// bio interests createdAt
    public int id;

    public String username;

    public String password;

    public String email;

    public String phone;
    //头像
    public String avatar;

    public String gender;

    public int age;

    public String bio;

    public String interests;

    public String createdAt;
}
