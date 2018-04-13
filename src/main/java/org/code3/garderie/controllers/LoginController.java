package org.code3.garderie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import org.springframework.ui.ModelMap;

@Controller
public class LoginController {


  @RequestMapping("/")
  public String index() {
    return "/login/login";
  }


}
