package org.code3.garderie;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.format.annotation.DateTimeFormat;

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
                       @ModelAttribute("state") String state,
                       @ModelAttribute("date") String date,
                       @ModelAttribute("absenceReason") String absenceReason){
    log.info("update {}, {}, {}", child_id, date, absenceReason);
    //XXX here the user name should be taken from session
    var currentUserName = "moi";
    var child = childRepository.getChildById(child_id);
    var presence = new Presence(new Date(), state, child, absenceReason, currentUserName);
    presenceRepository.createOrUpdate(presence);

    return "redirect:/presence";
  }

  @GetMapping("/presence/{child_id}/{from}/{to}")
  public String getPresenceByChildBetweenTwoDate(
    @PathVariable("child_id") Long childId,
    @PathVariable("from") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date from,
    @PathVariable("to") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date to
  ){
      log.debug("getPresenceByChildBetweenTwoDate {} {} {}", childId, from, to);
      var child = childRepository.getChildById(childId);
      var presences = presenceRepository.getPresenceByChildBetweenTwoDates(child, from, to);

      return "redirect:/presence";
  }
}
