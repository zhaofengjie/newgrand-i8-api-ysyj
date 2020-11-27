package com.newgrand.secdev.controller.EDB;

import com.newgrand.secdev.domain.EDB.CbsModel;
import com.newgrand.secdev.domain.EDB.CntModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 成本科目
 * @Author ChenXiangLu
 * @Date 2020/11/27 17:46
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/edbApi")
@Api("经济数据库接口")
public class CbsController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected JdbcTemplate jdbcTemplate;

    @ApiOperation(value="推送CBS数据到经济数据库", notes="推送CBS数据到经济数据库", produces="application/json")
    @RequestMapping(value = "/getCbs",method = RequestMethod.GET)
    public String syncCbs()
    {
        var sql="select t1.cbs_code,t1.cbs_name,t1.note,t1.cbs,t1.parent_code,t1.remarks from bd_cbs t1";
        RowMapper<CbsModel> rowMapper=new BeanPropertyRowMapper(CbsModel.class);
        List<CbsModel> cnts= jdbcTemplate.query(sql, rowMapper);
        return  "测试成功";
    }
}
