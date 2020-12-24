package com.newgrand.domain.YSYJ;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DBMainModel {

    /**
     * 单据主键
     */
    @JSONField(name = "phId")
    private String phId;

    /**
     * 单据编码
     */
    @JSONField(name = "billNo")
    private String billNo;

    /**
     * 单据名称
     */
    @JSONField(name = "title")
    private String title;
    /**
     * 单据日期
     */
    @JSONField(name = "billDt")
    private String billDt;

    /**
     * 项目编码
     */
    @JSONField(name = "pcNo")
    private String pcNo;

    /**
     * 项目名称
     */
    @JSONField(name = "pcName")
    private String pcName;

    /**
     * 预算分类
     */
    @JSONField(name = "ysfl")
    private String ysfl;

    /**
     * 专业分类
     */
    @JSONField(name = "zyfl")
    private String zyfl;

    /**
     * 招标负责人
     */
    @JSONField(name = "zbEmp")
    private String zbEmp;

    /**
     * 建设单位（业主）
     */
    @JSONField(name = "jsdw")
    private String jsdw;

    /**
     * 招标方式
     */
    @JSONField(name = "zbfs")
    private String zbfs;

    /**
     * 招标单位
     */
    @JSONField(name = "zbdw")
    private String zbdw;

    /**
     * 评标方法
     */
    @JSONField(name = "pbbf")
    private String pbbf;

    /**
     * 计划工期（日历天）
     */
    @JSONField(name = "planDt")
    private String planDt;

    /**
     * 招标控制价（无税）
     */
    @JSONField(name = "zbkzjWs")
    private String zbkzjWs;

    /**
     * 招标控制价
     */
    @JSONField(name = "zbkzjHs")
    private String zbkzjHs;

    /**
     * 计划招标日期（日历天）
     */
    @JSONField(name = "jhzbrq")
    private String jhzbrq;

    /**
     * 报价方式
     */
    @JSONField(name = "bjfs")
    private String bjfs;

    /**
     * 分包理由及说明
     */
    @JSONField(name = "fblyjs")
    private String fblyjs;

    /**
     * 付款条款
     */
    @JSONField(name = "fktk")
    private String fktk;

    /**
     * 结算条款
     */
    @JSONField(name = "jstk")
    private String jstk;

    /**
     * 经办人
     */
    @JSONField(name = "jbr")
    private String jbr;

    /**
     * 经办部门
     */
    @JSONField(name = "dept")
    private String dept;
}

