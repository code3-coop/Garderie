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

    return "/child/list";
  }

  List<Date> generateListOfPresencePeriods(){

    //get first day of the year
    var firstDayOfYear = Calendar.getInstance();
    var d = new Date();
    firstDayOfYear.setTime(d);
    firstDayOfYear.set(Calendar.DAY_OF_MONTH, 1);
    firstDayOfYear.set(Calendar.MONTH, 0);

    //Then fetch the first day of week after this year

    // generate list of start of period after this
    //periods are 4 weeks long

  }
}
