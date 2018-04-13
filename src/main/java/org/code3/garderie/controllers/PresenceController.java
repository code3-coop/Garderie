package org.code3.garderie;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.ModelMap;

@Controller
public class PresenceController{

  private static final Logger log = LoggerFactory.getLogger(PresenceController.class);

  @Autowired
  private PresenceRepository presenceRepository;

  @Autowired
  private ChildRepository childRepository;

  @GetMapping("/presence")
  public String index(ModelMap model){
    var myGroup = new Group(1l, "", "");
    var children = childRepository.getChildrenByGroup(myGroup);
    var presences = presenceRepository.getPresenceByDateAndGroup(new Date(), myGroup);
    model.addAttribute("presences", presences);
    return "presence/index";
  }

  // @PostMapping("presence/:groupId/")
  // public String updatePresence(ModelMap model){
  //
  //   return "presence/index"
  // }


}
