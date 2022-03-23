package com.rbac.hcye_admin.jpa;


import com.rbac.hcye_admin.entity.SwGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGateway extends JpaRepository<SwGateway,Integer> {
    Page<SwGateway> findSwGatewaysByVlanidLike(String vlanid, Pageable pageable);
    SwGateway findSwGatewayByVlanid(String vlanid);
    SwGateway findSwGatewayByGateway(String gateway);
}
