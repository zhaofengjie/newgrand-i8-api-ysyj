package com.newgrand.secdev.controller.AM;


import com.newgrand.secdev.domain.AM.FBCntMModel;

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

@Slf4j
@RestController
@RequestMapping("/fbCntMApi")
@Api("周材租入合同API接口")
public class FBCntMController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    protected JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "推送周材租入合同数据到铝模系统", notes = "推送周材租入合同数据到铝模系统", produces = "application/json")
    @RequestMapping(value = "/postCntM", method = RequestMethod.POST)
    public String syncProCount() {
        var sql = "select \n" +
                "T1.phid,\n" +
                "T2.cname as jBR,\n" +
                "T1.user_htbm as hTBM,\n" +
                "T1.cnt_org_sum_vat_fc as cntMoney,\n" +
                "T1.title,\n" +
                "'' as contractPrice,\n" +
                "T1.user_jbbm as deptId,T3.oname as deptName,\n" +
                "T1.remarks,\n" +
                "T4.compname as recCompName,senemp as recEmp,user_yfdblxfs as recLink,\n" +
                "T5.compname as senCompName,phid_senemp as senEmp,user_jfdblxfs as senLink,\n" +
                "T1.phid_pc as projectId,T6.project_name as projectName,T1.signDt\n" +
                "from pcm3_cnt_m T1\n" +
                "left join hr_epm_main T2 on T2.phid = T1.user_jbr\n" +
                "left join fg_orglist T3 on T3.phid = T1.user_jbbm\n" +
                "left join fg3_enterprise T4 on T4.phid = T1.phid_reccomp\n" +
                "left join fg3_enterprise T5 on T5.phid = T1.phid_sencomp \n" +
                "left join project_table T6 on T6.phid = T1.phid_pc";
        try {
            log.info("推送周材租入合同数据开始");
            RowMapper<FBCntMModel> rowMapper=new BeanPropertyRowMapper(FBCntMModel.class);
            List<FBCntMModel> dbDt= jdbcTemplate.query(sql, rowMapper);
            for(int i = 0;i<dbDt.size();i++) {
                var id = dbDt.get(i).getPhid();//主键
                var agent = dbDt.get(i).getJBR();//经办人
                var contractCode = dbDt.get(i).getHTBM();//合同编码
                var contractMoney = dbDt.get(i).getCntMoney();//合同金额
                var contractName = dbDt.get(i).getTitle();//合同标题
                var contractPrice = dbDt.get(i).getContractPrice();//合同单据
                var contractSignDeptId = dbDt.get(i).getDeptId();//合同签订部门id
                var contractSignDeptName = dbDt.get(i).getDeptName();//合同签订部门
                var description = dbDt.get(i).getRemarks();//描述
                var estimateAllBetonArea = dbDt.get(i).getEstimateAllBetonArea();//预估总混凝土接触面积(㎡)
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "测试成功";
    }
}
