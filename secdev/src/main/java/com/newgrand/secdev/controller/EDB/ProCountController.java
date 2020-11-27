package com.newgrand.secdev.controller.EDB;

import com.newgrand.secdev.domain.EDB.BOQModel;
import com.newgrand.secdev.domain.EDB.EDBResultModel;
import com.newgrand.secdev.domain.EDB.ProCountModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 产值接口
 * @Author ChenXiangLu
 * @Date 2020/11/25 21:06
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/edbApi")
@Api("经济数据库接口")
public class ProCountController {


    @ApiOperation(value="接收产值数据", notes="接收产值数据", produces="application/json")
    @RequestMapping(value = "/Get",method = RequestMethod.GET)
    public String syncProCount()
    {
        return  "测试成功";
    }

    @ApiOperation(value="接收产值数据", notes="接收产值数据", produces="application/json")
    @RequestMapping(value = "/syncProCount",method = RequestMethod.POST)
    public EDBResultModel<ArrayList<EDBResultModel>> syncProCount(@RequestBody ProCountModel param)
    {
        var result=new EDBResultModel<ArrayList<EDBResultModel>>();

        return result;
    }
}
