package com.dev.Pt_CWP06.model.certification;

import lombok.Data;

@Data
public class ResponseConfig {

	private String site_cd       = "";
	private String ordr_idxx     = "";
    
	private String cert_no       = "";
	private String enc_cert_data2 = "";
	private String enc_info      = "";
	private String enc_data      = "";
	private String req_tx        = "";
    
	private String tran_cd       = "";
	private String res_cd        = "";
	private String res_msg       = "";

	private String dn_hash       = "";
}

