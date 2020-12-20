package com.newgrand.secdev.controller.YSYJ;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.helper.EntityConverter;
import com.newgrand.secdev.helper.GetPhIdByCode;
import com.newgrand.secdev.helper.I8Request;
import com.newgrand.secdev.helper.IJdbcTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dbjgApi")
@Api("定标结果API接口")
public class FBDBController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected IJdbcTemplate ijdbcTemplate;

    @Autowired
    private I8Request i8Request;

    @Autowired
    private EntityConverter entityConverter;

    private static final String mstFormStr = "{\"form\":{\"key\":\"phid\",\"newRow\":{\"pc\":\"\",\"u_xmbm\":\"\",\"u_htlx\":\"\",\"u_dbjgdh\":\"\",\"u_fbsqbm\":\"\",\"u_jfdw\":\"\",\"u_jfdb\":\"\",\"u_jfdblxfs\":\"\",\"u_yfdw\":\"\",\"u_yfdb\":\"\",\"u_yfdblxfs\":\"\",\"u_jehs\":\"\",\"u_jews\":\"\",\"u_se\":\"\",\"u_zz\":\"\",\"u_zbydfkfs\":\"\",\"u_zbydjsfs\":\"\",\"u_bz\":\"\",\"phid\":\"543201207000001\",\"ocode\":\"438201029000001\",\"code\":\"\",\"creator\":\"472201202000002\",\"editor\":\"472201202000002\",\"ng_insert_dt\":\"2020-12-07 14:59:19\",\"ng_update_dt\":\"2020-12-07 14:59:19\",\"ng_record_ver\":\"1\",\"cur_orgid\":\"438201029000001\",\"ng_phid_org\":\"438201029000001\",\"ng_phid_cu\":\"438201029000001\",\"ng_phid_bp\":\"0\",\"ng_phid_original\":\"0\",\"ng_orgid_original\":\"0\",\"ng_phid_ui_scheme\":\"0\",\"ng_sv_search_key\":\"10\",\"ng_sd_search_key\":\"438201029000001\",\"ng_share_sign\":\"1\",\"is_wf\":\"0\",\"ischeck\":\"0\",\"checkpsn\":\"\",\"blobdocid\":\"\",\"tr_date\":\"\",\"voucher_flag\":\"\",\"tr_num\":\"\",\"tr_type\":\"\",\"da_flg\":\"\",\"asr_flg\":\"\",\"bill_no\":\"202012070001\",\"s_task_flg\":\"\",\"phid_schemeid\":\"\",\"imp_info\":\"\",\"key\":\"543201207000001\"}}}";
    private static final String dtlFormStr = "[{\"row\":{\"u_gclqdbh\":\"21341234\",\"u_gclqdmc\":\"21341234\",\"u_jldw\":\"191518180204544\",\"u_jldw_name\":\"吨\",\"u_qty\":0,\"u_gznr\":\"\",\"u_clgyjpp\":\"\",\"u_jlgz\":\"\",\"u_djws\":0,\"u_qzrgfdjws\":0,\"u_hsws\":0,\"u_qzrgfhjws\":0,\"u_sl\":0,\"u_djhs\":0,\"u_qzrgfdjhs\":0,\"u_hjhs\":0,\"u_qzrgfhjhs\":0,\"u_bz\":\"\",\"phid\":\"\",\"m_code\":\"\",\"lineid\":0,\"code\":\"\",\"rel_key1\":\"\",\"phid_itemdata\":\"\",\"phid_resbs\":\"\",\"res_propertys\":\"\",\"s_groupfield\":\"\",\"s_tree_id\":\"\",\"s_tree_pid\":\"\",\"imp_info\":\"\",\"key\":null}}]";

    @ApiOperation(value = "接收分包定标结果", notes = "接收分包定标结果", produces = "application/json")
    @RequestMapping(value = "/dbInfo", method = RequestMethod.POST)
    public @ResponseBody JSONObject syncFBDBJGInfo(@RequestBody String str) {
        JSONObject re = new JSONObject();
        try {
            JSONObject fbInfo = JSON.parseObject(str);
            log.info(fbInfo.toString());

            JSONObject mst = fbInfo.getJSONObject("mst");
            String u_dbjgdh = mst.getString("u_dbjgdh");
            String mstFormData = mstFormData(mst);
            String dtlFormData = dtlFormData(fbInfo.getJSONArray("dtl"));
            //查询发货单据phid
            String sql = "select phid from p_form_zcgl_fhdxx_m where ";
            String oldPhId = ijdbcTemplate.queryForObject(sql,String.class);
            if(oldPhId == "" || oldPhId == null)
            {
                /*新增方法*/
                List<NameValuePair> urlParameters=new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("otype", "add"));//单据类型
                urlParameters.add(new BasicNameValuePair("isContinue", "0"));
                urlParameters.add(new BasicNameValuePair("mstform", mstFormData));//主表
                urlParameters.add(new BasicNameValuePair("phidtemplate", ""));
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d", dtlFormData));//明细表
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d_AllData", dtlFormData));
                urlParameters.add(new BasicNameValuePair("extendParam", ""));
                urlParameters.add(new BasicNameValuePair("BillNoStatus", ""));
                urlParameters.add(new BasicNameValuePair("customBusCode", "EFORM9000000048"));//表单名称
                String i8rv = i8Request.PostFormSync("/SUP/CustomPC/save",urlParameters);
                JSONObject i8rvJson = JSON.parseObject(i8rv);
                if(i8rvJson!=null&&i8rvJson.getString("Status").equals("OK")){
                    re.put("result", "true");
                    re.put("code", "1");
                    re.put("message", "新增保存成功");
                }
                else {
                    re.put("result", "false");
                    re.put("code", "0");
                    re.put("message", "新增保存失败");
                }
            }
            else
            {
                /*修改*/
                List<NameValuePair> urlParameters =new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("otype", "add"));//单据类型
                urlParameters.add(new BasicNameValuePair("isContinue", "0"));
                urlParameters.add(new BasicNameValuePair("mstform", mstFormData));//主表
                urlParameters.add(new BasicNameValuePair("phidtemplate", ""));
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d", dtlFormData));//明细表
                urlParameters.add(new BasicNameValuePair("p_form_fbgl_fbdbjg_d_AllData", dtlFormData));
                urlParameters.add(new BasicNameValuePair("extendParam", ""));
                urlParameters.add(new BasicNameValuePair("BillNoStatus", ""));
                urlParameters.add(new BasicNameValuePair("customBusCode", "EFORM9000000048"));//表单名称
                String i8rv = i8Request.PostFormSync("/SUP/CustomPC/save",urlParameters);
                JSONObject i8rvJson = JSON.parseObject(i8rv);
                if(i8rvJson!=null&&i8rvJson.getString("Status").equals("OK")){
                    re.put("result", "true");
                    re.put("code", "1");
                    re.put("message", "修改保存成功");
                }
                else {
                    re.put("result", "false");
                    re.put("code", "0");
                    re.put("message", "修改保存失败");
                }
            }
        } catch (Exception ex) {
            re.put("result", "false");
            re.put("code", "0");
            re.put("message", "保存失败,原因:" + ex.getMessage());
            log.error(ex.getMessage());
        }
        return re;
    }

    /**
     * 主表formjson串拼接
     */
    public String mstFormData(JSONObject mst) {
        GetPhIdByCode phIdByCode = new GetPhIdByCode();
        HashMap<String, Object> map = new HashMap<String, Object>();

        String sql = "select phid from project_table where pc_no = '" + mst.getString("pcNo") + "'";
        String pcPhId = ijdbcTemplate.queryForObject(sql, String.class);

        //通过编码获取对应表phid
        //String pcPhId = phIdByCode.GetPhIdByCode("projet_table","pc_no",mst.getString("pcNo"));

        map.put("u_xmbm", mst.getString("pcNo"));//项目编码
        map.put("pc", pcPhId);//项目主键
        map.put("u_htlx", "");//合同类型
        map.put("u_dbjgdh", "");//定标结果单号
        map.put("u_fbsqbm", "");//分包申请编码
        map.put("u_jfdw", "");//甲方单位
        map.put("u_jfdb", "");//甲方代表
        map.put("u_jfdblxfs", "");//甲方代表联系方式
        map.put("u_yfdw", "");//乙方单位
        map.put("u_yfdb", "");//乙方代表
        map.put("u_yfdblxfs", "");//乙方代表联系方式
        map.put("u_jehs", "");//金额（含税）
        map.put("u_jews", "");//金额（无税）
        map.put("u_se", "");//税额
        map.put("u_zz", "");//组织
        map.put("u_zbydfkfs", "");//招标约定付款方式
        map.put("u_zbydjsfs", "");//招标约定结算方式
        map.put("u_bz", "");//备注
        return entityConverter.SetField(mstFormStr, map);
    }

    /**
     * 明细表表formjson串拼接
     */
    public String dtlFormData(JSONArray dtl) throws Exception{
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<dtl.size();i++) {
            map.put("lineid",i);//行号
            map.put("u_gclqdbh","");//工程量清单编号
            map.put("u_gclqdmc", "");//工程量清单名称
            map.put("u_jldw", "");//计量单位
            map.put("u_jldw_name", "");//计量单位名称
            map.put("u_qty", "");//数量
            map.put("u_gznr", "");//工作内容
            map.put("u_clgyjpp", "");//材料供应及品牌
            map.put("u_jlgz", "");//计量规则
            map.put("u_djws", "");//单价（无税）
            map.put("u_qzrgfdjws", "");//其中人工费单价（无税）
            map.put("u_hsws", "");//合价（无税）
            map.put("u_qzrgfhjws", "");//其中人工费合价（无税）
            map.put("u_sl", "");//税率
            map.put("u_djhs", "");//单价（含税）
            map.put("u_qzrgfdjhs", "");//其中人工费单价（含税）
            map.put("u_hshs", "");//合价（含税）
            map.put("u_qzrgfhjhs", "");//其中人工费合价（含税）
            map.put("u_bz", "");//备注
            list.add(map);
        }
        return entityConverter.SetTableRow(dtlFormStr, list);
    }
}
