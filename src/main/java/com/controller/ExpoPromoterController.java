package com.controller;

import com.entities.UserEntity;
import com.service.SecurityService;
import com.service.UserService;
import com.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class ExpoPromoterController {
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;
    @Autowired
    SecurityService securityService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> registration(@ModelAttribute("userForm") UserEntity userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("BindingResult Error",HttpStatus.BAD_REQUEST);
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getEmail(), userForm.getConfirmPassword());

        return new ResponseEntity<>("Registration success", HttpStatus.OK);

    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String error, String logout) {
        if (error != null)
            return ("Your username and password is invalid.");

        if (logout != null)
            return ("You have been logged out successfully.");

        return "Success";
    }

    @RequestMapping(value = "/change_email", method = RequestMethod.GET)
    public
    @ResponseBody
    String changeEmail(@RequestParam String email, @RequestParam String newEmail) {
        UserEntity userEntity = getUser();
        if (newEmail != null) {
            try {
                userEntity.setEmail(email);
                userService.save(userEntity);
            } catch (Exception e) {
                return "Transaction failed: field 'email' update error.";
            }

            return "Success: filed 'email' was updated!";
        } else {
            return "New email field is required!";
        }


    }


    @RequestMapping(value = "/change_password", method = RequestMethod.GET)
    public @ResponseBody
    String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, BindingResult bindingResult) {
        //
        UserEntity user = getUser();
        if (bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(newPassword);
            user.setConfirmPassword(confirmPassword);
        } else {
            return "Current password is incorrect!";
        }
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "BindingResult Error";
        }

        userService.save(user);
        return "Password changed";
    }

    ///Rewrite for mob
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        UserEntity u = getUser();
//        model.addAttribute("email", u.getEmail());
//        model.addAttribute("password", u.getPassword());
        return "profile";
    }

    public UserEntity getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj = auth.getPrincipal();
        String username = "";
        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        UserEntity u = userService.findByEmail(username);
        return u;
    }
}