package com.hzfc.management.yjzx.modules.reports.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 指标
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Data
public class Rank implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer momRank;

    private Integer yoyRank;

    private Integer momLjRank;

    private Integer yoyPjRank;

}
