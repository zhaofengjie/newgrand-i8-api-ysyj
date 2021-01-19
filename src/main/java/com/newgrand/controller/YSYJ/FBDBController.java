package com.newgrand.controller.YSYJ;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.helper.EntityConverter;
import com.newgrand.helper.GetPhIdHelper;
import com.newgrand.helper.I8Request;
import com.newgrand.helper.IJdbcTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ysyjApi")
@Api("定标结果API接口")
public class FBDBController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected IJdbcTemplate ijdbcTemplate;

    @Autowired
    private I8Request i8Request;

    @Autowired
    private EntityConverter entityConverter;

    @Autowired
    private GetPhIdHelper getPhIdHelper;

    private static final String mstFormStrNew = "{\"form\":{\"key\":\"phid\",\"newRow\":{\"bill_no\":\"\",\"title\":\"\",\"bill_dt\":\"2021-01-14\",\"pc\":\"468210113000002\",\"u_xmbm\":\"F-455455\",\"u_htlx\":\"155201102000001\",\"u_fbsqbm\":\"582210113000003\",\"u_jfdw\":\"\",\"u_jfdb\":\"\",\"u_jfdblxfs\":\"\",\"u_yfdw\":\"\",\"u_yfdb\":\"\",\"u_yfdblxfs\":\"\",\"u_jehs\":\"\",\"u_jews\":\"\",\"u_se\":\"\",\"u_zz\":\"438201029000001\",\"u_zbydfkfs\":\"\",\"u_zbydjsfs\":\"\",\"u_bz\":\"\",\"u_ysyjid\":\"\",\"u_dbjgdh\":\"\",\"phid\":\"\",\"ocode\":\"438201029000001\",\"code\":\"\",\"creator\":\"\",\"editor\":\"\",\"ng_insert_dt\":\"\",\"ng_update_dt\":\"\",\"ng_record_ver\":\"\",\"cur_orgid\":\"\",\"ng_phid_org\":\"\",\"ng_phid_cu\":\"\",\"ng_phid_bp\":\"\",\"ng_phid_original\":\"\",\"ng_orgid_original\":\"\",\"ng_phid_ui_scheme\":\"\",\"ng_sv_search_key\":\"\",\"ng_sd_search_key\":\"\",\"ng_share_sign\":\"\",\"is_wf\":\"\",\"ischeck\":\"\",\"checkpsn\":\"\",\"blobdocid\":\"\",\"tr_date\":\"\",\"voucher_flag\":\"\",\"tr_num\":\"\",\"tr_type\":\"\",\"da_flg\":\"\",\"asr_flg\":\"\",\"s_task_flg\":\"\",\"phid_schemeid\":\"538210106000002\",\"imp_info\":\"\",\"key\":\"\"}}}";
    private static final String mstFormStrModilfy = "{\"form\":{\"key\":\"phid\",\"modifiedRow\":{\"bill_no\":\"\",\"title\":\"\",\"bill_dt\":\"2021-01-14\",\"pc\":\"468210113000002\",\"u_xmbm\":\"F-455455\",\"u_htlx\":\"155201102000001\",\"u_fbsqbm\":\"582210113000003\",\"u_jfdw\":\"\",\"u_jfdb\":\"\",\"u_jfdblxfs\":\"\",\"u_yfdw\":\"\",\"u_yfdb\":\"\",\"u_yfdblxfs\":\"\",\"u_jehs\":\"\",\"u_jews\":\"\",\"u_se\":\"\",\"u_zz\":\"438201029000001\",\"u_zbydfkfs\":\"\",\"u_zbydjsfs\":\"\",\"u_bz\":\"\",\"u_ysyjid\":\"\",\"u_dbjgdh\":\"\",\"phid\":\"\",\"ocode\":\"438201029000001\",\"code\":\"\",\"creator\":\"\",\"editor\":\"\",\"ng_insert_dt\":\"\",\"ng_update_dt\":\"\",\"ng_record_ver\":\"\",\"cur_orgid\":\"\",\"ng_phid_org\":\"\",\"ng_phid_cu\":\"\",\"ng_phid_bp\":\"\",\"ng_phid_original\":\"\",\"ng_orgid_original\":\"\",\"ng_phid_ui_scheme\":\"\",\"ng_sv_search_key\":\"\",\"ng_sd_search_key\":\"\",\"ng_share_sign\":\"\",\"is_wf\":\"\",\"ischeck\":\"\",\"checkpsn\":\"\",\"blobdocid\":\"\",\"tr_date\":\"\",\"voucher_flag\":\"\",\"tr_num\":\"\",\"tr_type\":\"\",\"da_flg\":\"\",\"asr_flg\":\"\",\"s_task_flg\":\"\",\"phid_schemeid\":\"538210106000002\",\"imp_info\":\"\",\"key\":\"\"}}}";
    private static final String dtlFormStr = "[{\"row\":{\"u_gclqdbh\":\"001\",\"u_gclqdmc\":\"测试\",\"u_jldw\":\"192492810021888\",\"u_jldw_name\":\"克\",\"u_qty\":0,\"u_gznr\":\"1\",\"u_clgyjpp\":\"1\",\"u_jlgz\":\"1\",\"u_djws\":0,\"u_qzrgfdjws\":0,\"u_hsws\":0,\"u_qzrgfhjws\":0,\"u_sl\":0,\"u_se\":0,\"u_djhs\":0,\"u_qzrgfdjhs\":0,\"u_hjhs\":0,\"u_qzrgfhjhs\":0,\"u_boqid\":\"\",\"u_wbs\":\"\",\"u_wbs_name\":\"\",\"u_cbs\":\"\",\"u_cbs_name\":\"\",\"u_bz\":\"\",\"u_jjsjkid\":\"\",\"u_ysyjmxid\":\"\",\"phid\":\"\",\"m_code\":\"\",\"lineid\":0,\"code\":\"\",\"rel_key1\":\"\",\"itemid\":\"\",\"phid_itemdata\":\"\",\"phid_resbs\":\"\",\"res_propertys\":\"\",\"s_groupfield\":\"\",\"s_tree_id\":\"\",\"s_tree_pid\":\"\",\"imp_info\":\"\",\"key\":null}}]";


    @ApiOperation(value = "接收分包定标结果", notes = "接收分包定标结果", produces = "application/json")
    @RequestMapping(value = "/dbInfo", method = RequestMethod.POST)
    public @ResponseBody JSONObject syncFBDBJGInfo(@RequestBody String str) {
        JSONObject re = new JSONObject();
        try {
            JSONObject fbInfo = JSON.parseObject(str);
            //log.info(fbInfo.toString());

            //JSONObject mst = fbInfo.getJSONObject("mst"); //定标结果表头
            String instanceId = fbInfo.getString("instanceId");//唯一标识主键
            String dutyNum = fbInfo.getString("dutyNum");//分包申请编码
            String bidUnit = fbInfo.getString("bidUnit");//甲方单位
            //分包申请phId
            String fbPhId = getPhIdHelper.GetPhIdByCode("pms3_fb_subj_m", "bill_no", dutyNum);
            if (StringUtils.isEmpty(fbPhId)) {
                re.put("sucess", "N");;
                re.put("message", "保存失败,原因: 分包申请{" + dutyNum + "}，不存在");
                return re;
            }
            String supplierCompanyCode = fbInfo.getString("supplierCompanyCode");//乙方单位编码(即中标单位，根据名称匹配)
            //甲方单位
            String jfdw = getPhIdHelper.GetPhIdByCode("fg3_enterprise", "compno", bidUnit);
            if (StringUtils.isEmpty(jfdw)) {
                re.put("sucess", "N");;
                re.put("message", "保存失败,原因: 甲方单位{" + bidUnit + "}，不存在");
                return re;
            }
            //乙方单位
            String yfdw = getPhIdHelper.GetPhIdByCode("fg3_enterprise", "compno", supplierCompanyCode);
            if (StringUtils.isEmpty(yfdw)) {
                re.put("sucess", "N");;
                re.put("message", "保存失败,原因: 乙方单位{" + supplierCompanyCode + "}，不存在");
                return re;
            }
            JSONArray dtl = fbInfo.getJSONArray("line"); //获取到定标结果明细

            //查询发货单据phid
            String sql = "select phid from p_form_fbgl_fbdbjg_m where u_ysyjid = '"+instanceId +"'";
            String oldPhId = ijdbcTemplate.queryForObject(sql, String.class);
            if (oldPhId == "" || oldPhId == null) {
                String mstFormData = mstFormData(fbInfo, "", jfdw, yfdw ,fbPhId,mstFormStrNew);
                String dtlFormData = dtlFormData(dtl, "" ,"newRow");
                /*新增方法*/
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("otype", "add"));//单据类型
                urlParameters.add(new BasicNameValuePair("isContinue", "0"));
                urlParameters.add(new BasicNameValuePair("mstform", mstFormData));//主表
                urlParameters.add(new BasicNameValuePair("phidtemplate", ""));
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d", dtlFormData));//明细表
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d_AllData", dtlFormData));
                urlParameters.add(new BasicNameValuePair("extendParam", ""));
                urlParameters.add(new BasicNameValuePair("BillNoStatus", "1"));
                urlParameters.add(new BasicNameValuePair("customBusCode", "EFORM9000000048"));//表单名称
                String i8rv = i8Request.PostFormSync("/SUP/CustomPC/save", urlParameters);
                JSONObject i8rvJson = JSON.parseObject(i8rv);
                if (i8rvJson != null && i8rvJson.getString("Status").equals("OK")) {
                    re.put("instanceId", instanceId);
                    re.put("sucess", "Y");;
                    re.put("message", "新增保存成功");
                } else {
                    re.put("instanceId", instanceId);
                    re.put("sucess", "N");
                    if (i8rv.length() == 0) {
                        re.put("message", "新增保存失败，" + "请求i8未获得返回值");
                    } else {
                        re.put("message", "新增保存失败，" + i8rvJson);
                    }
                }
            } else {
                String mstFormData = mstFormData(fbInfo, oldPhId, jfdw, yfdw, fbPhId,mstFormStrModilfy);
                String dtlFormData = dtlFormData(dtl, oldPhId, "modifiedRow");
                /*修改*/
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("otype", "edit"));//单据类型
                urlParameters.add(new BasicNameValuePair("isContinue", "0"));
                urlParameters.add(new BasicNameValuePair("mstform", mstFormData));//主表
                urlParameters.add(new BasicNameValuePair("phidtemplate", ""));
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d", dtlFormData));//明细表
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d_AllData", dtlFormData));
                urlParameters.add(new BasicNameValuePair("extendParam", ""));
                urlParameters.add(new BasicNameValuePair("BillNoStatus", "1"));
                urlParameters.add(new BasicNameValuePair("customBusCode", "EFORM9000000048"));//表单名称
                String i8rv = i8Request.PostFormSync("/SUP/CustomPC/save", urlParameters);
                JSONObject i8rvJson = JSON.parseObject(i8rv);
                if (i8rvJson != null && i8rvJson.getString("Status").equals("OK")) {
                    re.put("instanceId", instanceId);
                    re.put("sucess", "Y");
                    re.put("message", "修改保存成功");
                } else {
                    re.put("instanceId", instanceId);
                    re.put("sucess", "N");
                    if (i8rv.length() == 0) {
                        re.put("message", "修改保存失败，" + "请求i8未获得返回值");
                    } else {
                        re.put("message", "修改保存失败，" + i8rvJson);
                    }
                }
            }
        } catch (Exception ex) {
            re.put("sucess", "N");
            re.put("message", "保存失败,原因:" + ex.getMessage());
            log.error(ex.getMessage());
        }
        return re;
    }

    /**
     * 主表formjson串拼接
     */
    public String mstFormData(JSONObject mst,String oldPhId ,String jfdw, String yfdw ,String fbPhId ,String mstFormStr) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        String defineNum = mst.getString("defineNum");//定标结果单号
        String title = mst.getString("title");//定标结果名称
        String instanceId = mst.getString("instanceId");//唯一标识主键
        String projectNum = mst.getString("projectNum");//项目编号
        String dutyNum = mst.getString("dutyNum");//分包申请编码
        //String bidUnit = mst.getString("bidUnit");//甲方单位
        String purName = mst.getString("purName");//甲方代表
        String purPhone = mst.getString("purPhone");//甲方代表联系方式
        //String supplierCompanyCode = mst.getString("supplierCompanyCode");//乙方单位编码(即中标单位，根据名称匹配)
        String supplierName = mst.getString("supplierName");//乙方代表
        String supplierPhone = mst.getString("supplierPhone");//乙方代表联系方式
        String winTaxAmount = mst.getString("winTaxAmount");//含税金额
        String winNetAmount = mst.getString("winNetAmount");//不含税金额
        String tax_amount = mst.getString("tax_amount");//税额
        String parmentTerms = mst.getString("parmentTerms");//招标约定付款方式
        String settlementTerms = mst.getString("settlementTerms");//招标约定结算方式
        String note = mst.getString("note");//备注

        //通过编码获取对应表phid
        String pcPhId = getPhIdHelper.GetPhIdByCode("project_table", "pc_no", projectNum);
        //合同类型
        String cntTypePhId = getPhIdHelper.GetCodeByCode("pms3_fb_subj_m","bill_type" , "bill_no", dutyNum);
        if(cntTypePhId.equals("1")){
            cntTypePhId = "2";
        }else {
            cntTypePhId = "155201102000001";
        }

        //项目所属组织
        String projCbooPhId = getPhIdHelper.GetCodeByFilter("fg_orglist","phid","phid = (select cat_phid from project_table where pc_no = '"+projectNum+"')");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        map.put("u_ysyjid", instanceId);//云上营家ID
        map.put("bill_dt", df.format(new Date()));//单据日期
        map.put("title", title);//标题
        map.put("phid", oldPhId);//phid（主键）
        map.put("key", oldPhId);//key
        map.put("u_xmbm", projectNum);//项目编码
        map.put("pc", pcPhId);//项目主键
        map.put("u_htlx", cntTypePhId);//合同类型
        map.put("u_dbjgdh", defineNum);//定标结果单号
        map.put("u_fbsqbm", fbPhId);//分包申请编码
        map.put("u_jfdw", jfdw);//甲方单位
        map.put("u_jfdb", purName);//甲方代表
        map.put("u_jfdblxfs", purPhone);//甲方代表联系方式
        map.put("u_yfdw", yfdw);//乙方单位
        map.put("u_yfdb", supplierName);//乙方代表
        map.put("u_yfdblxfs", supplierPhone);//乙方代表联系方式
        map.put("u_jehs", winTaxAmount);//金额（含税）
        map.put("u_jews", winNetAmount);//金额（无税）
        map.put("u_se", tax_amount);//税额
        map.put("u_zz", projCbooPhId);//组织
        map.put("u_zbydfkfs", parmentTerms);//招标约定付款方式
        map.put("u_zbydjsfs", settlementTerms);//招标约定结算方式
        map.put("u_bz", note);//备注
        return entityConverter.SetField(mstFormStr, map);
    }

    /**
     * 明细表表formjson串拼接
     */
    public String dtlFormData(JSONArray dtl ,String oldPhId ,String rowStatus) throws Exception{
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<dtl.size();i++) {
            String lineId = dtl.getJSONObject(i).getString("lineId");//行id
            String headerId = dtl.getJSONObject(i).getString("headerId");//定标头id
            String materialCode = dtl.getJSONObject(i).getString("materialCode");//分项工程编码
            String materialName = dtl.getJSONObject(i).getString("materialName");//分项工程名称
            String uomCode = dtl.getJSONObject(i).getString("uomCode");//单位
            String jobContent = dtl.getJSONObject(i).getString("jobContent");//工作内容
            String supplier = dtl.getJSONObject(i).getString("supplier");//材料设备供应及品牌
            String measurementRules = dtl.getJSONObject(i).getString("measurementRules");//计量规则
            String allottedQuantity = dtl.getJSONObject(i).getString("allottedQuantity");//量
            String netPrice = dtl.getJSONObject(i).getString("netPrice");//不含税单价
            String noTaxArtificialPrice = dtl.getJSONObject(i).getString("noTaxArtificialPrice");//不含税人工费单价
            String taxRate = dtl.getJSONObject(i).getString("taxRate");//税率
            String taxPrice = dtl.getJSONObject(i).getString("taxPrice");//含税单价
            String artificialPrice = dtl.getJSONObject(i).getString("artificialPrice");//含税人工费单价
            String noTaxArtificialAmount = dtl.getJSONObject(i).getString("noTaxArtificialAmount");//不含税人工费金额
            String winNetAmount = dtl.getJSONObject(i).getString("winNetAmount");//不含税金额
            String winTaxAmount = dtl.getJSONObject(i).getString("winTaxAmount");//含税金额
            String artificialAmount = dtl.getJSONObject(i).getString("artificialAmount");//含税人工费
            String quoteRemarks = dtl.getJSONObject(i).getString("quoteRemarks");//备注

            //通过编码获取对应表phid
            String msunit = getPhIdHelper.GetPhIdByCode("msunit", "msunit", uomCode);

            //明细Phid
            String oldDtPhId = getPhIdHelper.GetCodeByFilter("p_form_fbgl_fbdbjg_d","phid", " m_code = '"+oldPhId+"' and lineid = '"+lineId+"'");

            map.put("phid",oldDtPhId);//明细phid
            map.put("key",oldDtPhId);//明细phid
            map.put("m_code",oldPhId);//表头phid

            map.put("u_ysyjmxid",headerId);//定标头ID
            map.put("lineid",lineId);//行号
            map.put("u_gclqdbh",materialCode);//工程量清单编号
            map.put("u_gclqdmc", materialName);//工程量清单名称
            map.put("u_jldw", msunit);//计量单位
            map.put("u_jldw_name", "");//计量单位名称
            map.put("u_qty", allottedQuantity);//数量
            map.put("u_gznr", jobContent);//工作内容
            map.put("u_clgyjpp", supplier);//材料供应及品牌
            map.put("u_jlgz", measurementRules);//计量规则
            map.put("u_djws", netPrice);//单价（无税）
            map.put("u_qzrgfdjws", noTaxArtificialPrice);//其中人工费单价（无税）
            map.put("u_hsws", winNetAmount);//合价（无税）
            map.put("u_qzrgfhjws", noTaxArtificialAmount);//其中人工费合价（无税）
            map.put("u_sl", taxRate);//税率
            map.put("u_djhs", taxPrice);//单价（含税）
            map.put("u_qzrgfdjhs", artificialPrice);//其中人工费单价（含税）
            map.put("u_hshs", winTaxAmount);//合价（含税）
            map.put("u_qzrgfhjhs", artificialAmount);//其中人工费合价（含税）
            map.put("u_bz", quoteRemarks);//备注
            //map.put("u_bz", quoteRemarks);//备注
            list.add(map);
        }
        return entityConverter.SetTableRow(dtlFormStr, list, rowStatus);
    }
}
