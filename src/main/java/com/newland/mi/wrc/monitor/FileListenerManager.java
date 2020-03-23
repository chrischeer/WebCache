package com.newland.mi.wrc.monitor;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * @author ：Miracle.
 * @date ：Created in 15:00 2018/7/31
 */
public class FileListenerManager {

    /**
     * 开启监视文件状态变化
     */
    public static void watchFileStatus(String watchPath, long interval) {
        // 创建过滤器
//        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
//        IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"));
//        IOFileFilter filter = FileFilterUtils.or(directories, files);
        // 使用过滤器
//        FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);

        //不使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(watchPath));
        observer.addListener(new FileListener(watchPath));
        //创建文件变化监听器
        Constant.fileAlterationMonitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        try {
            Constant.fileAlterationMonitor.start();
//            monitor.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
