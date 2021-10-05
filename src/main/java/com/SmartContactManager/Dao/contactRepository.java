/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.Dao;

import com.SmartContactManager.Entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Aniket_Randhave
 */
public interface contactRepository extends JpaRepository<Contact, Integer> {
    
    // currentPage = page
    // contact per page = 5 ---> this is value , how many contact show in one page
    @Query("select c from Contact c where c.user.id = :user_id")  // query for getting data of user by providing cid
    public Page<Contact> getAllContactsBy_username(@Param("user_id") int user_id ,Pageable pageable); // pagable it is class , help for set the value of page limit like 5,6, etc
    
      // code for pagination
    
    
}

// here we create seprate method or interface for show all contacts in page because we implement pagination concept here

// if you dont apply pagination then you are write few line of code in user controller view_contacts controller 
//        User username = principal.getName();
//        this.contact_repository.getAllContactsBy_username(username)