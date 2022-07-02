package com.company.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Authentication
        auth.inMemoryAuthentication()
                .withUser("1").password("{bcrypt}$2a$10$8iNTeno76dTI41UOvf2EJeBgFJYldLMl0hMHYZ6zO1TT6.VzDS8Za").roles("ADMIN")
                .and()
                .withUser("4").password("{noop}2225").roles("USER")
                .and()
                .withUser("5").password("{noop}1245").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Authorization
        http.authorizeRequests()
                .antMatchers("/article_like", "/article_like/*").permitAll()
                .antMatchers("/article/public/*").permitAll()
                .antMatchers("/article/adm/*").hasRole( "ADMIN")
                .antMatchers("/article/pub/*").hasRole( "PUBLISHER")
                .antMatchers("/article/mod/*").hasRole( "MODERATOR")
                .antMatchers("/profile", "/profile/adm/*").hasRole( "ADMIN")
                .antMatchers("/profile", "/profile/user/*").hasRole( "USER")
                .antMatchers("/category/adm/*").hasRole( "ADMIN")
                .antMatchers("/category/public/*").hasRole( "USER")
                .antMatchers("/comment/adm/*").hasRole( "ADMIN")
                .antMatchers("/comment/user/*").hasRole( "USER")
                .antMatchers("/comment/public/*").permitAll()
                .antMatchers("/region/adm/*").hasRole( "ADMIN")
                .antMatchers("/region/public/*").permitAll()
                .antMatchers("/types/adm/*").hasRole( "ADMIN")
                .antMatchers("/types/public/*").permitAll()
                .antMatchers("/comment_like", "/article_like/*").permitAll()
                .antMatchers("/attach/admUser/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/sms/adm/*").hasRole( "ADMIN")
                .antMatchers("/admin", "/admin/*").hasRole("ADMIN")
                .antMatchers("/auth", "/auth/*").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic();
        http.csrf().disable();



//           "/profile/adm/*");
//           "/article/adm/*");
//           "/article_like/*");
//           "/category/adm/*");
//           "/comment/adm/*");
//           "/region/adm/*");
//           "/types/adm/*");
//           "/comment_like/*");
//           "/attach/adm/*");
//           "/sms/adm/*");
    }
}
