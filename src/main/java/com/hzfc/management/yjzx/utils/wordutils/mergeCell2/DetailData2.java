package com.hzfc.management.yjzx.utils.wordutils.mergeCell2;

import com.deepoove.poi.data.RowRenderData;

import java.util.List;
import java.util.Map;

public class DetailData2 {
	
    
    // 商品订单详情列表数据
    private List<RowRenderData> plists;
    
    // 二级分类分组统计商品个数数据
    private List<Map<String,Object>> tlists;
    

	public List<RowRenderData> getPlists() {
		return plists;
	}

	public void setPlists(List<RowRenderData> plists) {
		this.plists = plists;
	}

	public List<Map<String,Object>> getTlists() {
		return tlists;
	}

	public void setTlists(List<Map<String,Object>> tlists) {
		this.tlists = tlists;
	}


	
}
