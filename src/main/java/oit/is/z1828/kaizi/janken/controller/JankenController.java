package oit.is.z1828.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z1828.kaizi.janken.model.Janken;
import oit.is.z1828.kaizi.janken.model.Match;
import oit.is.z1828.kaizi.janken.model.MatchMapper;
import oit.is.z1828.kaizi.janken.model.User;
import oit.is.z1828.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {
  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchMapper matchMapper;

  private String loginUser;

  @GetMapping("/janken")
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
    boolean result_flag = false;

    model.addAttribute("user", user);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("result_flag", result_flag);

    return "match.html";
  }

  @GetMapping("/fight")
  public String JankenGame(@RequestParam String hand, ModelMap model, Principal prin) {

    String result;
    String yourHand;
    String cpuHand;
    boolean result_flag = true;
    Match match = new Match();
    User cpu = userMapper.selectById(1);
    User user = userMapper.selectByname(loginUser);

    yourHand = Janken.setYourHand(hand);
    cpuHand = Janken.setCpuHand();
    result = Janken.judgeGame(yourHand, cpuHand);

    match.setUser1(user.getId());
    match.setUser2(cpu.getId());
    match.setUser1Hand(yourHand);
    match.setUser2Hand(cpuHand);
    match.setResult(result);
    matchMapper.insertMatch(match);

    model.addAttribute("user", cpu);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("result_flag", result_flag);
    model.addAttribute("match", match);

    return "match.html";
  }

}
