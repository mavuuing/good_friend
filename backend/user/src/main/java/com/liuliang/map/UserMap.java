package com.liuliang.map;

import com.liuliang.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMap {
    @Select("select * from user where username=#{username}")
    User findByName(String username);

    @Select("select * from user where id=#{id}")
    User findById(Long id);

    @Insert("insert into user(username,password,email) values(#{username},#{password},#{email})")
    void addUser(String username, String password, String email);

    @Update("update user set email=#{email}, phone=#{phone}, avatar=#{avatar}, gender=#{gender}, age=#{age}, bio=#{bio}, interests=#{interests} where username=#{username}")
    void updateUserProfile(@Param("username") String username, 
                          @Param("email") String email, 
                          @Param("phone") String phone, 
                          @Param("avatar") String avatar, 
                          @Param("gender") Integer gender, 
                          @Param("age") Integer age, 
                          @Param("bio") String bio, 
                          @Param("interests") String interests);

    @Update("update user set password=#{newPassword} where username=#{username} and password=#{oldPassword}")
    int changePassword(@Param("username") String username, 
                      @Param("oldPassword") String oldPassword, 
                      @Param("newPassword") String newPassword);

    @Select("select * from user where username like concat('%', #{keyword}, '%') limit #{offset}, #{size}")
    java.util.List<User> searchUsers(@Param("keyword") String keyword, 
                                   @Param("offset") int offset, 
                                   @Param("size") int size);

    @Select("select count(*) from user where username like concat('%', #{keyword}, '%')")
    int countSearchUsers(@Param("keyword") String keyword);
}
