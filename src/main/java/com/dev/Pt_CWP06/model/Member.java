package com.dev.Pt_CWP06.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Data
public class Member implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Nullable
	@Column(name="username")
	private String username;
	
	@Nullable
	@Column(name="password")
	private String password;
	
	@Nullable
	@Column(name="nickname")
	private String nickname;
	
	@Nullable
	@Column(name="name")
	private String name;
	
	@Nullable
	@Column(name="phone")
	private String phone;
	
	@Nullable
	@Column(name="email")
	private String email;
	
	@Nullable
	@Column(name="birth")
	private String birth;
	
	@Nullable
	@Column(name="point")
	private int point;
	
	@Nullable
	@Column(name="temp_code")
	private Boolean tempcode;
	
	@Nullable
	@Column(name="enabled")
	private Boolean enabled;
	
	@Nullable
	@Column(name="join_date")
	private String joindate;
	
	@Transient
	private String pointC;
	
	@ManyToMany
	@JoinTable(
		name = "member_role",
		joinColumns = @JoinColumn(name="member_id"),
		inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<Role> roles = new ArrayList<>();
	
	@OneToMany
	private List<Videodetail> videodetails = new ArrayList<>();
	
	@OneToMany
	private List<Pointdetail> pointdetails = new ArrayList<>();
	
	@OneToMany
	private List<Accessdetail> accessdetails = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
