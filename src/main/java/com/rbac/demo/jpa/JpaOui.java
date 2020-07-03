package com.rbac.demo.jpa;

import com.rbac.demo.entity.Oui;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public interface JpaOui extends JpaRepository<Oui,Long> {

}
