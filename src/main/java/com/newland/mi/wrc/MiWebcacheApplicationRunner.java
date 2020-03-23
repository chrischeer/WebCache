package com.newland.mi.wrc;

import com.newland.mi.wrc.monitor.FileListenerManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Miracle
 * @date 2018/10/11
 * 在应用启动后执行
 */
@Component
public class MiWebcacheApplicationRunner implements ApplicationRunner {
    /**
     * 监控目录
     */
    @Value("${webcache.watchPath}")
    public String watchPath;
    /**
     * 轮询间隔 毫秒
     */
    @Value("${webcache.interval:1000}")
    public long interval;

    @Override
    public void run(ApplicationArguments var1) {
        //文件监控开启
        FileListenerManager.watchFileStatus(watchPath, interval);
    }
}
