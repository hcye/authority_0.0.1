package com.rbac.demo.jpa;

import com.rbac.demo.entity.Othermeans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOthermeans extends JpaRepository<Othermeans,Integer> {
}
