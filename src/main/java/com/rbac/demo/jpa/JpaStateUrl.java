package com.rbac.demo.jpa;

import com.rbac.demo.entity.StateUrl;
import com.rbac.demo.entity.StateUrlSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStateUrl extends JpaRepository<StateUrl,Integer> {
    List<StateUrl> findStateUrlsByStateUrlSetBySetFk(StateUrlSet urlSet);

}