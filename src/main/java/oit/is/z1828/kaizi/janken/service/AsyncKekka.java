package oit.is.z1828.kaizi.janken.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oit.is.z1828.kaizi.janken.model.MatchInfoMapper;
import oit.is.z1828.kaizi.janken.model.MatchMapper;
import oit.is.z1828.kaizi.janken.model.MatchInfo;
import oit.is.z1828.kaizi.janken.model.Match;

@Service
public class AsyncKekka {

  int count = 1;
  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  @Async
  public void asyncShowMatch(SseEmitter emitter) throws IOException {
    boolean dbUpdated = true;
    logger.info("showMatch start");
    try {
      Match match = new Match();
      while (dbUpdated) {
        match = matchMapper.selectByActive(true);
        if (match != null) {
          emitter.send("Match: id " + match.getId() + " user1: " + match.getUser1() + " user2: " + match.getUser2()
              + " user1Hand: " + match.getUser1Hand() + " user2Hand: " + match.getUser2Hand());

          TimeUnit.MILLISECONDS.sleep(1000);
          match.setActive(false);
          matchMapper.updateActive(match);
        }

      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
  }

  @Async
  public void count(SseEmitter emitter) throws IOException {
    logger.info("count start");
    try {
      while (true) {// 無限ループ
        logger.info("send:" + count);
        // sendによってcountがブラウザにpushされる
        emitter.send(count);
        count++;
        // 1秒STOP
        TimeUnit.SECONDS.sleep(1);
      }
    } catch (InterruptedException e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
  }
}
