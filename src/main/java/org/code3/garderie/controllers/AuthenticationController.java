package org.code3.garderie;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticationController {
  private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

  @GetMapping("/login")
  public String index() {
    log.debug("index");
    return "/login/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("username") String username,
                      @ModelAttribute("password") String password,
                      HttpSession session){
    log.debug("login {}", username);
    session.setAttribute("username", username);
    session.setAttribute("group", username.equals("Chantal") ? new Long(1) : new Long(2));
    return "redirect:/presence";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session){
    log.debug("logout");
    session.invalidate();
    return "redirect:/";
  }

}
