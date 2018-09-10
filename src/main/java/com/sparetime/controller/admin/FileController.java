package com.sparetime.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.sparetime.common.util.CommonUtil;
import com.sparetime.common.util.FileUtil;
import com.sparetime.domian.SysConfig;
import com.sparetime.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by muye on 17/7/26.
 */
@Controller
@RequestMapping("/file")
public class FileController {

//    @Value("${file.path}")
//    private String filePath;

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request){

        SysConfig sysConfig = sysConfigService.queryByKey("file_path");
        if (sysConfig == null || StringUtils.isEmpty(sysConfig.getValue()))
            throw new RuntimeException("路径配置不能为空");

        Map<String, Object> result = new HashMap<>();
        result.put("errno", 0);
        List<String> paths = null;
        try {
            paths = FileUtil.upload(files, sysConfig.getValue(), request.getServerPort());
        }catch (Exception e){
            result.put("errno", -1);
            e.printStackTrace();
        }

        result.put("data", paths);

        return JSONObject.toJSONString(result);
    }
}
