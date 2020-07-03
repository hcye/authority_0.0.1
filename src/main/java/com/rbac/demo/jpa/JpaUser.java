package com.rbac.demo.jpa;

import com.rbac.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUser extends JpaRepository<User,Integer> {
    User findUserByUnameAndPwd(String name,String pwd);
}
