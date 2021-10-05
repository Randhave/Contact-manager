/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.config;

import com.SmartContactManager.Entities.User;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Aniket_Randhave
 */
public class CustomUserDetails implements UserDetails {

    private User user;
    
    @Autowired
    public CustomUserDetails(User user) {
        super();
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());  // role we are initlized in user entity class and depends only role
        return List.of(simpleGrantedAuthority); // here we can return multiple list of role , but we have only one role 
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // we are consider username are email 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; /// we are simply return true value , we dont know but we return this value   
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

// Note username means email ,  we are consider username as a email
//
/// UserDetails are the interface and these interface have some method for helping or handling spring security
// and these method are available in interace and we are implements these method and it required to implements otherwise we get error
// we are implements here these method becuase these method help for or provide security for web pages
// what is mean provide security for web pages : suppose we need to only user access to the login page by entering username and password
//  and we can apply this spring security on custom web pages like login page , user_dashboard
// user_dashboard this page access only user who have username and password



// we are using spring boot starter security dependency 
// and these dependency have many standerd way to use

// 1. we are creating this class first and extends UserDetails interface 
//    and implements all method here defined in UserDetails interface
//    in this class we are only implements methods not provide , we are provide another class
// by the help of step 1 we get all implementations of user and we can use this implmentations in UserDetailsService class and this class defined seprate

// 2. After complete first step , then we are creaing another class for USING this implementations defined in this class
// for using this implementations we are defined another class Name is UserDetailsServiceImpl.java


// 3. after complete 2 step we are defined another java class in config package  
//    , and these class we are defined bean and method for tellig which web pages have security 
//    also telling what is required user for accessing user_dashboard page , user_dashboard are the main homopage of after provide username and password or credentials

// Above these three steps are stander rull for implementing spring boot security on web pages


// here we are created customdetailsService class and this class method we implement in another class known as UserDetailsImpl