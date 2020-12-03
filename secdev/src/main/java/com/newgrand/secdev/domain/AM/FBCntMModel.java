package com.newgrand.secdev.domain.AM;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FBCntMModel {
    /**
     * 主键
     */
    @JSONField(name = "id")
    private String phid;

    /**
     * 经办人（页面 user_jbr）
     */
    @JSONField(name = "agent")
    private String jBR;//user_jbr

    /**
     * 合同编码（页面 user_htbm）
     */
    @JSONField(name = "contractCode")
    private String hTBM;

    /**
     * 合同金额 （页面 CntOrgSumVatFc）
     */
    @JSONField(name = "contractMoney")
    private String cntMoney;

    /**
     * 合同名称（页面 Title）
     */
    @JSONField(name = "contractName")
    private String title;

    /**
     * 合同单价(业务没有该单价)
     */
    @JSONField(name = "contractPrice")
    private String contractPrice;

    /**
     * 合同签订部门id（页面 user_jbbm）
     */
    @JSONField(name = "contractSignDeptId")
    private String deptId;

    /**
     * 合同签订部门（页面 user_jbbmname）
     */
    @JSONField(name = "contractSignDeptName")
    private String deptName;

    /**
     * 描述（页面 Remarks）
     */
    @JSONField(name = "description")
    private String remarks;

    /**
     * 预估总混凝土接触面积(㎡)
     */
    @JSONField(name = "estimateAllBetonArea")
    private String estimateAllBetonArea;

    /**
     * 预估模板使⽤⾯积(㎡)
     */
    @JSONField(name = "estimateArea")
    private String estimateArea;

    /**
     * 模板预计使用层数
     */
    @JSONField(name = "estimateTemplateTier")
    private String estimateTemplateTier;

    /**
     * 乙方名称（页面 PhidSenComp）
     */
    @JSONField(name = "partA")
    private String senCompName;

    /**
     * 乙方联系人（页面 PhidSenEmp）
     */
    @JSONField(name = "partAContacts")
    private String senEmp;

    /**
     * 乙方联系人电话（页面 user_yfdblxfs）
     */
    @JSONField(name = "partAContactsPhone")
    private String senLink;

    /**
     * 甲方名称（页面 PhIdRecComp）
     */
    @JSONField(name = "partB")
    private String recComp;

    /**
     * 甲方联系人（页面 SenEmp）
     */
    @JSONField(name = "partBContacts")
    private String recEmp;

    /**
     * 甲方联系人电话（页面 user_jfdblxfs）
     */
    @JSONField(name = "partBContactsPhone")
    private String recLink;

    /**
     * 项目id(页面 PhidPc)
     */
    @JSONField(name = "projectId")
    private String projectId;

    /**
     * 项目名称
     */
    @JSONField(name = "projectName")
    private String projectName;

    /**
     * 合同签订日期(页面 SignDt)
     */
    @JSONField(name = "signDate")
    private String signDt;
}
