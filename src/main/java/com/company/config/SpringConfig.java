package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SpringConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Authentication
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Authorization
        http.authorizeRequests()
                .antMatchers("/article_like", "/article_like/*").permitAll()
                .antMatchers("/article/public/list/*").permitAll()
                .antMatchers("/article/adm/*").hasRole( "ADMIN")
                .antMatchers("/article/publisher/*").hasRole( "PUBLISHER")
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
        http.cors().disable();
        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return new BCryptPasswordEncoder();
    }
}
