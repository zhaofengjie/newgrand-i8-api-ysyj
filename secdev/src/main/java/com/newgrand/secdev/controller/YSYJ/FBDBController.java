package com.newgrand.secdev.controller.YSYJ;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.domain.AM.ResultModel;
import com.newgrand.secdev.domain.ApiType;
import com.newgrand.secdev.helper.I8Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dbjgApi")
@Api("定标结果API接口")
public class FBDBController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private I8Request i8Request;

    @ApiOperation(value = "分包定标结果", notes = "分包定标结果", produces = "application/json")
    @RequestMapping(value = "/dbInfo", method = RequestMethod.POST)
    public @ResponseBody JSONObject syncFBDBJGInfo(@RequestBody String str) {
        JSONObject re = new JSONObject();
        try {
            JSONObject fbInfo = JSON.parseObject(str);
            log.info(fbInfo.toString());
            re.put("result", "true");
            re.put("code", "1");
            re.put("message", "测试成功");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return re;
    }
}
