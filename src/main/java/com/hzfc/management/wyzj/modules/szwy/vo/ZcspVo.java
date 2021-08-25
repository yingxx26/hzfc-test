package com.hzfc.management.wyzj.modules.szwy.vo;

import com.hzfc.management.wyzj.modules.szwy.model.ActProcinst;
import com.hzfc.management.wyzj.modules.szwy.model.TpqWyqygx;
import com.hzfc.management.wyzj.modules.szwy.model.TpqZcspb;
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
public class ZcspVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private ActProcinstVo actProcinstVo;

    private TpqWyqygxVo tpqWyqygxVo;

    private TpqZcspbVo tpqZcspbVo;
}
