package org.code3.garderie;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PresenceController{

  private static final Logger log = LoggerFactory.getLogger(PresenceController.class);

  @Autowired
  private PresenceRepository presenceRepository;

  @Autowired
  private ChildRepository childRepository;

  @GetMapping("/presence")
  public String index(ModelMap model){
    //TODO this group should be fetched from
    //current user instead of
    var myGroup = new Group(1l, "", "");
    var presences = presenceRepository.getPresenceByDateAndGroup(new Date(), myGroup);
    model.addAttribute("presences", presences);
    model.addAttribute("todayAsString", "2018-04-01");
    return "presence/index";
  }

  @PostMapping("/presence")
  public String update(@ModelAttribute("child_id") Long child_id,
                       @ModelAttribute("presence") String presence,
                       @ModelAttribute("date") String date){
    log.debug("I'm posted here ");

    log.info("params {}, {}, {}", child_id, presence, date);
    return "redirect:/presence";
  }


}
