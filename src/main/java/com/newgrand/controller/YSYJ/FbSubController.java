package com.newgrand.controller.YSYJ;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.domain.BackMsgModel;
import com.newgrand.domain.ResultModel;
import com.newgrand.domain.YSYJ.DBDetailModel;
import com.newgrand.domain.YSYJ.DBMainModel;
import com.newgrand.helper.EntityConverter;
import com.newgrand.helper.IJdbcTemplate;
import com.newgrand.helper.YSYJRequestHelper;
import com.newgrand.helper.YSYJTokenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ysyjApi")
@Api("分包申请API接口")
public class FbSubController {
    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    private IJdbcTemplate jdbcTemplate;

    @Autowired
    private YSYJRequestHelper requestHelper;


    @ApiOperation(value = "推送分包申请到云上营家", notes = "推送分包申请到云上营家", produces = "application/json")
    @RequestMapping(value = "/postFbSub", method = RequestMethod.POST)
    public ResultModel syncProCount(HttpServletRequest request) throws IOException {
        String PhId = request.getParameter("Phid");//唯一标识主键
        ResultModel backmsg = new ResultModel();
        if(StringUtils.isEmpty(PhId)){
            backmsg.setResult("false");
            backmsg.setMessage("推送云上营家失败，原因分包申请主键Phid值为空");
            backmsg.setCode("0");
            return backmsg;
        }
        String mainSQL = MainSQL(PhId);
        RowMapper<DBMainModel> rowMapper = new BeanPropertyRowMapper(DBMainModel.class);
        List<DBMainModel> mainDt = jdbcTemplate.query(mainSQL, rowMapper);
        List<DBDetailModel> detailDt = null;
        JSONObject re = new JSONObject();
        for (int i = 0; i < mainDt.size(); i++) {
            //主表phid (主键)
            String pphid = mainDt.get(i).getPhId();
            String detailSQL = DetailSQL(pphid);
            RowMapper<DBDetailModel> deatilRowMapper = new BeanPropertyRowMapper(DBDetailModel.class);
            detailDt = jdbcTemplate.query(detailSQL, deatilRowMapper);
            re.put("mst",mainDt.get(i));
            re.put("dtl",detailDt);
            JSONObject data = new JSONObject();
            data.put("data",re);
            data.put("operation","create");
            JSONObject dataBosy = new JSONObject();
            dataBosy.put("payload", data.toJSONString());
            backmsg = requestHelper.SendPost(JSONObject.toJSONString(dataBosy));
        }
        return backmsg;
    }

    /**
     * 表头
     *
     * @return
     */
    private String MainSQL(String PhId) {
        //when 1 then '专业分包' when 2 then '劳务分包' end
        var sql = "select " +
                "t1.phid,t1.bill_no as billNo,t1.bill_title as title, " +
                "t1.bill_dt as billDt,t2.project_name as pcName, t2.pc_no as pcNo," +
                "t3.item_name as ysfl,t4.type_no as zyfl, " +
                "t5.cno as zbEmp,t6.compno as jsdw, " +
                "t7.c_no as zbfs,t2.user_zh as zbdw, " +
                "t9.c_no as pbbf,t1.user_jhgq as planDt, " +
                "t1.user_zbkzjws as zbkzjWs,t1.user_zbkzjhs as zbkzjHs, " +
                "t1.user_jhzbrq as jhzbrq,t10.c_no as bjfs, " +
                "t1.user_fblyjsm as fblyjs, " +
                "t1.user_fktk as fktk, " +
                "t1.user_jstk as jstk, " +
                "t11.cno as jbr, " +
                "t12.user_zsjcode as jbbm, " +
                "t13.userno as zdr, " +
                "t1.bill_dt as zdrq, " +
                "t1.bill_type as billType " +
                "from pms3_fb_subj_m t1 " +
                "left join project_table t2 on t2.phid = t1.phid_pc " +
                "left join bs_data t3 on t3.phid = t1.phid_budget_type " +
                "left join wbs_type t4 on t4.phid = t1.user_zyfl  " +
                "left join hr_epm_main t5 on t5.phid = t1.user_zbfzr " +
                "left join FG3_ENTERPRISE t6 on t6.phid = t1.user_jsdw " +
                "left join fg_simple_data t7 on t7.phid = t1.user_zbfs " +
                //"left join FG3_ENTERPRISE t8 on t8.phid = t1.user_zbdw " +
                "left join fg_simple_data t9 on t9.phid = t1.user_pbbf " +
                "left join fg_simple_data t10 on t10.phid = t1.user_bjfs " +
                "left join hr_epm_main t11 on t11.phid = t1.user_jbr " +
                "left join fg_orglist t12 on t12.phid = t1.user_jbbm " +
                "left join fg3_user t13 on t13.phid = t1.user_zdr " +
                "where t3.oper_type = 'budget_classify' " +
                " and t1.phid = '" + PhId + "'";
        return sql;
    }

    /**
     * 表体
     *
     * @param mainPhId 主表主键
     * @return
     */
    private String DetailSQL(String mainPhId) {
        var dtlsql = "select t1.phid,t1.pphid,t1.code,t1.cname, " +
                "t2.msunit as msunit,t1.origin_qty as originQty, " +
                "t1.origin_price as originPrice,t1.vat_price as vatPrice, " +
                "t1.user_qzrgfdjws as qzrgfdjws,t1.user_qzrgfdjhs as qzrgfdjhs, " +
                "t1.origin_amt as originAmt,t1.vat_amt as vatAmt,t1.character, " +
                "t1.user_qzrgfjews as qzrgfjews,t1.user_qzrgfjehs as qzrgfjehs, " +
                "trim(to_char(t1.Tax_Rate * 100,'99999999999999.99')) as TaxRate,t1.Tax_Amt as TaxAmt, " +
                "t1.user_jlgz as jlgz,t1.user_gznr as gznr, " +
                "t1.user_clgyjpp as clgyjpp,t3.cbs_code as cbs, " +
                "t1.user_mbcbze as mbcbze,t1.user_mbcbye as mbcbye, " +
                "t1.remarks," +
                "t1.USER_JJSJKID as jjsjkId " +
                "from pms3_fb_subj_d t1  " +
                "left join MSUNIT t2 on t2.phid = t1.phid_unit " +
                "left join bd_cbs t3 on t3.phid = t1.phid_cbs " +
                "where pphid = '" + mainPhId + "'";
        return dtlsql;
    }
}
