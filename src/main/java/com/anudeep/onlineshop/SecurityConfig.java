package com.anudeep.onlineshop;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author anude
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		
		http.headers().frameOptions().disable().and()
         .authorizeRequests()
         .anyRequest().permitAll().and().cors().and(). 
         csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("blah").password("$2a$10$sIsYzTgwro/mmDONrl/x3eb6rtauA0sp/8FTy74G4mxWjnk2RI89.").roles("ADMIN");
	}
	
	
	
	
	/*
	 * @Bean public SecurityWebFilterChain securityWebFilterChain(
	 * ServerHttpSecurity http) { return http.authorizeExchange()
	 * .pathMatchers("/actuator/**").permitAll() .anyExchange().authenticated()
	 * .and().build(); }
	 */

}