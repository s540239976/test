package cn.smbms.controller;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/userLogin")
    public String userLogin(@RequestParam("userCode") String userCode,
                            @RequestParam("userPassword") String userPassword,
                            HttpSession session,
                            Model model) {
        User user = userService.login(userCode, userPassword);
        if (null != user) {
            if (user.getUserPassword().equals(userPassword)) {
                session.setAttribute(Constants.USER_SESSION, user);
                return "redirect:/user/main";
            } else {
                throw new RuntimeException("密码不正确");
            }
        } else {
            throw new RuntimeException("用户名不正确");
        }
    }

    @RequestMapping("/tologin")
    public String toLogin() {
        return "login";
    }
}
