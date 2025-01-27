package pl.simNG.commands;

/**
 * Klasa reprezentująca pojedynczy rozkaz w symulacji.
 * Rozkaz składa się z typu oraz danych tego rozkazu.
 * @param type typ rozkazu
 * @param data dane związane z rozkazem
 */
public record Command(CommandType type, Object data) {
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
}
