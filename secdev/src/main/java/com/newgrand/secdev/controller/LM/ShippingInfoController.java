package com.newgrand.secdev.controller.LM;


import com.newgrand.secdev.domain.LM.ResultModel;
import com.newgrand.secdev.helper.EntityConverter;
import com.newgrand.secdev.helper.IJdbcTemplate;
import com.newgrand.secdev.helper.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newgrand.secdev.helper.I8Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/shippingInfoApi")
@Api("发货信息接收API接口")
public class ShippingInfoController {

    @Autowired
    @Resource(name = "jdbcTemplateOrcle")
    private IJdbcTemplate jdbcTemplate;

    @Autowired
    private I8Request i8Request;

    @Autowired
    private EntityConverter entityConverter;

    @ApiOperation(value = "接收铝模发货信息", notes = "接收铝模发货信息", produces = "application/json")
    @RequestMapping(value = "/shippingInfo", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject syncShippingInfo(@RequestBody String str) throws IOException {
        return getJsonObject(str);
    }

    public JSONObject getJsonObject(@RequestBody String str) throws IOException {
        JSONObject re = new JSONObject();
        re.put("result", "true");
        try {
            JSONObject res = JSON.parseObject(str);
            re.put("code", "1");
            re.put("message", "测试成功");
            log.info(res.toString());

            //项目信息
            JSONObject pro = JSON.parseObject(res.getJSONObject("data").getString("project"));
            //项目合同
            JSONObject cnt = JSON.parseObject(res.getJSONObject("data").getString("projectContract"));
            //发货信息
            JSONObject info = JSON.parseObject(res.getJSONObject("data").getString("sendOrder"));
            //发货明细详情
            //JSONObject dtl = JSON.parseObject(res.getJSONObject("data").getString("sendOrderDetail"));

            //合同id
            String projectContractId = cnt.getString("projectContractId");
            //合同编码
            String projectContractCode = cnt.getString("projectContractCode");

            //项目id
            String projectId = pro.getString("projectId");
            //项目编码
            String projectCode = pro.getString("projectCode");

            //系统时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String dateTime = df.format(new Date());// new Date()为获取当前系统时间


            //取发货信息phid
            String sql = "select phid from p_form_zcgl_fhdxx_m where PC = '" + projectId + "' and U_HTMC = '" + projectContractId + "'";
            String oldPhId = jdbcTemplate.queryForObject(sql, String.class);
            if (oldPhId == null || oldPhId == "") {
                ResultModel back = MainForm(projectId, projectCode, projectContractId, projectContractCode, dateTime, info);
                re.put("code", back.getCode());
                re.put("message", back.getMessage());
                re.put("result", back.getResult());
            } else {
                ResultModel back = DetailForm(oldPhId, info);
                re.put("code", back.getCode());
                re.put("message", back.getMessage());
                re.put("result", back.getResult());
            }
        } catch (Exception ex) {
            re.put("code", "0");
            re.put("message", "保存失败, 原因" + ex.getMessage());
            re.put("result", "false");
            log.error("保存失败,原因" + ex.getMessage());
        }
        return re;
    }

    //@Transactional() (用于起事务，同一事务下)
    public ResultModel MainForm(String projectId, String projectCode, String projectContractId, String projectContractCode, String dateTime, JSONObject info) {
        ResultModel backMsg = new ResultModel();
        try {
            String orderCode = info.getString("orderCode"); //发货单号（对应运输单号）
            String id = info.getString("id");  //铝模无忧系统id
            String sendTime = info.getString("sendTime"); //发货时间
            String carNumber = info.getString("carNumber"); //车牌号

            //雪花算法生成PhId主键
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
            long phId = idWorker.nextId();
            long dphId = idWorker.nextId();
            StringBuilder insertSQL = new StringBuilder();
            insertSQL.append("BEGIN "); //开启事务

            //oracle数据库日期格式处理
            dateTime = "to_date('"+dateTime+"','yyyy-mm-dd hh24:mi:ss')";
            //主表语句拼接
            insertSQL.append("insert into p_form_zcgl_fhdxx_m (PHID,BILL_NO,PC,U_XMBM,U_HTMC,U_HTBM,OCODE,NG_INSERT_DT,NG_UPDATE_DT,NG_RECORD_VER,NG_PHID_BP,NG_PHID_ORIGINAL,NG_ORGID_ORIGINAL,NG_PHID_UI_SCHEME,NG_SV_SEARCH_KEY,NG_SHARE_SIGN)");
            insertSQL.append(" VALUES (");
            insertSQL.append("'" + phId + "',"); //主键
            insertSQL.append("'" + phId + "',"); //单据编码
            insertSQL.append("'" + projectId + "',"); //项目id
            insertSQL.append("'" + projectCode + "',"); //项目编码
            insertSQL.append("'" + projectContractId + "',"); //合同id
            insertSQL.append("'" + projectContractCode + "',"); //合同编码

            insertSQL.append("'438201029000001',"); //所属组织
            //单据插入必填项（系统内置字段，涉及表数据完整）
            insertSQL.append(dateTime + ","); //单据录入时间
            insertSQL.append(dateTime + ","); //单据更新时间
            insertSQL.append("'1',"); //单据版本号
            insertSQL.append("'0',"); //
            insertSQL.append("'0',"); //
            insertSQL.append("'0',"); //
            insertSQL.append("'0',"); //
            insertSQL.append("'10',"); //
            insertSQL.append("'1'"); //
            insertSQL.append(") ;");

            //oracle数据库日期格式处理
            sendTime = "to_date('" + sendTime + "','yyyy-mm-dd hh24:mi:ss')";
            //明细表语句拼接
            insertSQL.append(" insert into p_form_zcgl_fhdxx_d (PHID,M_CODE,LINEID,U_FHDH,U_FHSJ,U_YSDH,U_CPH,U_BZ)");
            insertSQL.append(" VALUES (");
            insertSQL.append("'" + dphId + "',"); //明细表主键
            insertSQL.append("'" + phId + "',"); //主表主键
            insertSQL.append("'0',"); //行号
            insertSQL.append("'" + id + "',"); //铝模无忧系统id
            insertSQL.append(sendTime + ","); //发货时间
            insertSQL.append("'" + orderCode + "',"); //发货单号（对应运输单号）
            insertSQL.append("'" + carNumber + "',"); //车牌号
            insertSQL.append("''"); //备注
            insertSQL.append(") ;");

            insertSQL.append("END; "); //结束事务
            int num = jdbcTemplate.update(insertSQL.toString());
            if (num > 0) {
                backMsg.setResult("true");
                backMsg.setMessage("保存成功");
                backMsg.setCode("1");
            } else {
                backMsg.setResult("false");
                backMsg.setMessage("保存失败，数据库sql语句执行失败");
                backMsg.setCode("0");
            }
        } catch (Exception ex) {
            backMsg.setResult("false");
            backMsg.setMessage("保存失败，异常原因 = " + ex.getMessage());
            backMsg.setCode("0");
            log.error("发货单信息保存失败" + ex.getMessage());
        }
        return backMsg;
    }

    public ResultModel DetailForm(String oldPhId, JSONObject info) {
        ResultModel backMsg = new ResultModel();
        try {
            String orderCode = info.getString("orderCode"); //发货单号（对应运输单号）
            String id = info.getString("id");  //铝模无忧系统id
            String sendTime = info.getString("sendTime"); //发货时间
            String carNumber = info.getString("carNumber"); //车牌号


            //雪花算法生成PhId主键
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
            long dphId = idWorker.nextId();

            //oracle数据库日期格式处理
            sendTime = "to_date('" + sendTime + "','yyyy-mm-dd hh24:mi:ss')";

            StringBuilder insertSQL = new StringBuilder();
            insertSQL.append(" insert into p_form_zcgl_fhdxx_d (PHID,M_CODE,LINEID,U_FHDH,U_FHSJ,U_YSDH,U_CPH,U_BZ)");
            insertSQL.append(" VALUES (");
            insertSQL.append("'" + dphId + "',"); //明细表主键
            insertSQL.append("'" + oldPhId + "',"); //主表主键
            insertSQL.append("'0',"); //行号
            insertSQL.append("'" + id + "',"); //铝模无忧系统id
            insertSQL.append(sendTime + ","); //发货时间
            insertSQL.append("'" + orderCode + "',"); //发货单号（对应运输单号）
            insertSQL.append("'" + carNumber + "',"); //车牌号
            insertSQL.append("''"); //备注
            insertSQL.append(") ");

            int num = jdbcTemplate.update(insertSQL.toString());
            if (num > 0) {
                backMsg.setResult("true");
                backMsg.setMessage("保存成功");
                backMsg.setCode("1");
            } else {
                backMsg.setResult("false");
                backMsg.setMessage("保存失败，数据库sql语句执行失败");
                backMsg.setCode("0");
            }
        } catch (Exception ex) {
            backMsg.setResult("false");
            backMsg.setMessage("保存失败，异常原因 = " + ex.getMessage());
            backMsg.setCode("0");
            log.error("发货单信息保存失败" + ex.getMessage());
        }
        return backMsg;
    }
}
