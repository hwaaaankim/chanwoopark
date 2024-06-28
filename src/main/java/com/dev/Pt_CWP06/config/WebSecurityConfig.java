package com.dev.Pt_CWP06.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsUtils;

import com.dev.Pt_CWP06.service.AccessdetailService;
import com.dev.Pt_CWP06.service.MemberService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private AccessdetailService accessdetailService;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		/*
		 * http.httpBasic().disable()
		 * .cors().configurationSource(corsConfigurationSource());
		 */
		http.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.antMatchers("/admin/**", "/community/**").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/library/**","/member/register","/member/findIdF","/member/idCheck","/member/nickCheck","/certification/**","/member/dupLogin","/api/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/member/login")
			.successHandler(new AuthenticationSuccessHandler() {
			    @Override
			    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			            Authentication authentication) throws IOException, ServletException {
			    	accessdetailService.save(authentication.getName());
			        redirectStrategy.sendRedirect(request, response, "/home");
			    }})
			.permitAll()
			.and()
		.rememberMe()
			.key("myAppKey")
			.tokenValiditySeconds(86400)
			.userDetailsService(memberService)
			.tokenRepository(tokenRepository())
			.and()
		.logout()
			.logoutUrl("/logout")
//			.logoutSuccessUrl("/")
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.permitAll()
			.and()
		.exceptionHandling()
			.accessDeniedPage("/accessDenied");		
		http.sessionManagement()
        .maximumSessions(1) // 같은 아이디로 1명만 로그인 할 수 있음
        .maxSessionsPreventsLogin(false) // 신규 로그인 사용자의 로그인이 허용되고, 기존 사용자는 세션아웃 됨
        .expiredUrl("/member/dupLogin")
        .sessionRegistry(sessionRegistry()); 
        
	}
	
	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() {
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * 
	 * configuration.addAllowedOrigin("*"); configuration.addAllowedHeader("*");
	 * configuration.addAllowedMethod("*"); configuration.setAllowCredentials(true);
	 * 
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); return source; }
	 */
	
	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }// Register HttpSessionEventPublisher

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) 
	  throws Exception {
	    auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .passwordEncoder(passwordEncoder())
	      .usersByUsernameQuery("select username, password, enabled "
	        + "from member "
	        + "where username = ?")
	      .authoritiesByUsernameQuery("select u.username, r.name "
	        + "from member_role ur inner join member u on ur.member_id = u.id "
	        + "inner join role r on ur.role_id = r.id "
	        + "where u.username = ?");
	}
	// Authetication : 로그인 관련
	// Authorization : 권한 관련
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}
}
