package com.rbac.demo.jpa;

import com.rbac.demo.entity.StateCode;
import com.rbac.demo.entity.StateUrlSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaStateCodeSet extends JpaRepository<StateUrlSet,Integer> {
    StateUrlSet findStateUrlSetBySetName(String name);
}
