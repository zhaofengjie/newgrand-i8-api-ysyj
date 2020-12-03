package com.newgrand.secdev.controller.AM;


import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shippingInfoApi")
@Api("发货信息API接口")
public class ShippingInfoController {


    @Autowired
    private I8Request i8Request;

    @ApiOperation(value="发货信息", notes="发货信息", produces="application/json")
    @RequestMapping(value = "/shippingInfo",method = RequestMethod.POST)
    public @ResponseBody JSONObject syncShippingInfo(@RequestBody String str) {
        return getJsonObject(str, i8Request);
    }

    public static JSONObject getJsonObject(@RequestBody String str, I8Request i8Request) {
        JSONObject re = new JSONObject();
        re.put("result", "true");
        try {
            JSONObject res = JSON.parseObject(str);
            re.put("code", "1");
            re.put("message", "保存成功");
            log.info(res.toString());
            return re;
            /* //项目信息
            JSONObject pro = JSON.parseObject(res.getJSONObject("data").getString("project"));
            //项目合同
            JSONObject cnt = JSON.parseObject(res.getJSONObject("data").getString("projectContract"));
            //发货信息
            JSONObject info = JSON.parseObject(res.getJSONObject("data").getString("project"));
            //发货明细详情
            JSONObject dtl = JSON.parseObject(res.getJSONObject("data").getString("project"));
            List<NameValuePair> urlParameters = new ArrayList<>();

            String i8rv = i8Request.PostFormSync("/SUP/CustomPC/save", urlParameters);
            JSONObject i8rvJson = JSON.parseObject(i8rv);
            //
            if (i8rvJson != null && i8rvJson.getString("Status").toLowerCase().equals("success")) {
                re.put("code", "1");
                re.put("message", "保存成功");
                log.info("");
            } else {
                re.put("code", "0");
                re.put("message", "保存失败,");
                log.error("");
            }
            */
        } catch (Exception ex) {
            re.put("code", "0");
            re.put("message", "保存失败," + ex.getMessage());
            log.error("保存失败,原因");
        }
        return re;
    }
}
