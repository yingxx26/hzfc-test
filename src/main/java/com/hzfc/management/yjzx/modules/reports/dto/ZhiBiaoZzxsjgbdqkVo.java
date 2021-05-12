package com.hzfc.management.yjzx.modules.reports.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 指标
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
public class ZhiBiaoZzxsjgbdqkVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String city;

    private Double mom;

    private Double yoy;

    private Double momLj;

    private Double yoyPj;

    private Double fixedbase;

}
