package com.newgrand.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class GetPhIdHelper {
    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected JdbcTemplate jdbcTemplate;

    /*
     * 功能描述: 通过编码获取表主键
     * @Param: [tname, codeName, codeValue]
     * @Return: java.lang.String
     */
    public String GetPhIdByCode(String tname,String codeName,String codeValue){
        String r = "";
        String sql="select phid from "+tname +" where  "+codeName+"='"+codeValue+"'";
        List<String> list = jdbcTemplate.queryForList(sql, null, String.class);
        if(list.size()>0)r=list.get(0);
        return r;
    }

    /*
     * 功能描述: 通过编码获取表对应字段
     * @Param: [tName, codeName, cnoName,cnoValue]
     * @Return: java.lang.String
     */
    public String GetCodeByCode(String tName,String codeName ,String cnoName,String cnoValue) {
        String r = "";
        String sql = "select "+codeName+" from " + tName + " where "+cnoName+"  ='" + cnoValue + "'";
        List<String> list = jdbcTemplate.queryForList(sql, null, String.class);
        if (list.size() > 0) r = list.get(0);
        return r;
    }

    /*
     * 功能描述: 通过过滤添加获取表对应字段
     * @Param: [tName, codeName, cnoName,cnoValue]
     * @Return: java.lang.String
     */
    public String GetCodeByFilter(String tName,String codeName ,String filter) {
        String r = "";
        String sql = "select "+codeName+" from " + tName + " where " + filter;
        List<String> list = jdbcTemplate.queryForList(sql, null, String.class);
        if (list.size() > 0) r = list.get(0);
        return r;
    }
}
