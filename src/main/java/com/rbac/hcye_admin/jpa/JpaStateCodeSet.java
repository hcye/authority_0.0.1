package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.StateUrlSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStateCodeSet extends JpaRepository<StateUrlSet,Integer> {
    StateUrlSet findStateUrlSetBySetName(String name);
}
