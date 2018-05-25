package org.code3.garderie;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.Period;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.ZoneId;

@Controller
public class ChildController {

  @Autowired
  ChildRepository childRepository;

  @Autowired
  GroupRepository groupRepository;

  private static final Logger log = LoggerFactory.getLogger(ChildController.class);

  @GetMapping("/child/list")
  public String list(ModelMap model, HttpSession session){
    log.debug("list");
    Long groupId = (Long) session.getAttribute("group");
    var group = groupRepository.getGroupById(groupId);
    var children = childRepository.getChildrenByGroup(group);
    model.addAttribute("children", children);
    model.addAttribute("periods", generateListOfPresencePeriods());

    return "/child/list";
  }

  List<Date> generateListOfPresencePeriods(){

    var now = LocalDate.now();
    var firstDayOfYear = now.with(TemporalAdjusters.firstDayOfYear());
    var lastDayOfYear = now.with(TemporalAdjusters.lastDayOfYear());
    //TODO here we should get the correct number of day
    var firstDayOfWeek = 7;
    var firstPeriodStart = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(firstDayOfWeek)));
    var listOfPeriodStartDate = firstPeriodStart.datesUntil​(lastDayOfYear, Period.ofDays​(14));
    return listOfPeriodStartDate
      .map(ld -> Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()))
      .collect(Collectors.toList());
  }
}
