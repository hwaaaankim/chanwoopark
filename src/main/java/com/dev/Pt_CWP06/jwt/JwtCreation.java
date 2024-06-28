package com.dev.Pt_CWP06.jwt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class JwtCreation {
	
	public String createToken(String videocode) throws InvalidKeyException, NoSuchAlgorithmException {
		String secretKey = "chanwooparkpoc";
		String headerJson = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
		String payloadJson = "{\"cuid\": \"test01\",\"expt\": 1462931880,\"mc\": [{\"mckey\": \""+videocode+"\"}]}";
		System.out.println(videocode);
		System.out.println(payloadJson);
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, 5);
		now = c.getTime();
		Long test = now.getTime();
		Long toDay = TimeUnit.MILLISECONDS.toSeconds(test);
		payloadJson = payloadJson.replace("1462931880",String.format("%d", toDay));
		System.out.println(String.format("%d", now.getTime()));
		String header = Base64.encodeBase64URLSafeString(headerJson.getBytes(StandardCharsets.UTF_8));
		String payload = Base64.encodeBase64URLSafeString(payloadJson.getBytes(StandardCharsets.UTF_8));
		String content = String.format("%s.%s", header, payload);
		final Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		byte[] signatureBytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
		String signature = Base64.encodeBase64URLSafeString(signatureBytes);
		System.out.println(String.format("%s.%s", content, signature));
		return String.format("%s.%s", content, signature);
	}
}
