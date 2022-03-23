package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.StateUrl;
import com.rbac.hcye_admin.entity.StateUrlSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStateUrl extends JpaRepository<StateUrl,Integer> {
    List<StateUrl> findStateUrlsByStateUrlSetBySetFk(StateUrlSet urlSet);

}