package com.subastas.virtual.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.stereotype.Component;

/** Horrible class that holds in a Map the active tasks required for the application to work automatically
 * Al ser un component de spring, lo incializa cuando se levanta la app
 */
public class AuctionTaskCoordinator {

  private static AuctionTaskCoordinator instance = null;
  private static Map<Integer, TimerTask> map = new HashMap<>();

  private AuctionTaskCoordinator() {
  }

  public static AuctionTaskCoordinator getInstance() {
    if (instance == null) {
      instance = new AuctionTaskCoordinator();
    }
    return instance;
  }

  public static void setTask(Integer id, TimerTask task) {
    map.put(id, task);
  }

  public static TimerTask getTask(Integer id) {
    return map.get(id);
  }
}
