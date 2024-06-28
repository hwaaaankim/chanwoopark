package com.dev.Pt_CWP06.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.Role;
import com.dev.Pt_CWP06.model.bankda.Order;
import com.dev.Pt_CWP06.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService implements UserDetailsService{

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Member findByUsername(String username) {
		Member member = memberRepository.findByUsername(username);
		return member;
	}
	
	public Member save(Member member) {
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Date j_date = new Date();
		String join_date = format.format(j_date);
		member.setJoindate(join_date);
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		member.setEnabled(true);
		member.setTempcode(true);
		Role role = new Role();
		role.setId(2l);
		member.getRoles().add(role);
		return memberRepository.save(member);
	}
	
	public void memberUpdate(Member member) {
//		System.out.println("service : " + member);
		Optional<Member> me = memberRepository.findById(member.getId());
		me.ifPresent(selectMember -> {
			if(member.getPassword()!=null) {
				String encodedPassword = passwordEncoder.encode(member.getPassword());
				selectMember.setPassword(encodedPassword);
//				System.out.println(member.getPassword());
			}
			selectMember.setPoint(member.getPoint());
			selectMember.setEnabled(member.getEnabled());
			memberRepository.save(selectMember);
		});
	}
	
	public void infoUpdate(Member member) {
//		System.out.println("service : " + member);
		Optional<Member> me = memberRepository.findById(member.getId());
		me.ifPresent(selectMember -> {
			if(member.getPassword()!=null) {
				String encodedPassword = passwordEncoder.encode(member.getPassword());
				selectMember.setPassword(encodedPassword);
				System.out.println(member.getPassword());
			}else if(member.getEnabled()!=null) {
				selectMember.setEnabled(member.getEnabled());
			}else if(member.getNickname()!=null) {
				selectMember.setNickname(member.getNickname());
			}else if(member.getEmail()!=null) {
				selectMember.setEmail(member.getEmail());
			}
			
			memberRepository.save(selectMember);
		});
	}
	
	public void withdraw(Member member) {
		Optional<Member> me = memberRepository.findById(member.getId());
		me.ifPresent(selectMem->{
			selectMem.setEnabled(false);
			memberRepository.save(selectMem);
		});
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("-- loadUserByUsername --");
		Member member = memberRepository.findByUsername(username);
		if(member == null) {
			log.info("-- 계정 정보가 존재하지 않습니다 --");
			throw new UsernameNotFoundException(username);
		}
		return member;
	}
	
	public int pointCharge(Member member, Order order) {
		
		int point = Integer.parseInt(order.getOrder_price_amount());
		
		return memberRepository.pointCharge(point , member.getPhone());
	}
}
