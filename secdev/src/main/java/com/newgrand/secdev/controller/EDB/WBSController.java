package com.newgrand.secdev.controller.EDB;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.domain.ApiType;
import com.newgrand.secdev.domain.DataInfo;
import com.newgrand.secdev.domain.EDB.*;
import com.newgrand.secdev.helper.EntityConverter;
import com.newgrand.secdev.helper.I8Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WBS数据接收
 *
 * @Author ChenXiangLu
 * @Date 2020/11/25 15:49
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/edbApi")
@Api("经济数据库接口")
public class WBSController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private I8Request i8Request;

    @ApiOperation(value = "接收WBS数据", notes = "接收WBS数据", produces = "application/json")
    @RequestMapping(value = "/syncWbs", method = RequestMethod.POST)
    public EDBResultModel<ArrayList<EDBResultModel<String>>> syncWbs(@RequestBody WBSModel param) {
        var result = new EDBResultModel<ArrayList<EDBResultModel<String>>>();
        var data=new ArrayList<EDBResultModel<String>>();
        var pcId = jdbcTemplate.queryForObject("select phid from project_table where bill_no='" + param.getCode() + "'", String.class);
        var level1List=param.getProjectDXInfos();
        for(WBSLevel1Model level1 :level1List) {
            DataInfo dataInfo = paramLevel(level1, pcId);
            data.add(new EDBResultModel<String>(dataInfo.getStatus(),dataInfo.getErrorText(),"", level1.getId()));
        }
        result.setData(data);
        return result;
    }

    /**
     * 编辑层级关系  注意层级间的phid值
     * @param model
     * @param pcId
     * @return
     */
    public DataInfo paramLevel(WBSLevel1Model model, String pcId){
        DataInfo rvInfo = new DataInfo();
        int phid = -1;//初始化本级暂存id
        int level2Phid = 0;//初始化本级暂存id
        int level2ParentPhid = 0;//初始化末级暂存id
        int level3Phid = 0;//初始化本级暂存id
        int level3ParentPhid = 0;//初始化末级暂存id
        ArrayList<Map<String, Object>> allRow = new ArrayList<>();
        var level2List= model.getProjectDWInfos();
        var isLeaf=level2List==null||level2List.size()==0?true:false;
        Map<String, Object> newRow1 = paramProcess(model.getId(),model.getName(),pcId,phid,0,"1",isLeaf);
        allRow.add(newRow1);
        level2ParentPhid=phid;//二级时需要把一级的phid赋值给父级
        level2Phid=phid;
        for (WBSLevel2Model level2:level2List)
        {
            //每次循环生成新的id 循环过三层后最新的id是level3Phid
            if(level2Phid>level3Phid)
                level2Phid=level3Phid-1;
            else
                level2Phid=level2Phid-1;
            var level3List= level2.getBqItemFBInfos();
            if(level3List==null)
                level3List=level2.getMeasureFBInfos();
            else
                level3List.addAll(level2.getMeasureFBInfos());
            isLeaf=level3List==null||level3List.size()==0?true:false;
            Map<String, Object> newRow2 = paramProcess(level2.getId(),level2.getName(),pcId,level2Phid,level2ParentPhid,"2",isLeaf);
            allRow.add(newRow2);
            level3ParentPhid=level2Phid;//二级时需要把一级的phid赋值给父级
            level3Phid=level2Phid;
            for (WBSLevel3Model level3:level3List)
            {
                level3Phid-=1;//每次循环生成新的id
                isLeaf=true;
                Map<String, Object> newRow3 = paramProcess(level3.getId(),level3.getName(),pcId,level3Phid,level3ParentPhid,"3",isLeaf);
                allRow.add(newRow3);
            }
        }
//        ((Map<String,Object>)allRow.get(0).get("row")).put("isFirst", true);//第一个
//        ((Map<String,Object>)allRow.get(allRow.size() - 1)).put("isLast", true);//倒数第一个
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("savedata", "{\"table\":{\"key\":\"phid\",\"newRow\":"+JSONObject.toJSONString(allRow)+"},\"isChanged\":true}"));
        urlParameters.add(new BasicNameValuePair("wbscbsdata", "{\"table\":{\"key\":\"PhId\"}}"));
        try {
            String i8rv = i8Request.PostFormSync("/PMS/BasicData/WBS/WbsSave", urlParameters);
            JSONObject i8rvJson = JSON.parseObject(i8rv);
            if (i8rvJson != null && i8rvJson.getString("Status").toLowerCase().equals("success")) {
                rvInfo.setStatus("0");
                rvInfo.setErrorText("新增记录成功");
            } else {
                rvInfo.setStatus("1");
                rvInfo.setErrorText(i8rv);
            }
        }
        catch (Exception e)
        {
            rvInfo.setStatus("1");
            rvInfo.setErrorText(e.getMessage());
        }
        return rvInfo;
    }

    /**
     * 封装参数
     * @param wbsCode wbs編碼
     * @param wbsName wbs名稱
     * @param pcId 項目phid
     * @param phid 本级phid
     * @param parentPhid 父级phid
     * @param depth 深度
     * @param leaf 是否是叶子节点
     * @return
     */
    public Map<String, Object> paramProcess(String wbsCode,String wbsName, String pcId,int phid,int parentPhid,
                                            String depth,boolean leaf) {
        Map<String, Object> newRow = new HashMap<>();
        Map<String, Object> row = new HashMap<>();
        row.put("OrgPrc", 0);
        row.put("WbsRealcode", wbsCode);
        row.put("WbsName", wbsName);
        row.put("PcId", pcId);
        row.put("PhId", phid);
        row.put("ParentPhId", parentPhid);
        row.put("depth", depth);
        if (depth.equals("1")) {
            row.put("parentId", "root");
        }
        row.put("leaf", false);
        row.put("PhIdProduct", "");
        row.put("PhIdProduct_EXName", "");
        row.put("GclTotal", 0);
        row.put("Prc", 0);
        row.put("GclPrc", 0);
        row.put("Msunit", "");
        row.put("BeginDate", null);
        row.put("EndDate", null);
        row.put("ActualBeginDate", null);
        row.put("ActualEndDate", null);
        row.put("Forbid", "");
        row.put("WbsHelpName", "");
        row.put("FgPclass", "");
        row.put("Dm", "");
        row.put("Remarks", "");
        row.put("WbsHelp", "");
        row.put("MsunitName", "");
        row.put("FgPclassCode", "");
        row.put("FgPclassName", "");
        row.put("text", "");
        row.put("WbsParent", "");
        row.put("RelWbsCode", "");
        row.put("WbsPcParent", "");
        row.put("WbsParentName", "");
        row.put("PhidWbsTask", "0");
        row.put("PhidWbsTask_EXName", "");
        row.put("IsLock", 0);
        row.put("Profit", 1);
        row.put("Cost", 1);
        row.put("Schedule", 1);
        row.put("NgPhIdBP_Name", "");
        row.put("GbCode", "");
        row.put("GbCodeName", "");
        row.put("NgShareDataSearchKey", "");
        row.put("NgSuperiorVisibleSearchKey", "");
        row.put("NgOrgIdOriginal", "");
        row.put("NgPhIdOriginal", "");
        row.put("NgPhIdUIScheme", "");
        row.put("NgPhIdOrg", "");
        row.put("NgPhIdBP", "");
        row.put("NgPhIdCU", "");
        row.put("NgShareSign", 0);
        row.put("NgPhIdOrg_Name", "");
        row.put("NgBpPhids", "");
        row.put("NgBpNames", "");
        row.put("NgRecordVer", 0);
        row.put("index", 0);
        row.put("expanded", true);
        row.put("expandable", true);
        row.put("checked", null);
        row.put("cls", "");
        row.put("iconCls", "");
        row.put("icon", "");
        row.put("root", false);
        row.put("isLast", false);
        row.put("isFirst", false);
        row.put("allowDrop", true);
        row.put("allowDrag", true);
        row.put("loaded", false);
        row.put("loading", false);
        row.put("href", "");
        row.put("hrefTarget", "");
        row.put("qtip", "");
        row.put("qtitle", "");
        row.put("qshowDelay", 0);
        row.put("children", null);
        row.put("key", null);
        newRow.put("row", row);
        return newRow;
    }
}
