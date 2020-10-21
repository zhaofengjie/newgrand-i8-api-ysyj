package com.newgrand.secdev.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.domain.*;
import com.newgrand.secdev.helper.I8Request;
import com.newgrand.secdev.helper.SnowflakeIdWorker;
import com.newgrand.secdev.service.MdmService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mdmApi")
@Api("项目信息API接口")
public class MdmController {
    @Autowired
    private MdmService mdmService;

    @ApiOperation(value="项目信息推送", notes="项目信息推送", produces="application/json")
    @RequestMapping(value = "/projectInfo",method = RequestMethod.POST)
    public @ResponseBody WebRv ProjectInfo(@RequestBody String project)
    {
        WebRv rv=new WebRv();
        rv.setSuccess(true);
        try {
            JSONObject projectInfo = JSON.parseObject(project);
            JSONArray dataInfos = projectInfo.getJSONObject("DATA").getJSONArray("DATAINFOS");
            List<String> allSaveReturn =  new ArrayList();;
            for(int i=0;i<dataInfos.size();i++){
                JSONObject pro = dataInfos.getJSONObject(i);
                String saveRv = mdmService.SaveProject(pro);
                allSaveReturn.add(saveRv);
            }
            rv.setData(allSaveReturn);
        }
        catch (Exception e){
            rv.setSuccess(false);
            rv.setMsg(e.getMessage());
            return rv;
        }
        return rv;
    }

    @ApiOperation(value="客商信息推送", notes="客商信息推送", produces="application/json")
    @RequestMapping(value = "/enterpriseInfo",method = RequestMethod.POST)
    public @ResponseBody WebRv EnterpriseInfo(@RequestBody String project)
    {
        WebRv rv=new WebRv();
        rv.setSuccess(true);
        try {
            JSONObject projectInfo = JSON.parseObject(project);
            JSONArray dataInfos = projectInfo.getJSONObject("DATA").getJSONArray("DATAINFOS");
            List<String> allSaveReturn =  new ArrayList();;
            for(int i=0;i<dataInfos.size();i++){
                JSONObject pro = dataInfos.getJSONObject(i);
                String saveRv = mdmService.SaveEnterprise(pro);
                allSaveReturn.add(saveRv);
            }
            rv.setData(allSaveReturn);
        }
        catch (Exception e){
            rv.setSuccess(false);
            rv.setMsg(e.getMessage());
            return rv;
        }
        return rv;
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public  @ResponseBody WebRv Test() throws IOException {
        WebRv rv=new WebRv();
        rv.setSuccess(true);
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rv.setData(df.format(day));
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        long id= idWorker.nextId();
        rv.setMsg(Long.toString(id));
        return  rv;
    }
}
