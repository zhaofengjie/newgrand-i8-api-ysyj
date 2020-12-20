package com.newgrand.secdev.controller.LM;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/lmApi")
@Api("根据铝模系统发货单id获取物流追踪信息")
public class GetLogisticsinfo {

    @ApiOperation(value = "获取物流追踪信息", notes = "获取物流追踪信息", produces = "application/json")
    @RequestMapping(value = "/getWLInfo", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject getLogisticsInfo(@RequestBody String str) throws IOException {
        return getWLXXInfo(str);
    }

    private JSONObject getWLXXInfo(String str) {
        JSONObject res = new JSONObject();

        return res;
    }
}
