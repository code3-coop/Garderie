package org.code3.garderie;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChildController {

  @Autowired
  ChildRepository childRepository;

  private static final Logger log = LoggerFactory.getLogger(ChildController.class);

  @GetMapping("/child/list")
  public String list(ModelMap model){
    log.debug("list ");
    var group = new Group(1l, "", "");
    var childs = childRepository.getChildrenByGroup(group);

    model.addAttribute("children", childRepository.getAllChildren());

    return "/child/list";
  }
}
