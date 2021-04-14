package com.hzfc.management.yjzx.utils.wordutils.mergeCell2;


import com.deepoove.poi.el.Name;

import java.util.List;
import java.util.Map;

public class PaymentData2 {
	
    @Name("detail_table")
    private DetailData2 detailTable;
    @Name("typeLists")
    private List<Map<String,Object>> typeLists;//所属大类统计列表
    private String total;
    public void setDetailTable(DetailData2 detailTable) {
        this.detailTable = detailTable;
    }

    public DetailData2 getDetailTable() {
        return this.detailTable;
    }

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Map<String, Object>> getTypeLists() {
		return typeLists;
	}

	public void setTypeLists(List<Map<String, Object>> typeLists) {
		this.typeLists = typeLists;
	}
	
}
