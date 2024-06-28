package com.dev.Pt_CWP06.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.certification.CertConfig;
import com.dev.Pt_CWP06.model.certification.RequestConfig;
import com.dev.Pt_CWP06.model.certification.ResponseConfig;
import com.dev.Pt_CWP06.repository.MemberRepository;
import com.dev.Pt_CWP06.service.MemberService;
import com.dev.Pt_CWP06.smsService.IdSMSService;
import com.dev.Pt_CWP06.smsService.PwSMSService;

import kr.co.kcp.CT_CLI;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/certification")
@Slf4j
public class CertificationController {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PwSMSService pwsmsService;
	
	@Autowired
	IdSMSService idsmsService;
	
	public String f_get_parm_str(String val) {
		if (val == null)
			val = "";
		return val;
	}

	public String f_get_parm_int(String val) {
		String ret_val = "";

		if (val == null)
			val = "00";
		if (val.equals(""))
			val = "00";

		ret_val = val.length() == 1 ? ("0" + val) : val;

		return ret_val;
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/request")
	public String request(HttpServletRequest request, RequestConfig ct, CertConfig cf, Model model) throws UnsupportedEncodingException {
		request.setCharacterEncoding("euc-kr");
		log.info("성인인증 요청");
		StringBuffer sbParam = new StringBuffer();
		CT_CLI cc = new CT_CLI();

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					ct.setSite_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("req_tx")) {
					ct.setReq_tx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("ordr_idxx")) {
					ct.setOrdr_idxx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("user_name")) {
					ct.setUser_name(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("year")) {
					ct.setYear(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("month")) {
					ct.setMonth(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("day")) {
					ct.setDay(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("sex_code")) {
					ct.setSex_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("local_code")) {
					ct.setLocal_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid_hashYN")) {
					ct.setWeb_siteid_hashYN(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid")) {
					ct.setWeb_siteid(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("cert_able_yn")) {
					ct.setCert_able_yn(f_get_parm_str(valParam[i]));
				}

				// 인증창으로 넘기는 form 데이터 생성 필드
				sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\"" + f_get_parm_str(valParam[i])	+ "\"/>");
			}
		}

		if (ct.getReq_tx().equals("cert")) {
			// !!up_hash 데이터 생성시 주의 사항
			// year , month , day 가 비어 있는 경우 "00" , "00" , "00" 으로 설정이 됩니다
			// 그외의 값은 없을 경우 ""(null) 로 세팅하시면 됩니다.
			// up_hash 데이터 생성시 site_cd 와 ordr_idxx 는 필수 값입니다.
			if (ct.getCert_able_yn().equals("Y")) {
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + "" + "00" + "00" + "00" + "" + ""));
				//System.out.println("Y : ENC_KEY : "+cf.getG_conf_ENC_KEY());
				//System.out.println("Y : up_hash : " + cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + "" + "00" + "00" + "00" + "" + ""));
			} else {
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + ct.getUser_name()	+ ct.getYear() + ct.getMonth() + ct.getDay() + ct.getSex_code() + ct.getLocal_code()));
				//System.out.println("up_hash : " + cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + ct.getUser_name()	+ ct.getYear() + ct.getMonth() + ct.getDay() + ct.getSex_code() + ct.getLocal_code()));
				//System.out.println("ENC_KEY : "+cf.getG_conf_ENC_KEY());
			}

			// 인증창으로 넘기는 form 데이터 생성 필드 ( up_hash )
			sbParam.append("<input type=\"hidden\" name=\"up_hash\" value=\"" + ct.getUp_hash() + "\"/>");

			// KCP 본인확인 라이브러리 버전 정보
			sbParam.append("<input type=\"hidden\" name=\"kcp_cert_lib_ver\" value=\"" + cc.getKCPLibVer() + "\"/>");
			model.addAttribute("KCPLibVer", cc.getKCPLibVer());
			cc = null; // 객체 해제
		}
		
		//System.out.println(sbParam);
		model.addAttribute("cf", cf);
		System.out.println("cf : " + cf);
		model.addAttribute("ct", ct);
		System.out.println("ct : " + ct);
		//System.out.println("up_hash : "+ct.getUp_hash());
		
		return "front/certification/cert_req";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/response")
	public String certResponse(HttpServletRequest request, ResponseConfig rc, CertConfig cf, Model model) throws UnsupportedEncodingException {
		request.setCharacterEncoding("euc-kr");
		StringBuffer sbParam = new StringBuffer();
	    CT_CLI       cc      = new CT_CLI();
	    //System.out.println("rc = " + rc);
	    //System.out.println("cf = " + cf);
	    Enumeration<String> params = request.getParameterNames();
	    //System.out.println("params = " + params);
		while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					rc.setSite_cd(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getSite_cd());
				}

				if (nmParam.equals("ordr_idxx")) {
					rc.setOrdr_idxx(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getOrdr_idxx());
				}

				if (nmParam.equals("res_cd")) {
					rc.setRes_cd(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getRes_cd());
				}

				if (nmParam.equals("req_tx")) {
					rc.setReq_tx(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getReq_tx());
				}

				if (nmParam.equals("cert_no")) {
					rc.setCert_no(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getCert_no());
				}

				if (nmParam.equals("enc_cert_data2")) {
					rc.setEnc_cert_data2(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getEnc_cert_data2());
				}

				if (nmParam.equals("dn_hash")) {
					rc.setDn_hash(f_get_parm_str(valParam[i]));
					//System.out.println(rc.getDn_hash());
				}
				// 결과 메시지가 한글 데이터 URL decoding 해줘야합니다.
				// 부모창으로 넘기는 form 데이터 생성 필드
				if (nmParam.equals("res_msg")) {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ URLDecoder.decode(valParam[i], "UTF-8") + "\"/>");
				} else {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ f_get_parm_str(valParam[i]) + "\"/>");
				}

			}
		}
	    
	    // 결과 처리
	        if( rc.getRes_cd().equals( "0000" ) )
	        {
	            // dn_hash 검증
	            // KCP 가 리턴해 드리는 dn_hash 와 사이트 코드, 요청번호 , 인증번호를 검증하여
	            // 해당 데이터의 위변조를 방지합니다
	            if (!cc.checkValidHash( cf.getG_conf_ENC_KEY(), rc.getDn_hash(), ( rc.getSite_cd() + rc.getOrdr_idxx() + rc.getCert_no())))
	            {
	                // 검증 실패시 처리 영역

	                System.out.println("dn_hash 변조 위험있음");
	                //cc = null; // 객체 반납 ( 루틴 탈출시에만 호출 )
	            }

	            // 가맹점 DB 처리 페이지 영역

	            System.out.println("rc.getSite_cd() = " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() = " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() = " + rc.getEnc_cert_data2()); // 암호화 v2
	            
	            // 인증데이터 복호화 함수
	            // 해당 함수는 암호화된 enc_cert_data2 를
	            // site_cd 와 cert_no 를 가지고 복화화 하는 함수 입니다.
	            // 정상적으로 복호화 된경우에만 인증데이터를 가져올수 있습니다.
	            System.out.println("cf.getG_conf_ENC_KEY() : " + cf.getG_conf_ENC_KEY());
	            System.out.println("rc.getSite_cd() : " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() : " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() : " + rc.getEnc_cert_data2());
	            cc.decryptEncCert(cf.getG_conf_ENC_KEY(), rc.getSite_cd(), rc.getCert_no(), rc.getEnc_cert_data2());
	            cc.setCharSetUtf8(); // 복호와 결과값 인코딩 변경 메서드 ( UTF-8 인코딩 사용시 주석을 해제하시기 바랍니다.)
	            
	            System.out.println( "이동통신사 코드"    + cc.getKeyValue("comm_id"     ) ); // 이동통신사 코드   
	            System.out.println( "전화번호"           + cc.getKeyValue("phone_no"    ) ); // 전화번호          
	            System.out.println( "이름"               + cc.getKeyValue("user_name"   ) ); // 이름              
	            System.out.println( "생년월일"           + cc.getKeyValue("birth_day"   ) ); // 생년월일          
	            System.out.println( "성별코드"           + cc.getKeyValue("sex_code"    ) ); // 성별코드          
	            System.out.println( "내/외국인 정보 "    + cc.getKeyValue("local_code"  ) ); // 내/외국인 정보    
	            System.out.println( "CI"                 + cc.getKeyValue("ci"          ) ); // CI                
	            System.out.println( "DI 중복가입 확인값" + cc.getKeyValue("di"          ) ); // DI 중복가입 확인값
	            System.out.println( "CI_URL"             + URLDecoder.decode(cc.getKeyValue("ci_url"      ) ) ); // CI URL 인코딩 값
	            System.out.println( "DI_URL"             + URLDecoder.decode(cc.getKeyValue("di_url"      ) ) ); // DI URL 인코딩 값
	            System.out.println( "웹사이트 아이디  "  + cc.getKeyValue("web_siteid"  ) ); // 암호화된 웹사이트 아이디
	            System.out.println( "암호화된 결과코드"  + cc.getKeyValue("res_cd"      ) ); // 암호화된 결과코드
	            System.out.println( "암호화된 결과메시지"+ cc.getKeyValue("res_msg"     ) ); // 암호화된 결과메시지
				String birth = cc.getKeyValue("birth_day");//19871215
				String str01 = birth.substring(0, 4);
				String str02 = birth.substring(4,6);
				String str03 = birth.substring(6);
				String result=str01+"-"+str02+"-"+str03;
				LocalDate now = LocalDate.now();
				LocalDate parsedBirthDate = LocalDate.parse(result, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				
				
				
				int age = now.minusYears(parsedBirthDate.getYear()).getYear();
				if(parsedBirthDate.plusYears(age).isAfter(now)) {
					age = age-1;
				}
				String num = cc.getKeyValue("phone_no");
				HttpSession session = request.getSession();
				if(num!=null&num.length()>=11) {
					String str04 = num.substring(0,3);
					String str05 = num.substring(3,7);
					String str06 = num.substring(7);
					String phone = str04+"-"+str05+"-"+str06;
					if(memberRepository.findAllByPhone(phone)!=null) {
						model.addAttribute("CertCode", "Dup");
						return "front/certification/cert_res";
					}else {
						session.setAttribute("phone", phone);
					}
				}
				if(age<19) {
					model.addAttribute("CertCode","Young");
					return "front/certification/cert_res";
				}else {
					session.setAttribute("birth", result);
					session.setAttribute("name", cc.getKeyValue("user_name"));
				}
	        }
	        else/*if( res_cd.equals( "0000" ) != true )*/
	        {
	        	System.out.println("인증 실패");
	        }

	    cc = null; // 객체 반납
	    System.out.println(sbParam);
		return "front/certification/cert_res";
	}
	
	@GetMapping("/findIdF")
	public String findIdF(Model model, CertConfig cf) {
		
		String ordr_idxx = "PARK" + (new SimpleDateFormat("yyyyMMddHHmmssSSSSSSS").format(new Date()));
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findInfoRes");
		model.addAttribute("cf", cf);
		model.addAttribute("ordr_idxx",ordr_idxx);
		
		return "front/findInfo/findIdF";
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/findInfoReq")
	public String findIdReq(HttpServletRequest request, RequestConfig ct, CertConfig cf, Model model) throws UnsupportedEncodingException {
		request.setCharacterEncoding("euc-kr");
		log.info("아이디 찾기 문자인증 요청");
		CT_CLI cc = new CT_CLI();
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findInfoRes");
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					ct.setSite_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("req_tx")) {
					ct.setReq_tx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("ordr_idxx")) {
					ct.setOrdr_idxx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("user_name")) {
					ct.setUser_name(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("year")) {
					ct.setYear(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("month")) {
					ct.setMonth(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("day")) {
					ct.setDay(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("sex_code")) {
					ct.setSex_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("local_code")) {
					ct.setLocal_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid_hashYN")) {
					ct.setWeb_siteid_hashYN(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid")) {
					ct.setWeb_siteid(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("cert_able_yn")) {
					ct.setCert_able_yn(f_get_parm_str(valParam[i]));
				}
			}
		}

		if (ct.getReq_tx().equals("cert")) {
			if (ct.getCert_able_yn().equals("Y")) {
				
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + "" + "00" + "00" + "00" + "" + ""));
			} else {
				
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + ct.getUser_name()	+ ct.getYear() + ct.getMonth() + ct.getDay() + ct.getSex_code() + ct.getLocal_code()));
			}

			model.addAttribute("KCPLibVer", cc.getKCPLibVer());
			cc = null; // 객체 해제
		}
		
		model.addAttribute("cf", cf);
		model.addAttribute("ct", ct);
		
		return "front/findInfo/find_req";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/findInfoRes")
	public String findInfoRes(HttpServletRequest request, ResponseConfig rc, CertConfig cf, Model model) throws UnsupportedEncodingException, EncoderException {
		request.setCharacterEncoding("euc-kr");
		
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findInfoRes");
		StringBuffer sbParam = new StringBuffer();
	    CT_CLI       cc      = new CT_CLI();

	    Enumeration<String> params = request.getParameterNames();

	    while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					rc.setSite_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("ordr_idxx")) {
					rc.setOrdr_idxx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("res_cd")) {
					rc.setRes_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("req_tx")) {
					rc.setReq_tx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("cert_no")) {
					rc.setCert_no(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("enc_cert_data2")) {
					rc.setEnc_cert_data2(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("dn_hash")) {
					rc.setDn_hash(f_get_parm_str(valParam[i]));
				}
				if (nmParam.equals("res_msg")) {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ URLDecoder.decode(valParam[i], "UTF-8") + "\"/>");
				} else {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ f_get_parm_str(valParam[i]) + "\"/>");
				}

			}
		}
	        if( rc.getRes_cd().equals( "0000" ) )
	        {
	            if (!cc.checkValidHash( cf.getG_conf_ENC_KEY(), rc.getDn_hash(), ( rc.getSite_cd() + rc.getOrdr_idxx() + rc.getCert_no())))
	            {
	                System.out.println("dn_hash 변조 위험있음");
	            }

	            System.out.println("rc.getSite_cd() = " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() = " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() = " + rc.getEnc_cert_data2()); // 암호화 v2
	            
	            System.out.println("cf.getG_conf_ENC_KEY() : " + cf.getG_conf_ENC_KEY());
	            System.out.println("rc.getSite_cd() : " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() : " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() : " + rc.getEnc_cert_data2());
	            cc.decryptEncCert(cf.getG_conf_ENC_KEY(), rc.getSite_cd(), rc.getCert_no(), rc.getEnc_cert_data2());
	            cc.setCharSetUtf8(); 
	            
	            System.out.println( "이동통신사 코드"    + cc.getKeyValue("comm_id"     ) ); // 이동통신사 코드   
	            System.out.println( "전화번호"           + cc.getKeyValue("phone_no"    ) ); // 전화번호          
	            System.out.println( "이름"               + cc.getKeyValue("user_name"   ) ); // 이름              
	            System.out.println( "생년월일"           + cc.getKeyValue("birth_day"   ) ); // 생년월일          
	            System.out.println( "성별코드"           + cc.getKeyValue("sex_code"    ) ); // 성별코드          
	            System.out.println( "내/외국인 정보 "    + cc.getKeyValue("local_code"  ) ); // 내/외국인 정보    
	            System.out.println( "CI"                 + cc.getKeyValue("ci"          ) ); // CI                
	            System.out.println( "DI 중복가입 확인값" + cc.getKeyValue("di"          ) ); // DI 중복가입 확인값
	            System.out.println( "CI_URL"             + URLDecoder.decode(cc.getKeyValue("ci_url"      ) ) ); // CI URL 인코딩 값
	            System.out.println( "DI_URL"             + URLDecoder.decode(cc.getKeyValue("di_url"      ) ) ); // DI URL 인코딩 값
	            System.out.println( "웹사이트 아이디  "  + cc.getKeyValue("web_siteid"  ) ); // 암호화된 웹사이트 아이디
	            System.out.println( "암호화된 결과코드"  + cc.getKeyValue("res_cd"      ) ); // 암호화된 결과코드
	            System.out.println( "암호화된 결과메시지"+ cc.getKeyValue("res_msg"     ) ); // 암호화된 결과메시지
				String num = cc.getKeyValue("phone_no");
	            
				if(num!=null&num.length()>=11) {
					String str04 = num.substring(0,3);
					String str05 = num.substring(3,7);
					String str06 = num.substring(7);
					String phone = str04+"-"+str05+"-"+str06;
					if(memberRepository.findAllByPhone(phone)!=null) {
						String id = memberRepository.findAllByPhone(phone).getUsername();
						idsmsService.sendSMSAsync(id, phone);
						model.addAttribute("FindCode", "S");
					}else {
						model.addAttribute("FindCode", "Null");
					}
				} 
	        }
	        else
	        {
	        	System.out.println("인증 실패");
	        }
	    cc = null; // 객체 반납
		return "front/findInfo/find_res";
	}
	
	
	@GetMapping("/findPwF")
	public String findPwF(Model model, CertConfig cf) {
		
		String ordr_idxx = "PARK" + (new SimpleDateFormat("yyyyMMddHHmmssSSSSSSS").format(new Date()));
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findPwRes");
		model.addAttribute("cf", cf);
		model.addAttribute("ordr_idxx",ordr_idxx);
		
		return "front/findPw/findPwF";
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/findPwReq")
	public String findPwReq(HttpServletRequest request, RequestConfig ct, CertConfig cf, Model model) throws UnsupportedEncodingException {
		request.setCharacterEncoding("euc-kr");
		log.info("비밀번호 찾기 문자인증 요청");
		CT_CLI cc = new CT_CLI();
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findPwRes");
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					ct.setSite_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("req_tx")) {
					ct.setReq_tx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("ordr_idxx")) {
					ct.setOrdr_idxx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("user_name")) {
					ct.setUser_name(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("year")) {
					ct.setYear(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("month")) {
					ct.setMonth(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("day")) {
					ct.setDay(f_get_parm_int(valParam[i]));
				}

				if (nmParam.equals("sex_code")) {
					ct.setSex_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("local_code")) {
					ct.setLocal_code(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid_hashYN")) {
					ct.setWeb_siteid_hashYN(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("web_siteid")) {
					ct.setWeb_siteid(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("cert_able_yn")) {
					ct.setCert_able_yn(f_get_parm_str(valParam[i]));
				}
			}
		}

		if (ct.getReq_tx().equals("cert")) {
			if (ct.getCert_able_yn().equals("Y")) {
				
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + "" + "00" + "00" + "00" + "" + ""));
			} else {
				
				ct.setUp_hash(cc.makeHashData(cf.getG_conf_ENC_KEY(), ct.getSite_cd() + ct.getOrdr_idxx() + (ct.getWeb_siteid_hashYN().equals("Y") ? ct.getWeb_siteid() : "") + ct.getUser_name()	+ ct.getYear() + ct.getMonth() + ct.getDay() + ct.getSex_code() + ct.getLocal_code()));
			}

			model.addAttribute("KCPLibVer", cc.getKCPLibVer());
			cc = null; // 객체 해제
		}
		
		model.addAttribute("cf", cf);
		model.addAttribute("ct", ct);
		
		return "front/findPw/findPw_req";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/findPwRes")
	public String findPwRes(HttpServletRequest request, ResponseConfig rc, CertConfig cf, Model model) throws UnsupportedEncodingException, EncoderException {
		request.setCharacterEncoding("euc-kr");
		
		cf.setG_conf_Ret_URL("http://noddak.net/certification/findPwRes");
		StringBuffer sbParam = new StringBuffer();
	    CT_CLI       cc      = new CT_CLI();

	    Enumeration<String> params = request.getParameterNames();

	    while (params.hasMoreElements()) {
			String nmParam = (String) params.nextElement();
			String valParam[] = request.getParameterValues(nmParam);

			for (int i = 0; i < valParam.length; i++) {
				if (nmParam.equals("site_cd")) {
					rc.setSite_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("ordr_idxx")) {
					rc.setOrdr_idxx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("res_cd")) {
					rc.setRes_cd(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("req_tx")) {
					rc.setReq_tx(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("cert_no")) {
					rc.setCert_no(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("enc_cert_data2")) {
					rc.setEnc_cert_data2(f_get_parm_str(valParam[i]));
				}

				if (nmParam.equals("dn_hash")) {
					rc.setDn_hash(f_get_parm_str(valParam[i]));
				}
				if (nmParam.equals("res_msg")) {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ URLDecoder.decode(valParam[i], "UTF-8") + "\"/>");
				} else {
					sbParam.append("<input type=\"hidden\" name=\"" + nmParam + "\" value=\""+ f_get_parm_str(valParam[i]) + "\"/>");
				}

			}
		}
	        if( rc.getRes_cd().equals( "0000" ) )
	        {
	            if (!cc.checkValidHash( cf.getG_conf_ENC_KEY(), rc.getDn_hash(), ( rc.getSite_cd() + rc.getOrdr_idxx() + rc.getCert_no())))
	            {
	                System.out.println("dn_hash 변조 위험있음");
	            }

	            System.out.println("rc.getSite_cd() = " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() = " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() = " + rc.getEnc_cert_data2()); // 암호화 v2
	            
	            System.out.println("cf.getG_conf_ENC_KEY() : " + cf.getG_conf_ENC_KEY());
	            System.out.println("rc.getSite_cd() : " + rc.getSite_cd());
	            System.out.println("rc.getCert_no() : " + rc.getCert_no());
	            System.out.println("rc.getEnc_cert_data2() : " + rc.getEnc_cert_data2());
	            cc.decryptEncCert(cf.getG_conf_ENC_KEY(), rc.getSite_cd(), rc.getCert_no(), rc.getEnc_cert_data2());
	            cc.setCharSetUtf8(); 
	            
	            System.out.println( "이동통신사 코드"    + cc.getKeyValue("comm_id"     ) ); // 이동통신사 코드   
	            System.out.println( "전화번호"           + cc.getKeyValue("phone_no"    ) ); // 전화번호          
	            System.out.println( "이름"               + cc.getKeyValue("user_name"   ) ); // 이름              
	            System.out.println( "생년월일"           + cc.getKeyValue("birth_day"   ) ); // 생년월일          
	            System.out.println( "성별코드"           + cc.getKeyValue("sex_code"    ) ); // 성별코드          
	            System.out.println( "내/외국인 정보 "    + cc.getKeyValue("local_code"  ) ); // 내/외국인 정보    
	            System.out.println( "CI"                 + cc.getKeyValue("ci"          ) ); // CI                
	            System.out.println( "DI 중복가입 확인값" + cc.getKeyValue("di"          ) ); // DI 중복가입 확인값
	            System.out.println( "CI_URL"             + URLDecoder.decode(cc.getKeyValue("ci_url"      ) ) ); // CI URL 인코딩 값
	            System.out.println( "DI_URL"             + URLDecoder.decode(cc.getKeyValue("di_url"      ) ) ); // DI URL 인코딩 값
	            System.out.println( "웹사이트 아이디  "  + cc.getKeyValue("web_siteid"  ) ); // 암호화된 웹사이트 아이디
	            System.out.println( "암호화된 결과코드"  + cc.getKeyValue("res_cd"      ) ); // 암호화된 결과코드
	            System.out.println( "암호화된 결과메시지"+ cc.getKeyValue("res_msg"     ) ); // 암호화된 결과메시지
				
	            String num = cc.getKeyValue("phone_no");
				if(num!=null&num.length()>=11) {
					String str04 = num.substring(0,3);
					String str05 = num.substring(3,7);
					String str06 = num.substring(7);
					String phone = str04+"-"+str05+"-"+str06;
//					System.out.println("phone : " + phone);
					if(memberRepository.findAllByPhone(phone)!=null) {
						Member member = memberRepository.findAllByPhone(phone);
						member.setPassword(member.getUsername());
						memberService.memberUpdate(member);
						pwsmsService.sendSMSAsync(member.getUsername(), phone);
						model.addAttribute("FindPwCode", "S");
						System.out.println("문자발송");
					}else {
						model.addAttribute("FindPwCode", "Null");
						System.out.println("문자 미발송");
					}
				} 
	        }
	        else
	        {
	        	System.out.println("인증 실패");
	        }
	    cc = null; // 객체 반납
		return "front/findPw/findPw_res";
	}
}









































