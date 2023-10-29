package com.ticket.Ticketing.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
//@RequestMapping("/user-login")
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/register")
    public String setRegister(){
        return "register";
    }

    // need to check
//    @PostMapping("/login-send")
//    public void loginInfo(@RequestBody HashMap<String, Object> map){
//        System.out.println(map);
//    }


//    @GetMapping("/join")
//    public String joinPage() {
//        return "join";
//    }
//
//    @GetMapping("/dashboard")
//    public String dashboardPage(@AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("loginId", user.getUsername());
//        model.addAttribute("loginRoles", user.getAuthorities());
//        return "dashboard";
//    }
//
//    @GetMapping("/setting/admin")
//    @AdminAuthorize
//    public String adminSettingPage() {
//        return "admin_setting";
//    }
//
//    @GetMapping("/setting/member")
//    @MemberAuthorize
//    public String userSettingPage() {
//        return "member_setting";
//    }
//

}
