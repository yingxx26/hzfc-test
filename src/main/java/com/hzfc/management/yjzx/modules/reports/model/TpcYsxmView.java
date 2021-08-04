package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Yxx
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TPC_YSXM_VIEW")
public class TpcYsxmView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    @TableField("CODE")
    private Long code;

    @TableField("YWSLID")
    private Long ywslid;

    @TableField("SCYSXMID")
    private Long scysxmid;

    @TableField("YSXMMC")
    private String ysxmmc;

    @TableField("FDCXMID")
    private Long fdcxmid;

    @TableField("XMQC")
    private String xmqc;

    @TableField("XMZL")
    private String xmzl;

    @TableField("YTXZ")
    private String ytxz;

    @TableField("YSHZZMJ")
    private Double yshzzmj;

    @TableField("FZJG")
    private String fzjg;

    @TableField("QPJG")
    private String qpjg;

    @TableField("SJTZQTPW")
    private String sjtzqtpw;

    @TableField("YWWCJCGC")
    private String ywwcjcgc;

    @TableField("CDJWTRZJ")
    private Double cdjwtrzj;

    @TableField("QFRQ")
    private LocalDateTime qfrq;

    @TableField("STBABZ")
    private Integer stbabz;

    @TableField("SFWBGXM")
    private Integer sfwbgxm;

    @TableField("YSXZ")
    private Integer ysxz;

    @TableField("SGXKZBH")
    private String sgxkzbh;

    @TableField("TDSYXKZBH")
    private String tdsyxkzbh;

    @TableField("GHXKZBH")
    private String ghxkzbh;

    @TableField("STBAZH")
    private String stbazh;

    @TableField("KFQYID")
    private Long kfqyid;

    @TableField("LSBZ")
    private Integer lsbz;

    @TableField("CJSJ")
    private LocalDateTime cjsj;

    @TableField("ZZSJ")
    private LocalDateTime zzsj;

    @TableField("KFQYCODE")
    private Long kfqycode;

    @TableField("FDCXMCODE")
    private Long fdcxmcode;

    @TableField("XZQH")
    private Double xzqh;

    @TableField("LPMC")
    private String lpmc;

    @TableField("FDCXMMC")
    private String fdcxmmc;

    @TableField("DQZT")
    private Integer dqzt;

    @TableField("STHZRQ")
    private LocalDateTime sthzrq;

    @TableField("SGXKZBZ")
    private String sgxkzbz;

    @TableField("TDSYZBZ")
    private String tdsyzbz;

    @TableField("GHXKZBZ")
    private String ghxkzbz;

    @TableField("XMMS")
    private String xmms;

    @TableField("XMBZ")
    private String xmbz;

    @TableField("XMBH")
    private String xmbh;

    @TableField("KPRQ")
    private LocalDateTime kprq;

    @TableField("AID")
    private String aid;

    @TableField("FLAG")
    private Double flag;


}
