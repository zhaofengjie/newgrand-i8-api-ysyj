package com.newgrand.secdev.controller.EDB;

import com.newgrand.secdev.domain.EDB.BOQModel;
import com.newgrand.secdev.domain.EDB.EDBResultModel;
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
 * BOQ
 * @Author ChenXiangLu
 * @Date 2020/11/25 16:42
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/edbApi")
@Api("经济数据库接口")
public class BOQController {

    @ApiOperation(value="接收BOQ数据", notes="接收BOQ数据", produces="application/json")
    @RequestMapping(value = "/syncBoq",method = RequestMethod.POST)
    public EDBResultModel<ArrayList<EDBResultModel>> syncBoq(@RequestBody BOQModel param)
    {
        var result=new EDBResultModel<ArrayList<EDBResultModel>>();

        return result;
    }

}
