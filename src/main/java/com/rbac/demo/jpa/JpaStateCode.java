package com.rbac.demo.jpa;

import com.rbac.demo.entity.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStateCode extends JpaRepository<StateCode,Integer> {

}
