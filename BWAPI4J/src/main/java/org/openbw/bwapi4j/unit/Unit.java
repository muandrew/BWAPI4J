package org.openbw.bwapi4j.unit;

import java.util.Collection;
import java.util.List;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.type.UnitSizeType;
import org.openbw.bwapi4j.type.UnitType;

public interface Unit extends Comparable<Unit> {
  void initialize(int[] unitData, int index, int frame);

  void preUpdate();

  void update(int[] unitData, int index, int frame);

  int getKillCount();

  int getLastSpotted();

  int getInitiallySpotted();

  boolean isA(UnitType type);

  int getId();

  int getLeft();

  int getTop();

  int getRight();

  int getBottom();

  Position getMiddle(Unit unit);

  double getAngle();

  <T extends Unit> T getClosest(Collection<T> group);

  <T extends Unit> List<T> getUnitsInRadius(int radius, Collection<T> group);

  int getX();

  int getY();

  int height();

  int width();

  int tileHeight();

  int tileWidth();

  TilePosition getTilePosition();

  Position getPosition();

  UnitSizeType getSize();

  double getDistance(Position target);

  double getDistance(int x, int y);

  int getDistance(Unit target);

  boolean exists();

  UnitType getType();

  UnitType getInitialType();

  Position getInitialPosition();

  TilePosition getInitialTilePosition();

  boolean isFlying();

  boolean isVisible();

  boolean isSelected();

  int hashCode();

  boolean equals(Object obj);

  String toString();

  int compareTo(Unit otherUnit);
}
