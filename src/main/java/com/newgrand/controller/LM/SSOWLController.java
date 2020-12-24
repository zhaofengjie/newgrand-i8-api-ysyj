package com.newgrand.controller.LM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.domain.BackMsgModel;
import com.newgrand.helper.LMRequestHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/lmApi")
@Api("根据铝模系统发货单id，重定向到物流追踪信息页面")
public class SSOWLController {

    @Autowired
    private LMRequestHelper requestHelper;

    @ResponseBody
    @GetMapping(path = "index")
    public void index(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        //发货单号
        String orderGID = request.getParameter("orderGID");
        //String objStr = JSON.toJSONString(request.getParameterMap());
        //JSONObject obj = JSON.parseObject(objStr);
        //String order = obj.getString("orderGID");
        //JSONArray arr = JSON.parseArray(order);
        //String orderGID = arr.getString(0); //发货单号

        String url = "";
        BackMsgModel back = requestHelper.GetWLUrl(orderGID);
        if(back.getResult() == "true")
        {
            url = back.getData().toString();
        }
        else
        {
            //url = "重定向访问失败,原因 :" + back.getMessage();
            resp.sendError(400,"重定向访问失败,原因 :" + back.getMessage());
        }
        resp.sendRedirect(url);
    }

    @ResponseBody
    @GetMapping(path = "getSSOUrl")
    public String GetSSOUrl(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        //发货单号
        String orderGID = request.getParameter("orderGID");
        //String objStr = JSON.toJSONString(request.getParameterMap());
        //JSONObject obj = JSON.parseObject(objStr);
        //String order = obj.getString("orderGID");
        //JSONArray arr = JSON.parseArray(order);
        //String orderGID = arr.getString(0); //发货单号

        String url = "";
        BackMsgModel back = requestHelper.GetWLUrl(orderGID);
        if(back.getResult() == "true")
        {
            url = back.getData().toString();
        }
        else
        {
            //url = "重定向访问失败,原因 :" + back.getMessage();
            url = "获取物料追踪链接失败,原因 :" + back.getMessage();
        }
       return url;
    }
}
