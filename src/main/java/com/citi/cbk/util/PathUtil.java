package com.citi.cbk.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class PathUtil {
    // todo 有问题, 打成jar包获取失败, 得用流获取
    public static String getClassResPath(String dir) {
        ApplicationHome h = new ApplicationHome(PathUtil.class);
        File jarF = h.getSource();
        String parentPath = jarF.getParentFile().getAbsolutePath();
        return parentPath + "/"+ dir +"/";
    }

    public static String getClassParentPath(String dir) {
        ApplicationHome h = new ApplicationHome(PathUtil.class);
        File jarF = h.getSource();
        String parentPath = jarF.getParentFile().getAbsolutePath();
        return parentPath + "/"+ dir +"/";
    }
}
