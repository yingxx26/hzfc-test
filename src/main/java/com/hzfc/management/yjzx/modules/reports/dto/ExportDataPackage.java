package com.hzfc.management.yjzx.modules.reports.dto;

import com.hzfc.management.yjzx.modules.reports.model.ZhiBiaoZzxsjgbdqk;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * word模板分类表
 * </p>
 *
 * @author yxx
 * @since 2020-08-21
 */
@Getter
@Setter
public class ExportDataPackage {


    List<ZhiBiaoZzxsjgbdqkVo> zhiBiaoZzxsjgbdqkList;

    Map rankMap;

    List<Yhcqtj> yhcqtjList;

    List<String> yhbm_everyMonth_month_List;

    List<Integer> yhbm_everyMonth_yhsize_List;

    List<Integer> yhbm_everyMonth_lysize_List;

    List<Double> yhbm_everyMonth_lylv_List;

    List<Double> spfcj_taoshu_zz_list;

    List<Double> spfcj_jj_zz_thisyear_list;

    List<Double> spfcj_jj_zz_lastyear_list;

    List<Double> pzks_mianJi_list;

    List<Double> pzks_taoShu_list;

    List<Double> mianJi_everyMonth_fzz_list;

    List<Double> jj_everyMonth_fzz_list;

    List<Double> pzks_mianJi_fzz_everyMonth_list;

    List<Double> pzys_taoshu_zz_list;

    List<Double> pzys_gongXiaoBi_zz_list;
}
