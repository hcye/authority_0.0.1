package com.rbac.demo;

import com.rbac.demo.entity.AssetType;
import com.rbac.demo.jpa.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
public class Test1 {
    @Autowired
    JpaRole2Resources jpaRole2Resources;
    @Autowired
    JpaRole jpaRole;
    @Autowired
    JpaGroup jpaGroup;
    @Autowired
    JpaEmployee jpaEmployee;
    @Autowired
    JpaResources jpaResources;
    @Autowired
    JpaDevType jpaDevType;
    @Autowired
    JpaAssert jpaAssert;
    @Autowired
    JpaAssetType jpaAssetType;
    @Test
    public void t() throws IOException {

        String url = ResourceUtils.getURL("classpath:").getPath();
        System.out.println(url);
    }
}
