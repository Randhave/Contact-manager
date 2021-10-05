/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.Dao;

import com.SmartContactManager.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
 

/**
 *
 * @author Aniket_Randhave
 */
@Service
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // here we are defined this method and get dynamicly email from user enitites  by query method 
    @Query("select u from User u where u.email = :email")   // this query help for getting user data
    public User getUserBy_username(@Param("email") String email); // this method we help for giving all user data by accepting username(email)
}


// getUserBy_username this method accept email(username) and we get the all user data 

// 1. we create a simple query for getting email from database.
//    these query not get all userdata they just return email from database

// 2. This getUserBy_username method return all userdata by accepting email and , @Param is annotation help for fetching dynamic data 