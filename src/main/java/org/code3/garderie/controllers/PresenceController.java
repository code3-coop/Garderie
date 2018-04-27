package org.code3.garderie;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.text.SimpleDateFormat;

@Controller
public class PresenceController{

  private static final Logger log = LoggerFactory.getLogger(PresenceController.class);

  @Autowired
  private PresenceRepository presenceRepository;

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private GroupRepository groupRepository;

  @GetMapping("/presence")
  public String index(ModelMap model, HttpSession session){
    log.debug("index");
    var today =  new Date();
    var groupId = (Long) session.getAttribute("group");
    var group = groupRepository.getGroupById(groupId);
    var presences = presenceRepository.getPresenceByDateAndGroup(today, group);
    model.addAttribute("presences", presences);
    model.addAttribute("todayAsString", new SimpleDateFormat("yyyy-MM-dd").format(today));
    return "presence/index";
  }

  @PostMapping("/presence")
  public String update(@ModelAttribute("child_id") Long child_id,
                       @ModelAttribute("state") String state,
                       @ModelAttribute("date") String date,
                       @ModelAttribute("absenceReason") String absenceReason,
                       HttpSession session){
    log.debug("update {}, {}, {}", child_id, date, absenceReason);

    String username = (String) session.getAttribute("username");
    var child = childRepository.getChildById(child_id);
    var presence = new Presence(toDate(date), state, child, absenceReason, username);
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
  //XXX SHAME ON YOU
  private Date toDate(String dateAsString){
    try{
      var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return dateFormat.parse(dateAsString);
    } catch(Exception e){
      log.error("Failed to convert a date to iso format {}", dateAsString);
      return new Date();
    }

  }
}
