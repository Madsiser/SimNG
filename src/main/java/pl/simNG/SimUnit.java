package pl.simNG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstrakcyjna klasa reprezentująca pojedynczy środek bojowy w symulacji (np. Abrams).
 */
public abstract class SimUnit {
    /** Numer ID środka bojowego */
    public final int id;
    /** Nazwa środka bojowego (np. "Abrams"). */
    protected String name;
    /** Typ środka bojowego (np. "tank"). */
    protected String type;

    /** Zasięg widzenia. */
    protected Integer visibilityRange;
    /** Zasięg strzału. */
    protected Integer shootingRange;
    /** Szybkość poruszania. */
    protected Integer speed;

    /** Początkowa ilość środków bojowych. */
    protected Integer initialUnits;
    /** Obecnie aktywne środki bojowe. */
    protected Integer activeUnits;

    /** Lista amunicji dla każdego pojedynczego środka bojowego. */
    protected List<Integer> subunitAmmo;
    /** Amunicja początkowa każdego pojedynczego środka bojowego (startowa). */
    protected final int originalInitialAmmunition;

    /** Odchylenie poziome. */
    protected double horizontalDeviation;
    /** Odchylenie pionowe. */
    protected double verticalDeviation;

    /** Szerokość. */
    protected double width;
    /** Wysokość. */
    protected double height;

    /** Przebijalność pancerza. */
    protected double armorPenetration;
    /** Grubość pancerza. */
    protected double armorThickness;

    /** Intensywność ognia (częstotliwość strzałów). */
    protected double fireIntensity;

    /** Referencja do jednostki (grupy), do której należy środek bojowy. */
    private SimGroup parent = null;
    Random random = new Random();
    static int IdCounter = 0;

    /**
     * Konstruktor środka bojowego.
     *
     * @param name                Nazwa (np. "Abrams").
     * @param type                Typ (np. "tank").
     * @param visibilityRange     Zasięg widzenia.
     * @param shootingRange       Zasięg strzału.
     * @param speed               Prędkość poruszania.
     * @param initialUnits        Początkowa liczba środków bojowych.
     * @param initialAmmunition   Amunicja początkowa na 1 środek bojowy.
     * @param horizontalDeviation Odchylenie poziome.
     * @param verticalDeviation   Odchylenie pionowe.
     * @param width               Szerokość celu.
     * @param height              Wysokość celu.
     * @param armorThickness      Grubość pancerza.
     * @param armorPenetration    Przebijalność pocisku.
     * @param fireIntensity       Intensywność ognia (częstotliwość strzałów).
     */
    public SimUnit(String name, String type, Integer visibilityRange, Integer shootingRange, Integer speed, Integer initialUnits,
                   Integer initialAmmunition, double horizontalDeviation, double verticalDeviation, double width, double height,
                   double armorThickness, double armorPenetration, double fireIntensity) {
        this.id = IdCounter++;

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

        this.originalInitialAmmunition = initialAmmunition;
        this.subunitAmmo = new ArrayList<>(initialUnits);
        for (int i = 0; i < initialUnits; i++) {
            this.subunitAmmo.add(initialAmmunition);
        }
    }

    /**
     * Zwraca aktualną amunicję ostatniego aktywnego środka bojowego (jeśli jakieś są).
     * @return aktualna amunicja w ostatnim aktywnym środku bojowym.
     */
    public Integer getCurrentAmmunition() {
        if (activeUnits <= 0) {
            return 0;
        }
        int idx = activeUnits - 1;
        return subunitAmmo.get(idx);
    }

    /**
     * Zwraca łączną aktualną amunicję wszystkich środków bojowych.
     * @return suma amunicji danego środka bojowego.
     */
    public Integer getTotalCurrentAmmunition() {
        int sum = 0;
        for (int ammo : subunitAmmo) {
            sum += ammo;
        }
        return sum;
    }

    /**
     * Zwraca początkową amunicję jednego środka bojowego.
     */
    public Integer getInitialAmmunition() {
        return originalInitialAmmunition;
    }

    /**
     * Zwraca łączną początkową amunicję.
     */
    public Integer getTotalInitialAmmunition() {
        return initialUnits * originalInitialAmmunition;
    }

    /**
     * Ustawia nową wartość amunicji dla każdego pojedynczego środka bojowego.
     * @param newAmmo nowa ilość amunicji.
     */
    public void setCurrentAmmunition(Integer newAmmo) {
        for (int i = 0; i < subunitAmmo.size(); i++) {
            subunitAmmo.set(i, newAmmo);
        }
    }

    /**
     * Sprawdza, czy dany środek bojowy ma amunicję.
     * @param i indeks środka bojowego
     * @return true jeśli ma amunicję, false w p.p.
     */
    public boolean hasAmmo(int i) {
        return (subunitAmmo.get(i) > 0);
    }

    /**
     * Zużycie jednego naboju przez wskazany środek bojowy.
     * @param i indeks środka bojowego
     * @return true jeśli faktycznie zużyto, false jeśli brak amunicji
     */
    public boolean useOneAmmo(int i) {
        int ammo = subunitAmmo.get(i);
        if (ammo > 0) {
            subunitAmmo.set(i, ammo - 1);
            return true;
        }
        return false;
    }

    /**
     * Zabicie jednego środka bojowego (losowo spośród aktywnych).
     * Przenosi jego amunicję na ostatni indeks, ustawia tam 0 i zmniejsza licznik aktywnych.
     * @param random generator liczb pseudolosowych
     * @return ile amunicji straciliśmy (tracimy tyle ile miał dany środek bojowy)
     */
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

    /**
     * Sprawdza, czy grupa środków bojowych jest całkowicie zniszczona.
     * @return true jeśli nie ma aktywnych środków bojowych tego typu.
     */
    public boolean isDestroyed() {
        return activeUnits <= 0;
    }

    /**
     * Sprawdza, czy dana pozycja jest w zasięgu strzału środka bojowego.
     * @param position pozycja celu
     * @return true jeśli jest w zasięgu.
     */
    public boolean inShotRange(SimPosition position){
        if (parent == null) return false;
        return (shootingRange >= this.getParent().getPosition().distanceTo(position));
    }

    /**
     * Ustawia referencję do jednostki(grupy).
     * @param parent referencja do SimGroup
     */
    public void setParent(SimGroup parent) {
        this.parent = parent;
    }

    /**
     * Zwraca jednostkę (grupę z SimGroup), do której należy ten środek bojowy.
     * @return SimGroup rodzic.
     */
    public SimGroup getParent() {
        return parent;
    }

    /**
     * Sprawdza, czy trafiliśmy w obrys celu z uwzględnieniem rozrzutu zależnego od odległości.
     * @param attacker jednostka atakująca (pobieramy odchylenia)
     * @param target jednostka będąca celem (pobieramy wymiary)
     * @param distance odległość do celu
     * @return true jeśli trafiono w cel, false w p.p
     */
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

        boolean hit = (shotX >= -targetWidth / 2.0) && (shotX <= targetWidth / 2.0)
                && (shotY >= -targetHeight / 2.0) && (shotY <= targetHeight / 2.0);

        System.out.println(String.format(
                "HitProbability | Attacker: %s, Target: %s, Distance: %.2f, HorizDev: %.2f, VertDev: %.2f, ShotX: %.2f, ShotY: %.2f, TargetWidth: %.2f, TargetHeight: %.2f, Hit: %s",
                attacker.getName(), target.getName(), distance, currentHorizDev, currentVertDev, shotX, shotY, targetWidth, targetHeight, hit
        ));

        return hit;
    }

    /**
     * Metoda używana przez metodę sprawdzającą czy trafiliśmy w cel (calculateHitProbability)
     * Generuje dwie liczby (x, y) z rozkładu normalnego N(0,1) (Box–Muller).
     * @param random generator liczb pseudolosowych
     * @return tablica [x, y]
     */
    private double[] generateGaussianPair(Random random) {
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        double r = Math.sqrt(-2.0 * Math.log(r1));
        double t = 2.0 * Math.PI * r2;

        double x = r * Math.cos(t);
        double y = r * Math.sin(t);

        return new double[]{x, y};
    }

    /**
     * Sprawdza, czy zniszczyliśmy cel na podstawie przebijalności
     * z uwzględnieniem losowości (+-randomFactor) i porównania z armorThickness.
     * @param attacker jednostka atakująca (pobieramy armorPenetration)
     * @param target jednostka będąca celem (pobieramy armorThickness)
     * @param randomFactor np. 0.25 oznacza +-25% wokół bazowej przebijalności
     * @return true jeśli faktycznie przebito pancerz (zniszczenie), false w p.p
     */
    public boolean calculateDestructionProbability(SimUnit attacker, SimUnit target, double randomFactor) {
        double armorPenetration = attacker.getArmorPenetration();
        double armorThickness = target.getArmorThickness();

        double minPenetration = armorPenetration * (1.0 - randomFactor);
        double maxPenetration = armorPenetration * (1.0 + randomFactor);

        double randomValue = this.random.nextDouble();
        double rolledPenetration = minPenetration + randomValue * (maxPenetration - minPenetration);

        boolean destroyed = rolledPenetration >= armorThickness;

        System.out.println(String.format(
                "DestructionProbability | Attacker: %s, Target: %s, ArmorPen: %.2f, ArmorThick: %.2f, RandomFactor: %.2f, MinPen: %.2f, MaxPen: %.2f, RolledPen: %.2f, Destroyed: %s",
                attacker.getName(), target.getName(), armorPenetration, armorThickness, randomFactor, minPenetration, maxPenetration, rolledPenetration, destroyed
        ));

        return destroyed;
    }

    //GETTERY
    /** Zwraca nazwę środka bojowego (np. "Abrams"). */
    public String getName() {
        return name;
    }

    /** Zwraca typ środka bojowego (np. "tank"). */
    public String getType() {
        return type;
    }

    /** Zwraca zasięg widoczności środka bojowego. */
    public Integer getVisibilityRange() {
        return visibilityRange;
    }

    /** Zwraca zasięg strzału środka bojowego. */
    public Integer getShootingRange() {
        return shootingRange;
    }

    /** Zwraca szybkość środka bojowego. */
    public Integer getSpeed() {
        return speed;
    }

    /** Zwraca początkową ilość środków bojowych. */
    public Integer getInitialUnits() {
        return initialUnits;
    }

    /** Zwraca aktualną ilość środków bojowych. */
    public Integer getActiveUnits() {
        return activeUnits;
    }

    /** Zwraca odchylenie poziome środka bojowego. */
    public double getHorizontalDeviation() {
        return horizontalDeviation;
    }

    /** Zwraca odchylenie pionowe środka bojowego. */
    public double getVerticalDeviation() {
        return verticalDeviation;
    }

    /** Zwraca szerokość środka bojowego. */
    public double getWidth() {
        return width;
    }

    /** Zwraca wysokość środka bojowego. */
    public double getHeight() {
        return height;
    }

    /** Zwraca przebijalność pancerza przez strzały środka bojowego. */
    public double getArmorPenetration() {
        return armorPenetration;
    }

    /** Zwraca grubość pancerza środka bojowego. */
    public double getArmorThickness() {
        return armorThickness;
    }

    /** Zwraca intensywność ognia (częstotliwość strzałów) środka bojowego. */
    public double getFireIntensity() {
        return fireIntensity;
    }
}
