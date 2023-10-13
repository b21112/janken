package oit.is.z1828.kaizi.janken.controller;

import java.security.Principal;
import oit.is.z1828.kaizi.janken.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import oit.is.z1828.kaizi.janken.model.Janken;

@Controller
public class JankenController {
  @Autowired
  private Entry entry;
  private String loginUser;

  @GetMapping("/janken")
  public String Janken(Principal prin, ModelMap model) {
    this.loginUser = prin.getName();

    this.entry.addUser(loginUser);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("entry", entry);

    return "janken.html";
  }

  /*
   * @PostMapping("/janken")
   * public String Janken(@RequestParam String name, ModelMap model) {
   * model.addAttribute("name", name);
   * return "janken.html";
   * }
   */

  @GetMapping("/JankenGame/{hand}")
  public String JankenGame(@PathVariable String hand, ModelMap model, Principal prin) {

    String result;
    String yourHand;
    String cpuHand;

    this.loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("entry", entry);

    yourHand = Janken.setYourHand(hand);
    cpuHand = Janken.setCpuHand();
    result = Janken.judgeGame(yourHand, cpuHand);

    model.addAttribute("yourHand", yourHand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);
    return "janken.html";
  }

}
