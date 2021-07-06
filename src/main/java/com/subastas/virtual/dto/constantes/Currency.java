package com.subastas.virtual.dto.constantes;

public enum Currency {
  PESO("$"),
  DOLAR("U$D");

  private String symbol;

  Currency(String symbol) {
    this.symbol = symbol;
  }
}
