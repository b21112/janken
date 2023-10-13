package oit.is.z1828.kaizi.janken.model;

import java.util.Random;

public class Janken {

  public static String judgeGame(String yourHand, String cpuHand) {
    if (yourHand.equals(cpuHand)) {
      return "draw";
    }
    if (yourHand.equals("グー")) {
      if (cpuHand.equals("チョキ")) {
        return "win";
      }
    }
    if (yourHand.equals("チョキ")) {
      if (cpuHand.equals("パー")) {
        return "win";
      }
    }
    if (yourHand.equals("パー")) {
      if (cpuHand.equals("グー")) {
        return "win";
      }
    }

    return "lose";
  }

  public static String setYourHand(String hand) {
    switch (hand) {
      case ("gu"):
        return "グー";
      case ("choki"):
        return "チョキ";
      default:
        return "パー";
    }
  }

  public static String setCpuHand() {
    Random random = new Random();
    int handSeed = random.nextInt(3);

    switch (handSeed) {
      case (0):
        return "グー";
      case (1):
        return "チョキ";
      default:
        return "パー";
    }
  }
}
