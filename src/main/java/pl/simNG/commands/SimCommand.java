package pl.simNG.commands;

/**
 * Klasa reprezentująca pojedynczy rozkaz w symulacji.
 * Rozkaz składa się z typu oraz danych tego rozkazu.
 * @param type typ rozkazu
 * @param data dane związane z rozkazem
 */
public record SimCommand(SimCommandType type, Object data) {
    /**
     * Zwraca reprezentację rozkazu w formie tekstowej.
     * @return ciąg znaków reprezentujący typ i dane rozkazu
     */
    @Override
    public String toString() {
        return "Command{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }

    public String show() {
        switch (type) {
            case MOVE:
                return "Zadanie: Ruch | Cel: " + data;
            case ATTACK:
                return "Zadanie: Atak | Cel: " + data;
            case DEFEND:
                return "Zadanie: Obrona | Cel: " + data;
            default:
                return "Nieznane zadanie";
        }
    }
}
