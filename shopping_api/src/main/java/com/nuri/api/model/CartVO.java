package com.nuri.api.model;

import lombok.Data;

@Data
public class CartVO {
	private int cart_idx;
	private String user_id;
	private String cart_name;
	private String cart_img;
	private int cart_price;
	private int cate_idx;
}
