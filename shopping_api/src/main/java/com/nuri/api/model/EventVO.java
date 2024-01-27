package com.nuri.api.model;

import lombok.Data;

@Data
public class EventVO {
	private int event_idx;
	private String event_name;
	private String event_img;
	private String event_content;
}
