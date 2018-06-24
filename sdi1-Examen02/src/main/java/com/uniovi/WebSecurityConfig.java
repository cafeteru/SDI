package com.uniovi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests() // peticiones autorizadas
				.antMatchers("/css/**", "/img/**", "/script/**", "/", "/signup",
						"/login", "/admin/login")
				.permitAll()
				// Permite a todos los usuarios
				.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
				// pagina de autentificacion por defecto
				.and().formLogin().loginPage("/login").permitAll()
				// Si se loguea bien
				.defaultSuccessUrl("/home").and().logout().permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
	}
}