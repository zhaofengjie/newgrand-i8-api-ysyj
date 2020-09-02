package com.newgrand.secdev.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.domain.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Api("项目信息API接口")
public class ProjectInfoController {

    @ApiOperation(value="项目信息推送", notes="项目信息推送", produces="application/json")
    @RequestMapping(value = "/projectinfo",method = RequestMethod.POST)
    public @ResponseBody MDMFeedBack projectinfo(@RequestBody Object project)
    {
        MDMFeedBack mdmFeedBack = new MDMFeedBack();
        ESB esb = new ESB();
        DATA esbdata = new DATA();
        try {
            esb.setCode("S");
            esb.setDesc("接收成功");

            JSONObject jsonObject = (JSONObject)JSON.toJSON(project);
            JSONObject data = jsonObject.getJSONObject("DATA");
            String UUID =  data.getString("UUID");
            System.out.println("最外层UUID："+UUID);

            JSONArray datainfos = data.getJSONArray("DATAINFOS");
            int count = datainfos.size();
            System.out.println("count ："+count);

            DATAINFOS esbdatainfos = new DATAINFOS();
            esbdatainfos.setUuid(UUID);
            List<Object> dataList = new ArrayList<Object>();
            System.out.println("111:"+dataList.toArray());

            for(int i=0;i<count;i++)
            {
                DATAINFO datainfo = new DATAINFO();
                JSONObject proj = datainfos.getJSONObject(i);
                String DESC1 = proj.getString("DESC1");
                String DESC2 = proj.getString("DESC2");
                String uuid = proj.getString("UUID");
                String CODE = proj.getString("CODE");

                datainfo.setUuid(uuid);
                datainfo.setCode(CODE);
                datainfo.setVersion("0");
                try
                {
                    //处理具体数据逻辑

                    datainfo.setStatus("0");
                    datainfo.setErrorText("新增成功");

                    System.out.println(i+"接收到的参数DESC1："+DESC1+",DESC2:"+DESC2);
                }catch (Exception ex)
                {
                    datainfo.setStatus("1");
                    datainfo.setErrorText("出现异常:"+ex.getMessage());
                }
                dataList.add(datainfo);
            }

            esbdatainfos.setDatainfo(dataList);
            esbdata.setDatainfos(esbdatainfos);
            esb.setData(esbdata);
        }catch (Exception ex)
        {
            esb.setCode("E");
            esb.setDesc("接收失败");
            esb.setData(null);
            System.out.println("出现异常"+ex.getMessage());
        }

        mdmFeedBack.setEsb(esb);
        return mdmFeedBack;
    }
}
