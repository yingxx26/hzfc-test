package com.hzfc.management.jsbsb.modules.testOnlineTaxiJiJia.dto.map;


import lombok.Data;

import java.util.List;

/**
 * 
 */
@Data
public class Points {
	
	private Location startPoint;
	
	private Location endPoint;
	
	private List<Location> points;
	
	private Integer pointCount ;
	
	
}
