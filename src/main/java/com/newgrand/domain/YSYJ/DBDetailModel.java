package com.newgrand.domain.YSYJ;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBDetailModel {

    /**
     * 明细主键
     */
    @JSONField(name = "phid")
    private String phId;

    /**
     * 单据主键
     */
    @JSONField(name = "pPhId")
    private String pPhId;

    /**
     * 清单编码
     */
    @JSONField(name = "code")
    private String code;

    /**
     * 工程量清单名称
     */
    @JSONField(name = "cname")
    private String cname;

    /**
     * 计量单位
     */
    @JSONField(name = "msuint")
    private String msuint;

    /**
     * 数量
     */
    @JSONField(name = "originQty")
    private String originQty;

    /**
     * 综合单价（无税）
     */
    @JSONField(name = "originPrice")
    private String originPrice;

    /**
     * 综合单价（含税）
     */
    @JSONField(name = "vatPrice")
    private String vatPrice;

    /**
     * 其中人工费单价(无税)
     */
    @JSONField(name = "qzrgfdjws")
    private String qzrgfdjws;

    /**
     * 其中人工费单价(含税)
     */
    @JSONField(name = "qzrgfdjhs")
    private String qzrgfdjhs;

    /**
     * 金额（无税）
     */
    @JSONField(name = "originAmt")
    private String originAmt;

    /**
     * 金额（含税）
     */
    @JSONField(name = "vatAmt")
    private String vatAmt;

    /**
     * 清单特征
     */
    @JSONField(name = "character")
    private String character;

    /**
     * 清单来源
     */
    @JSONField(name = "qdml")
    private String qdml;

    /**
     * 其中人工费金额(无税)
     */
    @JSONField(name = "qzrgfjews")
    private String qzrgfjews;

    /**
     * 其中人工费金额(含税)
     */
    @JSONField(name = "qzrgfjehs")
    private String qzrgfjehs;

    /**
     * 税率
     */
    @JSONField(name = "TaxRate")
    private String TaxRate;

    /**
     * 税额
     */
    @JSONField(name = "TaxAmt")
    private String TaxAmt;

    /**
     * 计量规则
     */
    @JSONField(name = "jlgz")
    private String jlgz;

    /**
     * 工作内容
     */
    @JSONField(name = "gznr")
    private String gznr;

    /**
     * 材料供应及品牌
     */
    @JSONField(name = "clgyjpp")
    private String clgyjpp;

    /**
     * 成本科目
     */
    @JSONField(name = "cbs")
    private String cbs;

    /**
     * 目标成本总额
     */
    @JSONField(name = "mbcbze")
    private String mbcbze;

    /**
     * 目标成本余额
     */
    @JSONField(name = "mbcbye")
    private String mbcbye;

    /**
     * 备注
     */
    @JSONField(name = "remarks")
    private String remarks;
}
