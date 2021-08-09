package com.rbac.demo.tool;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public static String authGit(String url, String username, String password){
        try {
            Collection<Ref> refList;
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                UsernamePasswordCredentialsProvider pro = new UsernamePasswordCredentialsProvider(username, password);
                refList = Git.lsRemoteRepository().setRemote(url).setCredentialsProvider(pro).call();
            } else {
                refList = Git.lsRemoteRepository().setRemote(url).call();
            }
            List<String> branchnameList = new ArrayList<>(4);
            for (Ref ref : refList) {
                String refName = ref.getName();
                if (refName.startsWith("refs/heads/")) {                       //需要进行筛选
                    String branchName = refName.replace("refs/heads/", "");
                    branchnameList.add(branchName);
                }
            }
            System.out.println("success");
            return "success";
        } catch (Exception e) {
            System.out.println("error");
            return "error";
        }
    }
}

