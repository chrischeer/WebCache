package com.newland.mi.wrc.monitor;

import org.apache.commons.io.monitor.FileAlterationMonitor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miracle
 */
public class Constant {
    /**
     * 保存文件相对路径与最后修改时间的关系
     */
    public static ConcurrentHashMap<String, Long> fileLastModifiedMap = new ConcurrentHashMap<>();
    /**
     * 文件监控
     */
    public static FileAlterationMonitor fileAlterationMonitor;
}
