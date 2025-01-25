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

    protected double horizontalDeviation;
    protected double verticalDeviation;
    protected double width;
    protected double height;

    protected double armorPenetration;
    protected double armorThickness;

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
                   double horizontalDeviation,
                   double verticalDeviation,
                   double width,
                   double height,
                   double armorThickness,
                   double armorPenetration,
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

        this.horizontalDeviation = horizontalDeviation;
        this.verticalDeviation = verticalDeviation;
        this.width = width;
        this.height = height;

        this.armorThickness = armorThickness;
        this.armorPenetration = armorPenetration;

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
    public boolean calculateHitProbability(SimUnit attacker, SimUnit target, double distance) {
        double a1 = attacker.getHorizontalDeviation()/attacker.getShootingRange();
        double a2  = attacker.getVerticalDeviation()/attacker.getShootingRange();

        double currentHorizDev=a1*distance;
        double currentVertDev=a2*distance;

        double[] gaussianPair = generateGaussianPair(this.random);
        double shotX = gaussianPair[0] * currentHorizDev;
        double shotY = gaussianPair[1] * currentVertDev;

        double targetWidth = target.getWidth();
        double targetHeight = target.getHeight();

        return (shotX >= -targetWidth  / 2.0) && (shotX <= targetWidth  / 2.0)
                && (shotY >= -targetHeight / 2.0) && (shotY <= targetHeight / 2.0);
    }

    private double[] generateGaussianPair(Random random) {
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        double r = Math.sqrt(-2.0 * Math.log(r1));
        double t = 2.0 * Math.PI * r2;

        double x = r * Math.cos(t);
        double y = r * Math.sin(t);

        return new double[]{x, y};
    }

    //Obliczenie prawdopodobieństwa zniszczenia
    public boolean calculateDestructionProbability(SimUnit attacker, SimUnit target, double randomFactor) {
        double armorPenetration = attacker.getArmorPenetration();
        double armorThickness = target.getArmorThickness();

        double minPenetration = armorPenetration * (1.0 - randomFactor);
        double maxPenetration = armorPenetration * (1.0 + randomFactor);

        double randomValue = this.random.nextDouble();
        double rolledPenetration = minPenetration + randomValue * (maxPenetration - minPenetration);
        return (rolledPenetration >= armorThickness);
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

    public double getHorizontalDeviation() {
        return horizontalDeviation;
    }

    public double getVerticalDeviation() {
        return verticalDeviation;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getArmorPenetration() {
        return armorPenetration;
    }

    public double getArmorThickness() {
        return armorThickness;
    }

    public double getFireIntensity() {
        return fireIntensity;
    }

    public double getCriticalLevel() {
        return criticalLevel;
    }
}
