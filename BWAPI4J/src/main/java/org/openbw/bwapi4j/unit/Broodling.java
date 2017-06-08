package org.openbw.bwapi4j.unit;

import java.util.Map;

import org.openbw.bwapi4j.type.UnitType;

public class Broodling extends MobileUnit implements Organic {

    protected Broodling(int id) {
        
        super(id, UnitType.Zerg_Broodling);
    }
    
    @Override
    public int initialize(int[] unitData, int index, Map<Integer, Unit> allUnits) {

        return super.initialize(unitData, index, allUnits);
    }

    @Override
    public int update(int[] unitData, int index) {

        return super.update(unitData, index);
    }
}
