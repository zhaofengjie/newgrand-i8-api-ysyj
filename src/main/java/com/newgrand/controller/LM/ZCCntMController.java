package com.newgrand.controller.LM;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.newgrand.domain.LM.ResultModel;
import com.newgrand.domain.LM.ZCCntMModel;

import com.newgrand.helper.LMRequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lmApi")
@Api("周材租入合同推送API接口")
public class ZCCntMController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LMRequestHelper requestHelp;

    @ApiOperation(value = "推送周材租入合同数据到铝模系统", notes = "推送周材租入合同数据到铝模系统", produces = "application/json")
    @RequestMapping(value = "/postZCCntM", method = RequestMethod.POST)
    public @ResponseBody JSONObject syncFBCntInfo(@RequestBody String str) throws IOException {
        return sendFBInfo(str);
    }

    public JSONObject sendFBInfo(@RequestBody String str) throws IOException {
        JSONObject res = new JSONObject();
        JSONObject obj = JSON.parseObject(str);
        //合同类型为周材租入的合同
        //select phid from PCM3_CNT_TYPE where cnt_mode = '11'
        //过滤条件
        String filter = "";
        String phId = obj.getString("phId");
        if(!StringUtils.isEmpty(phId)){
            filter = " and T1.phid = '" + phId + "'";
        }

        var sql = "select \n" +
                "T1.phid,\n" +
                "T2.cname as jBR,\n" +
                "T1.bill_no as hTBM,\n" +
                "T1.cnt_org_sum_vat_fc as cntMoney,\n" +
                "T1.title,\n" +
                "'' as contractPrice,\n" +
                "T1.user_jbbm as deptId,T3.oname as deptName,\n" +
                "T1.remarks,\n" +
                "T4.compname as recComp,senemp as recEmp,user_yfdblxfs as recLink,\n" +
                "T5.compname as senComp,phid_senemp as senEmp,user_jfdblxfs as senLink,\n" +
                "T6.pc_no as projectId,T6.project_name as projectName,T1.signDt,\n" +
                "T1.user_ygmbsymj as estimateArea,\n" +
                "T1.user_ygzhntjcmj as estimateAllBetonArea,\n" +
                "T1.user_mbyjsycs as estimateTemplateTier\n" +
                "from pcm3_cnt_m T1\n" +
                "left join hr_epm_main T2 on T2.phid = T1.user_jbr\n" +
                "left join fg_orglist T3 on T3.phid = T1.user_jbbm\n" +
                "left join fg3_enterprise T4 on T4.phid = T1.phid_reccomp\n" +
                "left join fg3_enterprise T5 on T5.phid = T1.phid_sencomp \n" +
                "left join project_table T6 on T6.phid = T1.phid_pc\n" +
                "where T1.bill_type = '11' and (T1.user_tblmflag is null or  T1.user_tblmflag  = '0')" +
                " " + filter;
        //user_tblmflag 为空 或者 为 0, 为未同步或者同步失败状态
        try {
            log.info("推送周材租入合同数据开始");
            RowMapper<ZCCntMModel> rowMapper=new BeanPropertyRowMapper(ZCCntMModel.class);
            List<ZCCntMModel> dbDt= jdbcTemplate.query(sql, rowMapper);
            JSONObject re = new JSONObject();
            for(int i = 0;i<dbDt.size();i++) {
                //Map<String, Object> params = null;
                ZCCntMModel fbModel = new ZCCntMModel();
                String A = JSONObject.toJSONString(fbModel);
                String mainPhId = dbDt.get(i).getPhid();//主键
                fbModel.setPhid(mainPhId);
                fbModel.setJBR(dbDt.get(i).getJBR());//经办人
                fbModel.setHTBM(dbDt.get(i).getHTBM());//合同编码
                fbModel.setCntMoney(dbDt.get(i).getCntMoney());//合同金额
                fbModel.setTitle(dbDt.get(i).getTitle());//合同标题
                fbModel.setContractPrice(dbDt.get(i).getContractPrice());//合同单据
                fbModel.setDeptId(dbDt.get(i).getDeptId());//合同签订部门id
                fbModel.setDeptName(dbDt.get(i).getDeptName());//合同签订部门
                fbModel.setRemarks(dbDt.get(i).getRemarks());//描述
                fbModel.setEstimateAllBetonArea(dbDt.get(i).getEstimateAllBetonArea());//预估总混凝土接触面积(㎡)
                fbModel.setEstimateArea(dbDt.get(i).getEstimateArea());//预估模板使⽤⾯积(㎡)
                fbModel.setEstimateTemplateTier(dbDt.get(i).getEstimateTemplateTier());//模板预计使用层数
                fbModel.setRecComp(dbDt.get(i).getRecComp());//甲方名称
                fbModel.setRecEmp(dbDt.get(i).getRecEmp());//甲方联系人
                fbModel.setRecLink(dbDt.get(i).getRecLink());//甲方联系人电话
                fbModel.setSenComp(dbDt.get(i).getSenComp());//乙方名称
                fbModel.setSenEmp(dbDt.get(i).getSenEmp());//乙方联系人
                fbModel.setSenLink(dbDt.get(i).getSenLink());//乙方联系人电话
                fbModel.setProjectId(dbDt.get(i).getProjectId());//项目id(对应我们系统中项目项目编码)
                fbModel.setProjectName(dbDt.get(i).getProjectName());//项目id
                fbModel.setSignDt(dbDt.get(i).getSignDt());//合同签订日期
                //params.put("body",fbModel);
                //获取 access_token
                //推送数据 funcName:合同管理接口路由地址
                ResultModel back = requestHelp.SendPost(JSONObject.toJSONString(fbModel, SerializerFeature.WriteMapNullValue),"/lmwy-kebao/api/project/projectContract/insertContract");
                res.put("message",back.getMessage());
                res.put("code",back.getCode());
                res.put("result",back.getResult());
                if(back.getResult() == "true"){
                    //标识推送成功
                    jdbcTemplate.update("update pcm3_cnt_m set user_tblmflag = '1' where phid = '"+mainPhId+"'");
                }else {
                    //标识未推送成功
                    jdbcTemplate.update("update pcm3_cnt_m set user_tblmflag = '0' where phid = '"+mainPhId+"'");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            res.put("message", "推送失败，原因：" + ex.getMessage());
            res.put("code", "0");
            res.put("result", "false");
        }
        return res;
    }
}
