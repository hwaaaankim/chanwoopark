package com.dev.Pt_CWP06.model.bankda;


import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Orders {
	
	private String order_id;
	private String buyer_name;
	private String buyer_email;
	private String buyer_cellphone;
	private List<Map<String, Object>> item;
	private String billing_name;
	private String bank_account_no;
	private String bank_code_name;
	private String order_date;
	private String order_price_amount;
}
