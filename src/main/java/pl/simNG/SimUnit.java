package pl.simNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class SimUnit {
    protected String name;
    protected String type;

    protected Integer visibilityRange;
    protected Integer shootingRange;
    protected Integer speed;

    protected Integer initialUnits;
    protected Integer activeUnits;
    protected List<Integer> subunitAmmo;
    protected final int originalInitialAmmunition;

    protected double hitProbabilityMin;
    protected double hitProbabilityMax;
    protected double destructionProbabilityMin;
    protected double destructionProbabilityMax;
    protected double fireIntensity;
    protected double criticalLevel;

    private SimGroup parent = null;
    Random random = new Random();

    public SimUnit(String name,
                   String type,
                   Integer visibilityRange,
                   Integer shootingRange,
                   Integer speed,
                   Integer initialUnits,
                   Integer initialAmmunition,
                   double hitProbabilityMin,
                   double hitProbabilityMax,
                   double destructionProbabilityMin,
                   double destructionProbabilityMax,
                   double fireIntensity) {

        if (name == null || type == null || visibilityRange == null || shootingRange == null || speed == null || initialUnits == null || initialAmmunition == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }

        this.name = name;
        this.type = type;
        this.visibilityRange = visibilityRange;
        this.shootingRange = shootingRange;
        this.speed = speed;
        this.initialUnits = initialUnits;
        this.activeUnits = initialUnits;

        this.hitProbabilityMin = hitProbabilityMin;
        this.hitProbabilityMax = hitProbabilityMax;
        this.destructionProbabilityMin = destructionProbabilityMin;
        this.destructionProbabilityMax = destructionProbabilityMax;
        this.fireIntensity = fireIntensity;
        this.criticalLevel = 0.3;

        this.originalInitialAmmunition = initialAmmunition;
        this.subunitAmmo = new ArrayList<>(initialUnits);
        for (int i = 0; i < initialUnits; i++) {
            this.subunitAmmo.add(initialAmmunition);
        }
    }

    //Aktualna amunicja dla danej podjednostki
    public Integer getCurrentAmmunition() {
        if (activeUnits <= 0) {
            return 0;
        }
        int idx = activeUnits - 1;
        return subunitAmmo.get(idx);
    }

    //Łączna aktualna amunicja
    public Integer getTotalCurrentAmmunition() {
        int sum = 0;
        for (int ammo : subunitAmmo) {
            sum += ammo;
        }
        return sum;
    }

    //Początkowa amunicja dla jednej podjednostki
    public Integer getInitialAmmunition() {
        return originalInitialAmmunition;
    }

    //Łączna początkowa amunicja
    public Integer getTotalInitialAmmunition() {
        return initialUnits * originalInitialAmmunition;
    }

    //Ustawienie ilosc aktualnej amunicji dla podjednostki
    public void setCurrentAmmunition(Integer newAmmo) {
        for (int i = 0; i < subunitAmmo.size(); i++) {
            subunitAmmo.set(i, newAmmo);
        }
    }

    //Sprawdzanie czy podjednostka ma amunicje
    public boolean hasAmmo(int i) {
        return (subunitAmmo.get(i) > 0);
    }

    //Zużycie jednego naboju przez podjednostke
    public boolean useOneAmmo(int i) {
        int ammo = subunitAmmo.get(i);
        if (ammo > 0) {
            subunitAmmo.set(i, ammo - 1);
            return true;
        }
        return false;
    }

    //Zabicie jednej podjednostki
    public int killOneSubunit(Random random) {
        if (activeUnits <= 0) {
            return 0;
        }
        int idxToKill = random.nextInt(activeUnits);
        int lostAmmo = subunitAmmo.get(idxToKill);

        int lastAliveIndex = activeUnits - 1;
        subunitAmmo.set(idxToKill, subunitAmmo.get(lastAliveIndex));
        subunitAmmo.set(lastAliveIndex, 0);

        activeUnits--;
        return lostAmmo;
    }

    //Czy jednostka jest zniszczona (liczba podjednostek < 0)
    public boolean isDestroyed() {
        return activeUnits <= 0;
    }

    //Czy jest w zasięgu
    public boolean inShotRange(SimPosition position){
        if (parent == null) return false;
        return (shootingRange >= this.getParent().getPosition().distanceTo(position));
    }

    //Ustawienie rodzica
    public void setParent(SimGroup parent) {
        this.parent = parent;
    }

    //Pobranie rodzica
    public SimGroup getParent() {
        return parent;
    }

    //Obliczenie prawdopodobieństwa trafienia
    public double calculateHitProbability(String targetType, double distance) {
        double targetSizeModifier = getTargetSizeModifier(targetType);
        double hitProbabilityBase = hitProbabilityMin +
                random.nextDouble() * (hitProbabilityMax - hitProbabilityMin);
        double distanceFactor = 1.0 / (1.0 + distance / shootingRange);
        double hitProbability = hitProbabilityBase * targetSizeModifier * distanceFactor;
        return Math.max(0.0, Math.min(1.0, hitProbability));
    }

    //Pobranie modyfikatora wielkości jednostki potrzebnego do obliczenia prawdopodobieństwa trafienia
    private double getTargetSizeModifier(String targetType) {
        switch (targetType.toLowerCase()) {
            case "tank":
                return 1.2;
            case "artillery":
                return 1.1;
            case "combat vehicle":
                return 1.0;
            case "soldier":
                return 0.8;
            default:
                return 1.0;
        }
    }

    //Obliczenie prawdopodobieństwa zniszczenia
    public double calculateDestructionProbability(SimUnit attacker, String targetType) {
        double destructionModifier = getDestructionModifier(targetType);
        double destructionProbabilityBase = attacker.getDestructionProbabilityMin() +
                random.nextDouble() *
                        (attacker.getDestructionProbabilityMax() - attacker.getDestructionProbabilityMin());

        double destructionProbability = destructionProbabilityBase * destructionModifier;
        return Math.max(0.0, Math.min(1.0, destructionProbability));
    }

    //Pobranie modyfikatora wytrzymałości jednostki potrzebnego do obliczenia prawdopodobieństwa zniszczenia
    private double getDestructionModifier(String targetType) {
        switch (targetType.toLowerCase()) {
            case "tank":
                return 0.2;
            case "artillery":
                return 0.5;
            case "combat vehicle":
                return 0.7;
            case "soldier":
                return 1.0;
            default:
                return 0.5;
        }
    }

    //GETTERY

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getVisibilityRange() {
        return visibilityRange;
    }

    public Integer getShootingRange() {
        return shootingRange;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getInitialUnits() {
        return initialUnits;
    }

    public Integer getActiveUnits() {
        return activeUnits;
    }

    public double getHitProbabilityMin() {
        return hitProbabilityMin;
    }

    public double getHitProbabilityMax() {
        return hitProbabilityMax;
    }

    public double getDestructionProbabilityMin() {
        return destructionProbabilityMin;
    }

    public double getDestructionProbabilityMax() {
        return destructionProbabilityMax;
    }

    public double getFireIntensity() {
        return fireIntensity;
    }

    public double getCriticalLevel() {
        return criticalLevel;
    }
}
