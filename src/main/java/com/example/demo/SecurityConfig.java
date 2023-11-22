package com.example.demo;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

	@Bean
	public InMemoryUserDetailsManager userdetails()
	{
		List<UserDetails> users=List.of(
				User.withUsername("user1").
				password("{noop}user1").
				roles("USERS").
				build(),
				User.withUsername("user2").
				password("{noop}user2").
				roles("SELLER").
				build(),
				User.withUsername("admin").
				password("{noop}admin").
				roles("ADMIN").
				build()
		);
		return new InMemoryUserDetailsManager(users);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf(cus->cus.disable())
		.authorizeHttpRequests(
				aut->
				aut.requestMatchers(HttpMethod.POST,"/api/**").hasRole("ADMIN")
				   .requestMatchers(HttpMethod.DELETE,"/api/delete_a").hasRole("SELLER")
				   .requestMatchers(HttpMethod.DELETE,"/api/delete_b").hasRole("USERS")
				   .requestMatchers(HttpMethod.GET,"/api/**").hasAnyRole("ADMIN","SELLER")
				   .anyRequest().permitAll()
		).httpBasic(Customizer.withDefaults());
		return http.build();
	}
	
}







