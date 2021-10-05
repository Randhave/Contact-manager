/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.config;

import com.SmartContactManager.Dao.UserRepository;
import com.SmartContactManager.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Aniket_Randhave
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired // this annotation auto generated object of this class and put and apply this class
    private UserRepository user_repository;

    // in this method fetching username(email) from database 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {

            // fetching user from  database
            User user = this.user_repository.getUserBy_username(username);
            if (user == null) {
                throw new UsernameNotFoundException("Could Not be found User !");// here you can give any exception and put your message 
            }
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            return customUserDetails;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
  
    }

}

// here also we need to implemnts method becuse we are extending UserDetailsService interface
// in this class we are only fetching username form database and return complete object 
// how we are fetch username from db in this class :- in Dao package UserRepository class we defined one method and put query on these method
//                                                    these method feth the username or user data form database
//                                                    and here we are only calling these method and pass String username and then we get username form db 
