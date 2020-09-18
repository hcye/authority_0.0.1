package com.rbac.demo.jpa;

import com.rbac.demo.entity.AssertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAssertType extends JpaRepository<AssertType,Integer> {
        @Query("select spname.typeName from AssertType spname")
        List<String> findAssertTypeNames();
}
