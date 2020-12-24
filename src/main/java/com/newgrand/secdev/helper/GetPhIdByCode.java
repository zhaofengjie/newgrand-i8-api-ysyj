package com.newgrand.secdev.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

public class GetPhIdByCode {
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
}
