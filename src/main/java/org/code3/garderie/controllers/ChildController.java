package org.code3.garderie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import org.springframework.ui.ModelMap;

@Controller
public class ChildController {

  @Autowired
  ChildRepository childRepository;

  @GetMapping("/child/list")
  public String list(ModelMap model){
    var group = new Group(1l, "", "");
    var childs = childRepository.getChildrenByGroup(group);
    model.addAttribute("children", childRepository.getAllChildren());
    return "/child/list";
  }

}
