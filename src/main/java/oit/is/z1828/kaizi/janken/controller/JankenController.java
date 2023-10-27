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
import oit.is.z1828.kaizi.janken.model.MatchInfo;
import oit.is.z1828.kaizi.janken.model.MatchMapper;
import oit.is.z1828.kaizi.janken.model.MatchInfoMapper;
import oit.is.z1828.kaizi.janken.model.User;
import oit.is.z1828.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {
  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchMapper matchMapper;
  @Autowired
  MatchInfoMapper matchInfoMapper;

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
  public String JankenGame(@RequestParam String hand, @RequestParam Integer id, ModelMap model, Principal prin) {

    boolean isActive = true;
    MatchInfo matchInfo = new MatchInfo();
    User user1 = userMapper.selectByname(loginUser);

    matchInfo.setUser1(user1.getId());
    matchInfo.setUser2(id);
    matchInfo.setUser1Hand(hand);
    matchInfo.setActive(isActive);
    matchInfoMapper.insertMatchInfo(matchInfo);

    model.addAttribute("loginUser", loginUser);

    model.addAttribute("matchInfo", matchInfo);

    return "wait.html";
  }

}

// yourHand = Janken.setYourHand(hand);
// cpuHand = Janken.setCpuHand();
// result = Janken.judgeGame(yourHand, cpuHand);
// model.addAttribute("result_flag", result_flag);
// model.addAttribute("user",cpu);
