package com.mmt.user.controller;

import com.mmt.user.entity.Sys_user;
import com.mmt.user.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/Login")
    public String Login(Model model,@RequestParam("user_name") String name,
                         @RequestParam("user_pwd") String pwd){
        List<Sys_user> list = userService.findUserByName(name);
        if(list.size()!=0) {
            System.out.println("name="+list.get(0).getUser_name()+";password="+list.get(0).getUser_pwd());
            if (list.get(0).getUser_pwd().equals(pwd)) {
                //String uname = SecurityContextHolder.getContext().getAuthentication().getName();
                //System.out.println("当前登陆用户：" + uname + "  ;name= "+name+"  ;password="+pwd);
                model.addAttribute("name", list.get(0).getUser_name());
                return "ok";
            } else
                return "login";
        } else
            return "login";
    }



    @GetMapping("register")
    public String register(Model model){
        return "register";
    }

    @GetMapping("login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/")
    public String L(Model model){
        return "login";
    }

}*/
@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(Model model){
        return "register.html";
    }

    @RequestMapping("/register_user")
    public String regist(Model model, @RequestParam("username") String name,
                         @RequestParam("password") String pwd){
        Sys_user user = userService.findUserByName(name);
        logger.info("有新用户注册" + name + user);
        if(user == null) {//判断昵称是否已经被占用，没有占用则注册该用户
            userService.regist(name, pwd);
            return "login";
        }
        else
            return "register";
    }

    @RequestMapping("/")
    public String showHome() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("当前登陆用户：" + name);
        //System.out.println("当前登陆用户：" + name);
        //return "homepage-2.html";
        return "ok.html";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "login.html";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

    @RequestMapping("/login/error")
    public void loginError(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception =
                (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        try {
            response.getWriter().write(exception.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String invalid() {
        return "Session 已过期，请重新登录";
    }
}