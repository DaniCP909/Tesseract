package dad.aplicacionweb.openars.controllers;

import dad.aplicacionweb.openars.models.User;
import dad.aplicacionweb.openars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userServ;


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();   //java.security

        if(principal != null){

            model.addAttribute("logged", true);
            model.addAttribute("username", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));     //return boolean

        } else{
            model.addAttribute("logged", false);
        }

    }

    @GetMapping("/all-users")
    public String allUsers(Model model){

        model.addAttribute("users", userServ.findAll());

        return "temps_User/all-users";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request){


        String loggedname = (String)model.getAttribute("username");

        User user = userServ.findByUsername(loggedname);

        model.addAttribute("userid", user.getId());
        model.addAttribute("user", user);


        return "temps_User/profile";
    }

    @GetMapping("/signup-user")
    public String signupUser(){
        return "temps_User/signup_user";
    }

    @PostMapping("/signup-user")
    public String signupUserPost(User user){
        List<String> roles = new ArrayList<String>();
        roles.add("USER");
        user.setRoles(roles);
        userServ.save(user);
        return "redirect:/";
    }

    @GetMapping("/remove-user/{id}")
    public String removeUser(Model model, @PathVariable Long id){

        Optional<User> user = userServ.findById(id);

        if(user.isPresent()){
            userServ.delete(id);
            model.addAttribute("user", user.get());
        }
        return "temps_User/user-removed";

    }

    @GetMapping("/edit-user/{id}")
    public String editUser(Model model, HttpServletRequest request, @PathVariable Long id) {

        Optional<User> iduser = userServ.findById(id);

        if(iduser.isPresent()){
            model.addAttribute("user", iduser.get());
            model.addAttribute("userid", id);
        }

        return "temps_User/edit-user";

    }

    @PostMapping("/edit-user/{id}")
    public String editUerProcess(Model model, User user, @PathVariable Long id){

        Optional<User> actualuser = userServ.findById(id);

        if(actualuser.isPresent()){
            List<String> newroles = actualuser.get().getRoles();
            user.setRoles(newroles);
            userServ.save(user);
        }


        return "redirect:/";
    }

}
