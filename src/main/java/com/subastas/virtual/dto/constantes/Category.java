package com.subastas.virtual.dto.constantes;

public enum Category {

  COMUN,
  PLATA,
  ORO,
  PLATINO,
  ESPECIAL;

  public boolean isCategoryGreaterOrEqualsThan(Category other) {
    return this.ordinal() >= other.ordinal();
  }
}
