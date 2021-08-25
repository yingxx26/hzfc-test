package com.hzfc.management.wyzj.modules.szwy.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Yxx
 * @since 2021-08-18
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActProcinstVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double id;

    /**
     * 业务实例id
     */
    private Long ywslid;

    /**
     * 业务名称
     */
    private String ywname;

    /**
     * 创建时间
     */
    private String createtime;

    /**
     * 更新时间
     */
    private String updatetime;

    /**
     * 当前环节使用人
     */
    private String currentuser;

    /**
     * 流程定义id
     */
    private Long procdefid;

    /**
     * 当前活动名称
     */
    private String currenttask;


    /**
     * 流程实例id
     */
    private Long procinstid;


    /**
     * 流程定义名称
     */
    private String procdefname;
}
