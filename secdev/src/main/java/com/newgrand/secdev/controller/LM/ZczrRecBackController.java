package com.newgrand.secdev.controller.LM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.domain.BackMsgModel;
import com.newgrand.secdev.domain.LM.SaveResultModel;
import com.newgrand.secdev.helper.EntityConverter;
import com.newgrand.secdev.helper.I8Request;
import com.newgrand.secdev.helper.IJdbcTemplate;
import com.newgrand.secdev.helper.LMRequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/lmApi")
@Api("发货信息转发API接口")
public class ZczrRecBackController {

    @Autowired
    private I8Request i8Request;

    @Autowired
    private LMRequestHelper getData;

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    private IJdbcTemplate jdbcTemplate;

    @Autowired
    private EntityConverter entityConverter;

    /**
     * 发货信息获取方法名
     */
    @Value("${lm.getWLInfoFunc}")
    private String getWLInfoFunc;

    @ApiOperation(value = "发货信息", notes = "发货信息", produces = "application/json")
    @RequestMapping(value = "/zczrRecBackInfo", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject syncZczrRecBackInfo(@RequestBody String str) throws Exception {
        return getDataFromYSYJ(str);
    }

    /*根据发货单二维码信息取物料清单*/

    /**
     * 根据报文获取发货单Id,并发送请求到铝模系统，获取发货明细数据
     * @param str 请求报文
     * @return9
     * @throws Exception
     */
    private JSONObject getDataFromYSYJ(String str) throws Exception {
        JSONObject obj = JSON.parseObject(str);

        /*发货订单id*/
        //String logisticsId = obj.getString("logisticsId");
        String logisticsId = obj.getString("id");
        /*货物所属类别,组装url请求参数*/
        //String belongType = obj.getString("belongType");
        String belongType = obj.getString("type");
        StringBuilder params = new StringBuilder();
        params.append("logisticsId=").append(logisticsId);
        params.append("&");
        params.append("belongType=").append(belongType);

        //获取发货明细信息
        BackMsgModel back = getData.GetData(params.toString(), getWLInfoFunc);

        SaveResultModel result = new SaveResultModel();
        if (back.getResult() == "false") {
            result.setStatus("error");
            result.setMsg(back.getMessage());
            result.setErrorIs(true);
            result.setResBillNoOrIdEntity(null);
            result.setSaveRows(0);
            return (JSONObject) JSONObject.toJSON(result);
        }
        JSONObject body = JSON.parseObject(back.getData().toString());
        //业务组返回结果
        result = sendDataToApp(body, logisticsId);

        return (JSONObject) JSONObject.toJSON(result);
    }

    /**
     * 调用业务组保存方法
     * @param dataStr     铝模发货返回数据
     * @param logisticsId 发货单ID
     * @return
     */
    private SaveResultModel sendDataToApp(JSONObject dataStr, String logisticsId) throws Exception {
        SaveResultModel result = new SaveResultModel();
        try {
            JSONObject data = dataStr.getJSONObject("data");
            // 项目id
            String projectId = data.getString("projectId");
            // 合同id
            String contractId = data.getString("contractId");
            // 合同编码
            String contractCode = data.getString("contractCode");
            // 项目名称
            String projectName = data.getString("projectName");

            // 物流单明细
            JSONArray detailList = data.getJSONArray("detailList");

            JSONObject res = new JSONObject();

            //表头
            JSONObject mainFormStr = MainForm(contractId,projectId,projectName,logisticsId);
            //表体
            JSONArray detailStr = DetailForm(projectId,detailList);
            res.put("dtl",detailStr);
            res.put("mst",mainFormStr);

            result.setStatus("true");
            result.setMsg("获取发货单成功");
            result.setErrorIs(false);
            result.setResBillNoOrIdEntity(null);
            result.setSaveRows(0);
            result.setData(res);
        }
        catch (Exception ex){
            result.setStatus("error");
            result.setMsg("获取发货单信息失败," + ex.getMessage());
            result.setErrorIs(true);
            result.setResBillNoOrIdEntity(null);
            result.setSaveRows(0);
        }
        return result;
    }

    /**
     * 表体拼接
     * @param contractId 合同Id
     * @param projectId 项目Id(对应数据库中pc_no，项目编码)
     * @param logisticsId 发货单Id
     * @return
     */
    private JSONObject MainForm(String contractId, String projectId, String projectName, String logisticsId) {
        //SimpleDateFormat df = new SimpleDateFormat("yyyyMM");//设置日期格式
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar now = Calendar.getInstance();
        String mainFormStr = "{\"PhId\":\"\",\"BillType\":1,\"BillCode\":\"\",\"BillName\":\"\",\"BillDate\":\"\",\"PhidWouse\":\"\",\"PhidItemName\":\"\",\"PhidTreaty\":\"\",\"PhidTreatyName\":\"\",\"PhidBatchNoName\":\"\",\"RentOutName\":\"\",\"RentInName\":\"\",\"RentInDeptName\":\"\",\"RentMode\":\"\",\"PhidRentProj\":\"\",\"PhidRentProjName\":\"\",\"FreightMode\":\"\",\"PhidWorkCycle\":\"\",\"PhidFcur\":\"\",\"ExchRate\":1,\"HaveAmtFc\":\"\",\"FreightAmtFc\":\"\",\"TotalAmt\":\"\",\"TotalAmtFc\":\"\",\"Remark\":\"\",\"AttFlag\":0,\"PhidSchemeid\":\"\",\"ImpInfo\":\"\",\"RestAmt\":\"\",\"RestAmtFc\":\"\",\"ControlAmt\":\"\",\"ControlAmtFc\":\"\",\"PhidSourcemid\":\"\",\"ItemResource\":\"\",\"ResourceType\":\"\",\"PhidTeam\":\"\",\"PhidManager\":\"\",\"PhidDept\":\"\"}";

        String sql = "select title,Phid_SenComp as senComp,Phid_RecComp as recComp,Phid_Pc as pc,user_jbr as manger,user_jbbm as dept from pcm3_cnt_m where phid = '" + contractId + "'";
        var dataList = jdbcTemplate.queryForList(sql);

        String title = dataList.get(0).get("title").toString(); //合同标题
        String senComp = dataList.get(0).get("senComp").toString(); //出租方（乙方）
        String recComp = dataList.get(0).get("recComp").toString(); //租入方（甲方）
        String manger = dataList.get(0).get("manger").toString(); //经办人
        String dept = dataList.get(0).get("dept").toString(); //经办部门
        String pc = dataList.get(0).get("pc").toString(); //项目phid

        //默认收（1:收;2:退）
        String BillType = "1";
        //收货⽇期（默认接口产生时间）
        String BillDate = day.format(new Date()); // new Date()为获取当前系统时间
        //单据名称
        String BillName = (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日" + title + "周材进场单";
        //仓库
        String PhidWouse = "0";
        //协议编码（即周材租入合同PhId）
        String PhidTreaty = contractId;
        //协议名称（即周材租入合同名称）
        String PhidTreatyName = title;
        //出租方
        String RentOutName = senComp;
        //租入方(甲方)
        String RentInName = recComp;
        //租入方部门(甲方)
        String RentInDeptName = "";
        //租入方项目(甲方)
        String PhidRentProj = pc;
        //租赁模式（1:外租;2:内租;3:开票内租;） 默认 2:内租
        String RentMode = "2";
        //运费承担方
        String FreightMode = "";
        //统计周期（根据调用接口时间，反查数据库中统计周期帮助数据）
        String PhidWorkCycle = "";
        //班组
        String PhidTeam = "";
        //经办人
        String PhidManager = manger;
        //经办部门
        String PhidDept = dept;
        //币种（默认人民币）
        String PhIdFcur = "CNY";

        HashMap<String, Object> map = new HashMap<>();
        map.put("PhId", ""); //主键
        map.put("BillCode", ""); //单据编码
        map.put("BillName", BillName); //单据名称
        map.put("BillType", BillType); //单据类型（1:收;2:退,默认 收）
        map.put("BillDate", BillDate); //单据日期
        map.put("PhidWouse", PhidWouse); //仓库
        map.put("PhidTreaty", PhidTreaty); //合同PhId
        map.put("PhidTreatyName", PhidTreatyName); //协议名称
        map.put("RentOutName", RentOutName); //出租方
        map.put("RentInName", RentInName); //租入方
        map.put("RentInDeptName", RentInDeptName); //租入方部门
        map.put("RentMode", RentMode); //租赁模式
        map.put("PhidRentProj", PhidRentProj); //租入方项目
        map.put("PhidRentProjName", projectName); //租入方项目名称
        map.put("FreightMode", FreightMode); //运费承担方
        map.put("PhidWorkCycle", PhidWorkCycle); //统计周期
        map.put("PhidTeam", PhidTeam); //班组
        map.put("PhidManager", PhidManager); //经办人
        map.put("PhidDept", PhidDept); //经办部门
        map.put("PhidFcur", PhIdFcur); //币种（默认人民币）
        //map.put("PhidInputPsn", "0"); //录入人
        //map.put("InputDate", BillDate); //录入日期
        //map.put("user_lmxtid", logisticsId); //铝模系统发货单号id（用于判断数据是否录入，以及后续查询）
        return entityConverter.SetFieldEx(mainFormStr, map);
    }

    /**
     * 表体拼接
     * @param projectId 项目Id
     * @param detailList 明细List
     * @return
     * @throws Exception
     */
    private JSONArray DetailForm(String projectId, JSONArray detailList) throws Exception {
        String detailStr = "[{\"row\":{\"SortId\":0,\"PhidRentProj\":\"\",\"PhidRentProjName\":\"\",\"PhidItem\":\"\",\"PhidItemNo\":\"\",\"PhidItemName\":\"\",\"Spec\":\"\",\"PhidMsUnit\":\"\",\"PhidMsUnitName\":0,\"Qty\":0,\"RestQty\":\"\",\"ActQtyRec\":\"\",\"ActSquareRec\":\"\",\"PhidResBs\":\"\",\"PhidResBsNo\":\"\",\"PhidResBsName\":\"\",\"PhidCbsName\":\"\",\"PhidWbsName\":\"\",\"UnitTaxPrice\":0,\"UnitTaxPriceFc\":0,\"PhidResBs\":0,\"PhidResBsNo\":\"\",\"PhidResBsName\":\"\"}}]";
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        //遍历明细
        for (int i = 0; i < detailList.size(); i++) {
            String ItemNo = detailList.getJSONObject(i).getString("codeNum"); //物料编码
            String sql = "select phid,itemname from itemdata left join res_bs on ITEMDATA.PHID_RESBS = RES_BS.PHID  left join msunit on msunit.phid=itemdata.phid_msunit where itemno = '" + ItemNo + "'";
            var dataList = jdbcTemplate.queryForList(sql);

            String QTY = detailList.getJSONObject(i).getString("specNum"); //数量
            String ActQtyRec = detailList.getJSONObject(i).getString("specNum"); //实际收获数量 (默认等于数量)
            String ActSquareRec = detailList.getJSONObject(i).getString("specArea"); //实际收获面积
            String PhidRentProj = projectId; //租入方项目
            //String PhidRentProjName = ""; //租入方项目名称
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("QTY", QTY); //数量
            map.put("ActQtyRec", ActQtyRec); //实际收获数量
            map.put("ActSquareRec", ActSquareRec); //实际收获面积
            map.put("SortId",i); //排序号
            map.put("PhidRentProj",projectId); //项目id
            map.put("PhidItem",dataList.get(0).get("phid").toString()); //物料phid(主键)
            map.put("PhidItemNo",ItemNo); //物料编码
            map.put("PhidItemName",dataList.get(0).get("itemname").toString()); //物料名称
            //map.put("PhidMsUnit",ItemNo); //计量单位名称phid(主键)  主数据尚未对接，后续调整
            //map.put("PhidMsUnitName",ItemNo); //计量单位名称,帮助id:pms3.p_msunit  主数据尚未对接，后续调整
            map.put("PhidRentProj",PhidRentProj); //租入方项目
            //map.put("PhidRentProjName",PhidRentProjName); //租入方项目名称
            list.add(map);
        }
        return entityConverter.SetTableRowEx(detailStr,list);
    }
}
