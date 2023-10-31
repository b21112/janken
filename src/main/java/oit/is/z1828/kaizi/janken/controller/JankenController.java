package oit.is.z1828.kaizi.janken.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z1828.kaizi.janken.model.Janken;
import oit.is.z1828.kaizi.janken.model.Match;
import oit.is.z1828.kaizi.janken.model.MatchInfo;
import oit.is.z1828.kaizi.janken.model.MatchMapper;
import oit.is.z1828.kaizi.janken.model.MatchInfoMapper;
import oit.is.z1828.kaizi.janken.model.User;
import oit.is.z1828.kaizi.janken.model.UserMapper;
import oit.is.z1828.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {
  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchMapper matchMapper;
  @Autowired
  MatchInfoMapper matchInfoMapper;

  private String loginUser;

  private final Logger logger = LoggerFactory.getLogger(JankenController.class);

  @Autowired
  private AsyncKekka kekka;

  @GetMapping("/janken")
  public String Janken(Principal prin, ModelMap model) {
    this.loginUser = prin.getName();

    ArrayList<User> users = userMapper.selectAllByUsers();
    model.addAttribute("users", users);
    ArrayList<Match> matches = matchMapper.selectAllByMatches();
    ArrayList<MatchInfo> matchInfos = matchInfoMapper.selectByActive(true);

    model.addAttribute("matches", matches);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("matchInfos", matchInfos);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam Integer id, ModelMap model, Principal prin) {
    this.loginUser = prin.getName();
    User user = userMapper.selectById(id);
    boolean result_flag = false;

    model.addAttribute("user", user);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("result_flag", result_flag);

    return "match.html";
  }

  @GetMapping("/fight")
  public String JankenGame(@RequestParam String hand, @RequestParam Integer id, ModelMap model, Principal prin) {
    this.loginUser = prin.getName();
    MatchInfo matchInfo = new MatchInfo();
    Match match = new Match();
    User user1 = userMapper.selectByname(loginUser);
    ArrayList<MatchInfo> checkInfos = new ArrayList<>();
    boolean insert_flag = true;

    matchInfo.setUser1(user1.getId());
    matchInfo.setUser2(id);
    matchInfo.setUser1Hand(hand);
    matchInfo.setActive(true);
    // 挿入するか分岐
    checkInfos = matchInfoMapper.selectByActive(true);
    for (MatchInfo checkInfo : checkInfos) {
      if (checkInfo.getUser2() == user1.getId() && checkInfo.getUser1() == id) {
        insert_flag = false;
        checkInfo.setActive(false);
        matchInfoMapper.updateActive(checkInfo);
      }
      match.setUser1(checkInfo.getUser1());
      match.setUser2(checkInfo.getUser2());
      match.setUser1Hand(checkInfo.getUser1Hand());
      match.setUser2Hand(hand);
      match.setActive(true);
    }

    if (insert_flag) {
      matchInfoMapper.insertMatchInfo(matchInfo);
    } else {
      matchMapper.insertMatch(match);

    }

    model.addAttribute("loginUser", loginUser);
    model.addAttribute("match", match);
    model.addAttribute("matchInfo", matchInfo);

    return "wait.html";
  }

  @GetMapping("/result")
  public SseEmitter pushResult() {
    // infoレベルでログを出力する
    logger.info("pushResult");

    // finalは初期化したあとに再代入が行われない変数につける（意図しない再代入を防ぐ）
    final SseEmitter sseEmitter = new SseEmitter();

    try {
      this.kekka.asyncShowMatch(sseEmitter);
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
      sseEmitter.complete();
    }
    return sseEmitter;
  }

  @GetMapping("step1")
  public SseEmitter pushCount() {
    // infoレベルでログを出力する
    logger.info("pushCount");

    // finalは初期化したあとに再代入が行われない変数につける（意図しない再代入を防ぐ）
    final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);//
    // 引数にLongの最大値をTimeoutとして指定する

    try {
      this.kekka.count(emitter);
    } catch (IOException e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
      emitter.complete();
    }
    return emitter;
  }

}

// yourHand = Janken.setYourHand(hand);
// cpuHand = Janken.setCpuHand();
// result = Janken.judgeGame(yourHand, cpuHand);
// model.addAttribute("result_flag", result_flag);
// model.addAttribute("user",cpu);
