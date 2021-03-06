////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (C) 2017-2018 OpenBW Team
//
//    This file is part of BWAPI4J.
//
//    BWAPI4J is free software: you can redistribute it and/or modify
//    it under the terms of the Lesser GNU General Public License as published
//    by the Free Software Foundation, version 3 only.
//
//    BWAPI4J is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with BWAPI4J.  If not, see <http://www.gnu.org/licenses/>.
//
////////////////////////////////////////////////////////////////////////////////

#include "BridgeMap.h"
#include <BWAPI.h>
#include "Logger.h"
#include "org_openbw_bwapi4j_BWMapImpl.h"

void BridgeMap::initialize(JNIEnv *env, jclass jc, jobject bwObject, jclass bwMapClass) {
  LOGGER("Reading map information...");

  jfieldID bwMapField = env->GetFieldID(jc, "bwMap", "Lorg/openbw/bwapi4j/BWMapImpl;");
  jobject bwMap = env->GetObjectField(bwObject, bwMapField);

  // set mapHash
  jfieldID mapHashField = env->GetFieldID(bwMapClass, "mapHash", "Ljava/lang/String;");
  jstring mapHash = env->NewStringUTF(BWAPI::Broodwar->mapHash().c_str());
  env->SetObjectField(bwMap, mapHashField, mapHash);

  // set mapFileName
  jfieldID mapFileNameField = env->GetFieldID(bwMapClass, "mapFileName", "Ljava/lang/String;");
  jstring mapFileName = env->NewStringUTF(BWAPI::Broodwar->mapFileName().c_str());
  env->SetObjectField(bwMap, mapFileNameField, mapFileName);

  // set mapName
  jfieldID mapNameField = env->GetFieldID(bwMapClass, "mapName", "Ljava/lang/String;");
  jstring mapName = env->NewStringUTF(BWAPI::Broodwar->mapName().c_str());
  env->SetObjectField(bwMap, mapNameField, mapName);

  // set width
  jfieldID mapWidthField = env->GetFieldID(bwMapClass, "width", "I");
  env->SetIntField(bwMap, mapWidthField, (jint)BWAPI::Broodwar->mapWidth());

  // set height
  jfieldID mapHeightField = env->GetFieldID(bwMapClass, "height", "I");
  env->SetIntField(bwMap, mapHeightField, (jint)BWAPI::Broodwar->mapHeight());

  // set groundInfo (tile resolution)
  jfieldID groundInfoField = env->GetFieldID(bwMapClass, "groundInfo", "[[I");
  jobjectArray groundInfo2DArray = env->NewObjectArray(BWAPI::Broodwar->mapWidth(), env->GetObjectClass(env->NewIntArray(BWAPI::Broodwar->mapHeight())), 0);
  for (int i = 0; i < BWAPI::Broodwar->mapWidth(); ++i) {
    jint *groundInfo = new jint[BWAPI::Broodwar->mapHeight()];
    for (int j = 0; j < BWAPI::Broodwar->mapHeight(); ++j) {
      groundInfo[j] = BWAPI::Broodwar->getGroundHeight(i, j);
    }
    jintArray groundInfoArray = env->NewIntArray(BWAPI::Broodwar->mapHeight());
    env->SetIntArrayRegion(groundInfoArray, 0, BWAPI::Broodwar->mapHeight(), groundInfo);
    env->SetObjectArrayElement(groundInfo2DArray, i, groundInfoArray);
  }
  env->SetObjectField(bwMap, groundInfoField, groundInfo2DArray);

  // set walkabilityInfo (mini-tile resolution)
  jfieldID walkabilityInfoField = env->GetFieldID(bwMapClass, "walkabilityInfo", "[[I");
  jobjectArray walkabilityInfo2DArray =
      env->NewObjectArray(BWAPI::Broodwar->mapWidth() * 4, env->GetObjectClass(env->NewIntArray(BWAPI::Broodwar->mapHeight() * 4)), 0);
  for (int i = 0; i < BWAPI::Broodwar->mapWidth() * 4; ++i) {
    jint *walkabilityInfo = new jint[BWAPI::Broodwar->mapHeight() * 4];
    for (int j = 0; j < BWAPI::Broodwar->mapHeight() * 4; ++j) {
      walkabilityInfo[j] = BWAPI::Broodwar->isWalkable(i, j) ? 1 : 0;
    }
    jintArray walkabilityInfoArray = env->NewIntArray(BWAPI::Broodwar->mapHeight() * 4);
    env->SetIntArrayRegion(walkabilityInfoArray, 0, BWAPI::Broodwar->mapHeight() * 4, walkabilityInfo);
    env->SetObjectArrayElement(walkabilityInfo2DArray, i, walkabilityInfoArray);
  }
  env->SetObjectField(bwMap, walkabilityInfoField, walkabilityInfo2DArray);

  // set starting locations
  jobject startLocationsList = env->GetObjectField(bwMap, env->GetFieldID(bwMapClass, "startLocations", "Ljava/util/ArrayList;"));

  for (BWAPI::TilePosition tilePosition : BWAPI::Broodwar->getStartLocations()) {
    jobject startLocation = env->NewObject(javaRefs.tilePositionClass, javaRefs.tilePositionConstructor, tilePosition.x, tilePosition.y);
    env->CallObjectMethod(startLocationsList, javaRefs.arrayListClass_add, startLocation);
  }

  if (env->ExceptionOccurred()) {
    env->ExceptionDescribe();
    return;
  }

  LOGGER("Reading map information... done");
}

// TODO: Check if "isBuildable" is static. If yes, move this into a native init method such as "int[] getIsBuildableData()" and call from Java during onStart.
JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1isBuildable(JNIEnv *, jobject, jint tileX, jint tileY, jboolean considerBuildings) {
  return BWAPI::Broodwar->isBuildable(tileX, tileY, considerBuildings) ? 1 : 0;
}

JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1isExplored(JNIEnv *, jobject, jint tileX, jint tileY) {
  return BWAPI::Broodwar->isExplored(tileX, tileY) ? 1 : 0;
}

JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1isVisible(JNIEnv *, jobject, jint tileX, jint tileY) {
  return BWAPI::Broodwar->isVisible(tileX, tileY) ? 1 : 0;
}

JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1hasPath(JNIEnv *, jobject, jint x1, jint y1, jint x2, jint y2) {
  return BWAPI::Broodwar->hasPath(BWAPI::Position(x1, y1), BWAPI::Position(x2, y2)) ? 1 : 0;
}

JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1canBuildHere__III(JNIEnv *, jobject, jint x, jint y, jint typeId) {
  return BWAPI::Broodwar->canBuildHere(BWAPI::TilePosition(x, y), (BWAPI::UnitType)typeId) ? 1 : 0;
}

JNIEXPORT jint JNICALL Java_org_openbw_bwapi4j_BWMapImpl__1canBuildHere__IIII(JNIEnv *, jobject, jint x, jint y, jint typeId, jint builderId) {
  return BWAPI::Broodwar->canBuildHere(BWAPI::TilePosition(x, y), (BWAPI::UnitType)typeId, BWAPI::Broodwar->getUnit(builderId)) ? 1 : 0;
}

JNIEXPORT jintArray JNICALL Java_org_openbw_bwapi4j_BWMapImpl_getCreepData_1native(JNIEnv *env, jobject) {
  int index = 0;

  for (int tileX = 0; tileX < BWAPI::Broodwar->mapWidth(); ++tileX) {
    for (int tileY = 0; tileY < BWAPI::Broodwar->mapHeight(); ++tileY) {
      const auto currentTilePosition = BWAPI::TilePosition(tileX, tileY);

      intBuf[index++] = BWAPI::Broodwar->hasCreep(currentTilePosition);
    }
  }

  jintArray result = env->NewIntArray(index);
  env->SetIntArrayRegion(result, 0, index, intBuf);
  return result;
}

JNIEXPORT jintArray JNICALL Java_org_openbw_bwapi4j_InteractionHandler_getNukeDotsData_1native(JNIEnv *env, jobject) {
  int index = 0;

  for (const auto nukeDotPosition : BWAPI::Broodwar->getNukeDots()) {
    intBuf[index++] = nukeDotPosition.x;
    intBuf[index++] = nukeDotPosition.y;
  }

  jintArray result = env->NewIntArray(index);
  env->SetIntArrayRegion(result, 0, index, intBuf);
  return result;
}
