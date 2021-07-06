package com.subastas.virtual.dto.constantes;

public enum Category {

  COMUN,
  ORO,
  PLATA,
  DIAMANTE;

  public boolean isCategoryGreaterOrEqualsThan(Category other) {
    return this.ordinal() >= other.ordinal();
  }
}
