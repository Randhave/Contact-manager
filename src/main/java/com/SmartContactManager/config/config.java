/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Aniket_Randhave
 */
@Configuration // we put this annotation becuase we are defined bean class ,and bean it means configure something
@EnableWebSecurity // need this annotaion when we are using this WebSecurityConfiguration
public class config extends WebSecurityConfigurerAdapter {

    @ConditionalOnMissingBean
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // this bean safe or encode user password
    @ConditionalOnMissingBean
    @Bean
    public BCryptPasswordEncoder password_Encoder() { // in java easy for encode password compare to MERN stack
        return new BCryptPasswordEncoder();
    }

    // this class authinticate , and we need to provide UserDetailsService class we aleready configure
    @ConditionalOnMissingBean
    @Bean
    public DaoAuthenticationProvider authintication_provider() {

        DaoAuthenticationProvider dao_authintication_provider = new DaoAuthenticationProvider();

        dao_authintication_provider.setUserDetailsService(this.getUserDetailsService());
        dao_authintication_provider.setPasswordEncoder(password_Encoder());

        return dao_authintication_provider;
    }

    // configure method 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authintication_provider());
    }

    // this type of configured we need thats wy we do all previous configurations
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/**").permitAll().and().formLogin()
                .defaultSuccessUrl("/user/dashboard", true)
                .and()
                .csrf()
                .disable()
                .authorizeRequests();
    }
}

// In this class we are defined or implement or override some method depends upon user what type of security applye
// WebSecurityConfiguration this class provide some method , and with the help of this method we configured security on particular pages  
// anytime need to required to declared these above three bean for this configureations
// THis are the main class of setting userdetailservice 
 