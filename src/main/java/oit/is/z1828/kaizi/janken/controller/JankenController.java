package oit.is.z1828.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

import oit.is.z1828.kaizi.janken.model.Janken;
import oit.is.z1828.kaizi.janken.model.Entry;
import oit.is.z1828.kaizi.janken.model.Match;
import oit.is.z1828.kaizi.janken.model.MatchMapper;
import oit.is.z1828.kaizi.janken.model.User;
import oit.is.z1828.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private MatchMapper matchMapper;
  private Entry entry;
  private String loginUser;

  @GetMapping("/janken")
  @Transactional
  public String Janken(Principal prin, ModelMap model) {
    this.loginUser = prin.getName();

    ArrayList<User> users = userMapper.selectAllByUsers();
    model.addAttribute("users", users);
    ArrayList<Match> matches = matchMapper.selectAllByMatches();
    model.addAttribute("matches", matches);
    model.addAttribute("loginUser", loginUser);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam Integer id, ModelMap model, Principal prin) {
    User user = userMapper.selectById(id);

    model.addAttribute("user", user);
    model.addAttribute("loginUser", loginUser);

    return "match.html";
  }

  @GetMapping("/JankenGame/{hand}")
  public String JankenGame(@PathVariable String hand, ModelMap model, Principal prin) {

    String result;
    String yourHand;
    String cpuHand;

    User user = userMapper.selectById(1);

    model.addAttribute("user", user);
    model.addAttribute("loginUser", loginUser);

    yourHand = Janken.setYourHand(hand);
    cpuHand = Janken.setCpuHand();
    result = Janken.judgeGame(yourHand, cpuHand);

    model.addAttribute("yourHand", yourHand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);
    return "match.html";
  }

}
