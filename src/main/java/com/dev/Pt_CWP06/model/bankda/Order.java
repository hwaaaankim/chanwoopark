package com.dev.Pt_CWP06.model.bankda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bankda")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="orderid")
	private String order_id;
	
	@Column(name="buyername")
	private String buyer_name;
	
	@Column(name="buyeremail")
	private String buyer_email;
	
	@Column(name="buyercellphone")
	private String buyer_cellphone;
	
	@Column(name="item")
	private String item;
	
	@Column(name="billingname")
	private String billing_name;
	
	@Column(name="bankaccountno")
	private String bank_account_no;
	
	@Column(name="bankcodename")
	private String bank_code_name;
	
	@Column(name="orderdate")
	private String order_date;
	
	@Column(name="orderpriceamount")
	private String order_price_amount;
	
	@Column(name="sign")
	private Boolean sign;
}
