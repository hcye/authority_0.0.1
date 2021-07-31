package com.rbac.demo.tool;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class RepoUrlCheck {
    public static String authSvn(String svnurl, String username,
                                           String password) {
        // 初始化版本库
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
        // 创建库连接
        SVNRepository repository;
        try {
            repository = SVNRepositoryFactory.create(SVNURL
                    .parseURIEncoded(svnurl));
            // 身份验证
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());
            // 创建身份验证管理器
            repository.setAuthenticationManager(authManager);
            repository.getLatestRevision();
        } catch (SVNException e) {
            return "error";
        }
        return "success";
    }
}
