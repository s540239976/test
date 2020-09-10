package com.hisoft.paper.controller.user;

import com.hisoft.paper.pojo.User;
import com.hisoft.paper.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String Login(@RequestParam("userName") String userName, HttpSession session){
        User user = userService.queryUserByName(userName);
        if(user!=null){
            session.setAttribute("user",user);
            return "index";
        }else{
            return "redirect:/user/toLogin";
        }
    }

    @RequestMapping("/query")
    public String query(Model model){
        List<User> userList = userService.queryUserList();
        model.addAttribute("userList",userList);
        return "paperlist";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

    @RequestMapping("/modifyUser")
    public String modifyUser(@RequestParam("id") Integer id,Model model){
        User user = userService.queryUserById(id);
        model.addAttribute("user",user);
        return "papermodify";
    }
}
