package com.dev.Pt_CWP06.model.certification;

import lombok.Data;

@Data	
public class RequestConfig {

	private String req_tx="";

	private String site_cd="";
	private String ordr_idxx="";

	private String year="";
	private String month="";
	private String day="";
	private String user_name="";
	private String sex_code="";
	private String local_code="";

	private String web_siteid="";
	private String web_siteid_hashYN="";
	private String cert_able_yn="";

	private String up_hash="";
}
