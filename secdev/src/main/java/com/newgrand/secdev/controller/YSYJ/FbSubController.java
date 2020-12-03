package com.newgrand.secdev.controller.YSYJ;

import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class FbSubController {

    @ApiOperation(value = "推送分包申请到云上营家", notes = "推送分包申请到云上营家", produces = "application/json")
    @RequestMapping(value = "/postFbSub", method = RequestMethod.POST)
    public String syncProCount() {
        var sql = "select \n" +
                "t1.phid,t1.bill_no as billNo,t1.bill_title as title,\n" +
                "t1.bill_dt as billDt,t2.project_name as pcName,\n" +
                "t3.item_name as ysfl,t4.type_name as zyfl,\n" +
                "t5.cname as zbEmp,t6.compname as jsdw,\n" +
                "t7.c_name as zbfs,t8.compname as zbdw,\n" +
                "t9.c_name as pbbf,t1.user_jhgq as planDt,\n" +
                "t1.user_zbkzjws as zbkzjWs,t1.user_zbkzjhs as zbkzjHs,\n" +
                "t1.user_jhzbrq as jhzbrq,t10.c_name,\n" +
                "t1.user_fblyjsm as fblyjs,\n" +
                "t1.user_fktk as fktk,\n" +
                "t1.user_jstk as jstk,\n" +
                "t11.cname as jbr,\n" +
                "t12.oname as jbbm\n" +
                "from pms3_fb_subj_m t1\n" +
                "left join project_table t2 on t2.phid = t1.phid_pc\n" +
                "left join bs_data t3 on t3.phid = t1.phid_budget_type\n" +
                "left join wbs_type t4 on t4.phid = t1.user_zyfl\n" +
                "left join hr_epm_main t5 on t5.phid = t1.user_zbfzr\n" +
                "left join FG3_ENTERPRISE t6 on t6.phid = t1.user_jsdw\n" +
                "left join fg_simple_data t7 on t7.phid = t1.user_zbfs\n" +
                "left join FG3_ENTERPRISE t8 on t8.phid = t1.user_zbdw\n" +
                "left join fg_simple_data t9 on t9.phid = t1.user_pbbf\n" +
                "left join fg_simple_data t10 on t10.phid = t1.user_bjfs\n" +
                "left join hr_epm_main t11 on t11.phid = t1.user_jbr\n" +
                "left join fg_orglist t12 on t12.phid = t1.user_jbbm\n" +
                "where t3.oper_type = 'budget_classify'\n";
        var dtlsql = "select t1.phid,t1.pphid,t1.code,t1.cname,\t\n" +
                "t2.bill_name as msunit,t1.origin_qty as originQty,\n" +
                "t1.origin_price as originPrice,t1.vat_price as vatPrice,\n" +
                "t1.user_qzrgfdjws as qzrgfdjws,t1.user_qzrgfdjhs as qzrgfdjhs,\n" +
                "t1.origin_amt as originAmt,t1.character,\n" +
                "t1.user_qzrgfjews as qzrgfjews,t1.user_qzrgfjehs as qzrgfjehs,\n" +
                "t1.Tax_Rate as TaxRate,t1.Tax_Amt as TaxAmt,\n" +
                "t1.user_jlgz as jlgz,t1.user_gznr as gznr,\n" +
                "t1.user_clgyjpp as clgyjpp,t3.cbs_code as cbs,\n" +
                "t1.user_mbcbze as mbcbze,t1.user_mbcbye as mbcbye,\n" +
                "t1.remarks\n" +
                "from pms3_fb_subj_d t1 \n" +
                "left join MSUNIT t2 on t2.phid = t1.phid_unit\n" +
                "left join bd_cbs t3 on t3.phid = t1.phid_cbs";
        return "测试成功";
    }
}
