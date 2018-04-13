package org.code3.garderie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import org.springframework.ui.ModelMap;

@Controller
public class AppController {

  @Autowired
  ChildRepository childRepository;

  @Autowired
  PresenceRepository presenceRepository;

  // @RequestMapping("/")
  public String index() {
    var group = new Group(1l, "", "");
    var presences = presenceRepository.getPresenceByDateAndGroup(new Date(), group);
    System.out.println("PRESENCE " + presences.size());
    // return "";
    return "redirect:/child/list";
  }

}
