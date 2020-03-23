package com.newland.mi.wrc.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.mi.wrc.dto.ReqMsg;
import com.newland.mi.wrc.dto.RetMsg;
import com.newland.mi.wrc.entity.WebFile;
import com.newland.mi.wrc.monitor.Constant;
import com.newland.mi.wrc.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ：Miracle.
 * @date ：Created in 15:36 2018/7/18
 */
@RestController
@RequestMapping(value = "webcache")
@ResponseBody
public class CacheController {
    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 获取单个文件更新状态
     * @param reqMsg
     * @return
     */
    @PostMapping(value = "/status")
    public RetMsg getFileStatus(@Valid @RequestBody ReqMsg reqMsg) {
        WebFile webFile = reqMsg.getContentObject(WebFile.class);
        JSONObject fileStatusJsonObj = cacheService.getFileStatus(webFile);
        return RetMsg.newInstance().setContent(fileStatusJsonObj);
    }

    /**
     * 获取多个文件更新状态
     * @param reqMsg
     * @return
     */
    @PostMapping(value = "/check_update")
    public RetMsg checkUpdate(@Valid @RequestBody ReqMsg reqMsg) {
        JSONArray fileUpdateStatusJsonArray = reqMsg.getContent().getJSONArray("fileUpdateStatusJsonArray");
        JSONObject retJsonObj = cacheService.checkUpdate(fileUpdateStatusJsonArray);
        return RetMsg.newInstance().setContent(retJsonObj);
    }

    @PostMapping(value = "/reload")
    public RetMsg refresh(@Valid @RequestBody ReqMsg reqMsg) {
        Constant.fileAlterationMonitor.run();
        return RetMsg.newInstance();
    }
}
