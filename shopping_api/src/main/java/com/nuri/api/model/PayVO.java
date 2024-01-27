package com.nuri.api.model;

import lombok.Data;

@Data
public class PayVO {
//	user_idx, cate_name, cate_img, cate_price, pay_date
	
	private int pay_idx;
	private String user_id;
	private String cate_name;
	private String cate_img;
	private int cate_price;
	private String pay_date;
	private int cate_idx;
}
