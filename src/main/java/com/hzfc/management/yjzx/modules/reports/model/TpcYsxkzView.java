package com.hzfc.management.yjzx.modules.reports.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 预售许可证
 * </p>
 *
 * @author Yxx
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TPC_YSXKZ_VIEW")
public class TpcYsxkzView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预售许可证ID
     */
    @TableId("ID")
    private Long id;

    /**
     * 预售许可证CODE
     */
    @TableField("CODE")
    private Long code;

    /**
     * 预销售项目ID
     */
    @TableField("YSXMID")
    private Long ysxmid;

    /**
     * 预销售项名称
     */
    @TableField("YSXMMC")
    private String ysxmmc;

    /**
     * 预售证编号
     */
    @TableField("YSZBH")
    private String yszbh;

    /**
     * 发证机构
     */
    @TableField("FZJG")
    private String fzjg;

    /**
     * 用途性质
     */
    @TableField("YTXZ")
    private String ytxz;

    /**
     * 项目预销售核准总面积
     */
    @TableField("YSHZZMJ")
    private Double yshzzmj;

    /**
     * 发证日期
     */
    @TableField("FZRQ")
    private LocalDateTime fzrq;

    /**
     * 经办人
     */
    @TableField("JBR")
    private String jbr;

    /**
     * 签发日期
     */
    @TableField("QFRQ")
    private LocalDateTime qfrq;

    /**
     * 项目坐落
     */
    @TableField("XMZL")
    private String xmzl;

    /**
     * 开发企业名称
     */
    @TableField("KFQYMC")
    private String kfqymc;

    /**
     * 变更次数
     */
    @TableField("BGCS")
    private Integer bgcs;

    /**
     * 备注
     */
    @TableField("BZ")
    private String bz;

    /**
     * 是否历史标识
     */
    @TableField("LSBZ")
    private Integer lsbz;

    /**
     * 预售项目CODE
     */
    @TableField("YSXMCODE")
    private Long ysxmcode;

    /**
     * 行政区划
     */
    @TableField("XZQH")
    private Integer xzqh;

    /**
     * 创建时间
     */
    @TableField("CJSJ")
    private LocalDateTime cjsj;

    /**
     * 终止时间
     */
    @TableField("ZZSJ")
    private LocalDateTime zzsj;

    /**
     * 预售证字号
     */
    @TableField("YSZBHSTR")
    private String yszbhstr;

    /**
     * 预售证字号-年
     */
    @TableField("YSZBHYEAR")
    private Integer yszbhyear;

    /**
     * 预售证字号-字
     */
    @TableField("YSZBHZI")
    private String yszbhzi;

    /**
     * 预售证字号-第
     */
    @TableField("YSZBHDI")
    private Integer yszbhdi;

    /**
     * 预售证字号-头
     */
    @TableField("YSZBHHEAD")
    private String yszbhhead;

    /**
     * 变更日期
     */
    @TableField("BGRQ")
    private LocalDateTime bgrq;

    /**
     * 领证日期
     */
    @TableField("LZRQ")
    private LocalDateTime lzrq;

    /**
     * 精装修套数
     */
    @TableField("JZXTS")
    private Integer jzxts;

    /**
     * 毛坯套数
     */
    @TableField("MPTS")
    private Integer mpts;

    /**
     * 首次签发日期
     */
    @TableField("SCQFRQ")
    private LocalDateTime scqfrq;

    @TableField("AID")
    private String aid;

    @TableField("FLAG")
    private Long flag;


}
