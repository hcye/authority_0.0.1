package com.rbac.demo.jpa;


import com.rbac.demo.entity.SwGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGateway extends JpaRepository<SwGateway,Integer> {
    Page<SwGateway> findSwGatewaysByVlanidLike(String vlanid, Pageable pageable);
    SwGateway findSwGatewayByVlanid(String vlanid);
    SwGateway findSwGatewayByGateway(String gateway);
}
