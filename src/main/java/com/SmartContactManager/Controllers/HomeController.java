package com.SmartContactManager.Controllers;

import com.SmartContactManager.Dao.UserRepository;
import com.SmartContactManager.Entities.User;
import com.SmartContactManager.messageHelper.Message;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;

/**
 *
 * @author Aniket_Randhave
 */
@Controller
public class HomeController {

    @Autowired  // create obejct and inject here 
    private BCryptPasswordEncoder password_Encoder;  
    
    @Autowired
    private UserRepository user_repository;

    @GetMapping(path = "/")
    public String base(Model m) {
        m.addAttribute("title", "Home page ");
        return "home";
    }

    @GetMapping(path = "/home")
    public String Home(Model m) {
        m.addAttribute("title", "Home page ");
        return "home";
    }

    @GetMapping(path = "/about")
    public String About(Model m) {
        m.addAttribute("title", "About page !");
        return "about";
    }

    @GetMapping(path = "/contact")
    public String Contact(Model m) {
        m.addAttribute("title", "Contact page !");
        return "contact";
    }

    @GetMapping(path = "/signin")
    public String SignIn() {
        return "signin";
    }
 
    @GetMapping(path = "/signup")
    public String SignUp(Model m) {
        m.addAttribute("user", new User()); // create new instance of user entity for storing form data into this instance user
        return "signup";
    }

//    Handle for registering user data
    @PostMapping(path = "/do_register") // and after submiting form these user data store into user entity , and by the help of medelattrubute we get all data 
    public String do_register(@ModelAttribute("user") User user, BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model m, HttpSession session) {

        try {
            if (!agreement) {
                System.out.println("You have not agreed the Terms and condition");
                throw new Exception("You have not accepted term and conditions");  // if exception occures , then will be excuted catch block 
            }
            // validation checking
            if (result.hasErrors()) {  // by default hasErrors give true value
                System.out.println("Occured validation error .....");
                System.out.println(user);
                return "signup";
            }
            // if agreement checkbox is marked then this code excute , set the user value and save user data into database by user_repository , user_repository have method to save user data 
            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setImageUrl("default.png");
            user.setPassword(password_Encoder.encode(user.getPassword()));  // at the time of registration this code decrypt password
            
             this.user_repository.save(user); // save the user data into database
            m.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully registered", "alert-success"));
              
//            System.out.println("agreement" + agreement);
//            System.out.println("user data " + user);

            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong ! email must be unique  ", "alert-danger")); // with the help of this session you can show any message dynamicly
            return "signup";
        }

    }

}
