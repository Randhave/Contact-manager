/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SmartContactManager.Controllers;

import com.SmartContactManager.Dao.UserRepository;
import com.SmartContactManager.Dao.contactRepository;
import com.SmartContactManager.Entities.Contact;
import com.SmartContactManager.Entities.User;
import com.SmartContactManager.messageHelper.Message;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Aniket_Randhave
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository user_repository;

    @Autowired
    private contactRepository contact_repository;

    @Autowired
    private BCryptPasswordEncoder password_Encoder;

    // This controller auto send  the all web pages by @ModelAttribute annotation
    @ModelAttribute
    public void common_data_for_all_webPage(Model model, Principal principal) {// Principal it is spring web security class provide help to get the username
        String username = principal.getName();
        System.out.println("Username :" + username);

        // getting the username(email)
        User user = user_repository.getUserBy_username(username);
        System.out.println("user " + user);
        model.addAttribute("user", user);
    }

    @GetMapping("/dashboard")
    public String Dashboard() {
        return "normal_user/dashboard";
    }

    @GetMapping("/add_contact")
    public String add_contact(Model model) {
        model.addAttribute("title", "Add-Contact");
        model.addAttribute("contact", new Contact());  // new Contact() it means we send blank contact or reference of Contact class

        return "normal_user/add_contact";
    }

    // creating this controller for add contact form
    @PostMapping("/process_contact")
    public String Process_contact(@ModelAttribute Contact contact, Principal principal, HttpSession session) {

        try {
            // set the contact_data into database 
            String username = principal.getName();  // first we getting username by principal and getName() this method help
            User user = this.user_repository.getUserBy_username(username);  // get the all data of user who store in into id 
            contact.setUser(user); // also we set the user in contact it called bidirectional mapping, User need to contact info and also contact need to user for store into this id
            user.getContacts().add(contact); // after getting all data , user have all data then we are able to called it on method like getContacts , getName , getId etc 
            this.user_repository.save(user); // save user because we add contact in user 

            System.out.println("Added into database");

            session.setAttribute("message", new Message("Successfully registered", "alert-success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong ! email must be unique ", "alert-danger"));
        }
        return "normal_user/add_contact";
    }

    @GetMapping("/view_contacts/{page}")
    public String view_contacts(@PathVariable("page") Integer page, Model model, Principal principal) {

        try {
            model.addAttribute("title", "All-contacts");

            // here we give page argument , it is currentpage 
            Pageable pageable = PageRequest.of(page, 5);  // pagerequest it is property , it accept two argument first currentpage and limit of one page (How many contact show in page)

            String username = principal.getName();  // getting username(email)
            User user = this.user_repository.getUserBy_username(username);  // get all user data by username
            Page<Contact> contacts = this.contact_repository.getAllContactsBy_username(user.getId(), pageable); // and then send id contact-repository interface for getting all contacts stroe into this id

            model.addAttribute("contacts", contacts); // above 2 3 line code are represent for getting contacts and this line code send all contacts on webpage by model attribute
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPage", contacts.getTotalPages());
            System.out.println("view contacts on fire");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "normal_user/view_contacts";
    }

    // in this controller we get contact data by cid , because we send this data in contact_details page
    @GetMapping("/contact_details/{cid}")
    public String contact_details(@PathVariable("cid") Integer cid, Model model, Principal principal) {
//        System.out.println("Contact id " + cid);

        try {
            Optional<Contact> contact = this.contact_repository.findById(cid);  // findById  and so many method are available in contact-reposotory because this are the interface and they have all user data and they interface use jpa repository
            Contact user_contact_data = contact.get();  // getting particular object contact data 

            String username = principal.getName();
            User user = this.user_repository.getUserBy_username(username);
            if (user.getId() == user_contact_data.getUser().getId()) {  // this are the security code
                model.addAttribute("contacts", user_contact_data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "normal_user/contact_details";
    }

    @GetMapping("/delete_contact/{cid}")
    public String delete_contact(@PathVariable("cid") Integer cid, Model model, HttpSession session, Principal principal) {

        try {

            Contact contact = this.contact_repository.findById(cid).get();  // find contact by user and then get all data of these contact 
            User user = this.user_repository.getUserBy_username(principal.getName());
            user.getContacts().remove(contact);
            this.user_repository.save(user); // updated contact will be save by this code after remove contact

            System.out.println("deleted successfully");
            session.setAttribute("message", new Message("Contact deleted successfully", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Failed to deleted contact !", "danger"));
        }
        return "redirect:/user/view_contacts/0";
    }

    // we don't know but update url not working , and this code is correct code in future don't touch this code 
//    @PostMapping("/update_contact/{cid]")
//    public String update_form(@PathVariable("cid") Integer cid, Model model) {
//
//        Contact contact = this.contact_repository.findById(cid).get();
//        model.addAttribute("contact", contact);
//        System.out.println("update form on fire");
//        return "normal_user/update_form";
//    }
    
    @PostMapping("/update-contact")
    public String updateContact(){
        return "/normal_user/update_form";
    }
    
    @PostMapping("/process_UpdateContact")
    public String updateHandler(@ModelAttribute Contact contact, Principal principal, Model model, HttpSession session) {
        try {
            model.addAttribute("contact", contact);
            User user = this.user_repository.getUserBy_username(principal.getName()); // getting user by providing username throgh method , we already declared in startig of project
            contact.setUser(user);  // also setting the user in contact 
            this.contact_repository.save(contact);
            session.setAttribute("message", new Message("Contact Updated successfully", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Failed to Update contact !", "danger"));
        }
        return "redirect:/user/view_contacts/0";
    }

    @GetMapping("/user_profile")
    public String user_profile(Model model, Principal principal) {
       return "normal_user/user_profile";
    }

    // Changing password controller
    @GetMapping("/settings")
    public String Settings() {
        return "normal_user/settings";
    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
        System.out.println("old password : " + oldPassword);
        System.out.println("newPassword : " + newPassword);

        String username = principal.getName();
        User currentUser = this.user_repository.getUserBy_username(username);

        if (this.password_Encoder.matches(oldPassword, currentUser.getPassword())) {  // first argument is rawpassword means new password without encoded and second argument is existing password or encoded passowrd
            currentUser.setPassword(this.password_Encoder.encode(newPassword));  // set the new encoded password
            this.user_repository.save(currentUser);
            session.setAttribute("message", new Message("Successfully changes", "success"));
            System.out.println("successfully chages password");
            return "redirect:/logout";
        } else {
            session.setAttribute("message", new Message("Please Enter correct old password !", "danger"));
            return "redirect:/user/settings";
        }

    }

}

// In this class we get the all userdata by providing username 
// 1. first we get username by principal class , they class help
// 2. after getting user data we send web page by model attribute
