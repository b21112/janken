package oit.is.z1828.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class JankenController {

  @GetMapping("/janken")
  public String Janken() {
    return "janken.html";
  }

  @PostMapping("/janken")
  public String Janken(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/JankenGame/{hand}")
  public String JankenGame(@PathVariable String hand, ModelMap model) {

    String result;
    String yourHand;
    String cpuHand = "グー";

    if (hand.equals("pa")) {
      yourHand = "パー";
      result = "win";
    } else if (hand.equals("gu")) {
      yourHand = "グー";
      result = "draw";
    } else {
      yourHand = "チョキ";
      result = "lose";
    }
    model.addAttribute("yourHand", yourHand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);
    return "janken.html";
  }
}
