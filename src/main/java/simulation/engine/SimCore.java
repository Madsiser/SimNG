package simulation.engine;

import java.util.ArrayList;
import java.util.List;

public class SimCore {
    private final List<SimUnit> units;
    private int currentStep = 0;

    public SimCore(List<SimUnit> units) {
        this.units = units;
    }

    public void runSimulation() {
        while (true) {
            currentStep++;
            for (SimUnit unit : units) {
                List<SimUnit> visibleUnits = getVisibleUnits(unit);
                unit.updateVisibleUnits(visibleUnits);
                unit.runStep();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private List<SimUnit> getVisibleUnits(SimUnit unit) {
        List<SimUnit> visibleUnits = new ArrayList<>();
        for (SimUnit otherUnit : units) {
            if (otherUnit != unit && isInVisibilityRange(unit, otherUnit)) {
                visibleUnits.add(otherUnit);
            }
        }
        return visibleUnits;
    }

    private boolean isInVisibilityRange(SimUnit unit, SimUnit otherUnit) {

        return true;
    }

    private List<SimUnit> getShotingUnits(SimUnit unit) {
        List<SimUnit> visibleUnits = new ArrayList<>();
        for (SimUnit otherUnit : units) {
            if (otherUnit != unit && isInVisibilityRange(unit, otherUnit)) {
                visibleUnits.add(otherUnit);
            }
        }
        return visibleUnits;
    }

    private boolean isInShotingRange(SimUnit unit, SimUnit otherUnit) {

        return true;
    }
}
