package com.newland.mi.wrc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.mi.wrc.entity.WebFile;
import com.newland.mi.wrc.monitor.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author ：Miracle.
 * @date ：Created in 15:36 2018/7/18
 */
@Service
public class CacheService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    /**
     * 监控目录
     */
    @Value("${webcache.watchPath}")
    public String watchPath;

    /**
     * 获取单个文件状态
     */
    public JSONObject getFileStatus(WebFile webFile) {
        String serverRelativeFilePath = webFile.getServerRelativeFilePath();
        long clientLastModifiedTime = webFile.getLastModifiedTime();

        int fileStatus = 0;
        if (!Constant.fileLastModifiedMap.containsKey(serverRelativeFilePath)) {
            LOGGER.info("FileListenerManager.rootDir + serverRelativeFilePath:" + watchPath + serverRelativeFilePath);
            File watchedFile = new File(watchPath + serverRelativeFilePath);
            if (watchedFile.exists()) {
                Constant.fileLastModifiedMap.put(serverRelativeFilePath, watchedFile.lastModified() / 1000);
            } else {
                fileStatus = 2;
            }
        }
        LOGGER.info("fileLastModifiedMapTime:" + Constant.fileLastModifiedMap.get(serverRelativeFilePath));
        LOGGER.info("clientLastModifiedTime:" + clientLastModifiedTime);

        if (Constant.fileLastModifiedMap.get(serverRelativeFilePath) > clientLastModifiedTime) {
            fileStatus = 1;
            LOGGER.info("需更新");
        } else {
            LOGGER.info("不需更新");
        }
        JSONObject resultJsonObj = new JSONObject();
        resultJsonObj.put("fileStatus", fileStatus);
        resultJsonObj.put("lastModifiedTime", Constant.fileLastModifiedMap.get(serverRelativeFilePath));
        return resultJsonObj;
    }

    /**
     * 获取文件列表状态
     */
    public JSONObject checkUpdate(JSONArray fileUpdateStatusJsonArray) {
        JSONObject retJsonObj = new JSONObject();
        JSONArray needUpdate = new JSONArray();
        JSONArray needDelete = new JSONArray();
        fileUpdateStatusJsonArray.forEach(oneFile -> {
            JSONObject oneFileStatusJsonObj = (JSONObject) oneFile;
            long lastModifiedTime = oneFileStatusJsonObj.getLong("lastModifiedTime");
            String serverRelativeFilePath = oneFileStatusJsonObj.getString("serverRelativeFilePath");
            String fileUrl = oneFileStatusJsonObj.getString("fileUrl");

            if (!Constant.fileLastModifiedMap.containsKey(serverRelativeFilePath)) {
                LOGGER.info("FileListenerManager.rootDir + serverRelativeFilePath:" + watchPath + serverRelativeFilePath);
                File watchedFile = new File(watchPath + serverRelativeFilePath);
                if (watchedFile.exists()) {
                    Constant.fileLastModifiedMap.put(serverRelativeFilePath, watchedFile.lastModified() / 1000);
                } else {
                    needDelete.add(fileUrl);
                }
            }
            if (Constant.fileLastModifiedMap.containsKey(serverRelativeFilePath) && Constant.fileLastModifiedMap.get(serverRelativeFilePath) > lastModifiedTime) {
                needUpdate.add(fileUrl);
            }
        });
        retJsonObj.put("needDelete", needDelete);
        retJsonObj.put("needUpdate", needUpdate);
        return retJsonObj;
    }
}

