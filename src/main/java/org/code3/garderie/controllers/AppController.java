package org.code3.garderie;

import java.util.Date;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

  private static final Logger log = LoggerFactory.getLogger(AppController.class);

  @Autowired
  ChildRepository childRepository;

  @Autowired
  PresenceRepository presenceRepository;

  @GetMapping("/")
  public String index(HttpSession session) {
    log.debug("index");
    return "redirect:/presence";
  }

  @GetMapping("/accueil")
  public String accueil(HttpSession session) {
    log.debug("index");
    return "accueil/index";
  }

}
