package com.subastas.virtual.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/** Horrible class that holds in a Map the active tasks required for the application to work automatically
 *
 * Antes, estaba persistinedo el activeTask dentro de una subasta, pero como eso no se persite en la DB (no tiene sentido)
 * tenemos que extraerlo en algo que siga vivo en todo el proceso de la subasta.
 *
 * El candidato ideal sería el servicio de subastas, pero hacerlo desde ahí implica un refator mayor.
 *
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
