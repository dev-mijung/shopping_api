package com.nuri.api.model;

import lombok.Data;

@Data
public class UserVO {
	private String user_id;
	private String user_name;
	private String user_passwd;
	private String user_tel;
	private String user_address;
	private String user_zip;
}
