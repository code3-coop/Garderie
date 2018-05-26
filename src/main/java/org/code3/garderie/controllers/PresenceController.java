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
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.TreeMap;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Controller
public class PresenceController{

  private static final Logger log = LoggerFactory.getLogger(PresenceController.class);

  @Autowired
  private PresenceRepository presenceRepository;

  @Autowired
  private ParentsRepository parentsRepository;

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
                       @ModelAttribute("dayPart") String dayPart,
                       HttpSession session){
    log.debug("update {}, {}, {}, {}", child_id, date, absenceReason, dayPart);

    String username = (String) session.getAttribute("username");
    var child = childRepository.getChildById(child_id);
    var presence = new Presence(toDate(date), state, child, absenceReason, username, dayPart);
    presenceRepository.createOrUpdate(presence);

    return "redirect:/presence";
  }

  @GetMapping("/presence/{child_id}")
  public String getPresenceByChildForPeriod(
    @PathVariable("child_id") Long childId,
    @RequestParam(required= true, value="period_start") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) Date periodStart,
    ModelMap model
  ){
      log.debug("getPresenceByChildForPeriod {} {}", childId, periodStart);
      var child = childRepository.getChildById(childId);

      var periodEndLocalDate = periodStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(27, ChronoUnit.DAYS);
      var periodEnd = Date.from(periodEndLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

      var presences = presenceRepository.getPresenceByChildBetweenTwoDates(child, periodStart, periodEnd);
      var parents = parentsRepository.getParentsByChild(child);
      model.addAttribute("child", child);
      model.addAttribute("presences", groupPresenceByWeeks(presences));
      model.addAttribute("parents", parents);
      model.addAttribute("from", periodStart);
      model.addAttribute("to", periodEnd);
      return "presence/calendar.html";
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

  private Map<Date, List<Presence>> groupPresenceByWeeks(List<Presence> presences){
    var m = presences
    .stream()
    .collect(Collectors.groupingBy((presence) -> this.firstDayOfWeek(presence.getDate())));

    var sortedMap = new TreeMap<Date, List<Presence>>(Comparator.comparing(Date::getTime));
    sortedMap.putAll(m);
    return sortedMap;

  }

  private Date firstDayOfWeek(Date date){
    var firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
    var firstDayOfWeekDate = Calendar.getInstance();
    firstDayOfWeekDate.setTime(date);
    firstDayOfWeekDate.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
    return firstDayOfWeekDate.getTime();
  }
}
