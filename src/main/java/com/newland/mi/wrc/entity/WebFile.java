package com.newland.mi.wrc.entity;

/**
 * @author ：Miracle.
 * @date ：Created in 14:50 2018/7/31
 */
public class WebFile {
    public long lastModifiedTime;
    public String serverRelativeFilePath;

    public WebFile(long lastModifiedTime, String serverRelativeFilePath) {
        this.lastModifiedTime = lastModifiedTime;
        this.serverRelativeFilePath = serverRelativeFilePath;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getServerRelativeFilePath() {
        return serverRelativeFilePath;
    }

    public void setServerRelativeFilePath(String serverRelativeFilePath) {
        this.serverRelativeFilePath = serverRelativeFilePath;
    }
}
