package pl.simNG.scheduler;

/**
 * Rekord reprezentujący proces w harmonogramie symulacji.
 * Proces jest powiązany z akcją, która ma być wykonywana w regularnych odstępach czasu.
 * @param action akcja do wykonania
 * @param frequency częstotliwość wykonywania procesu (liczba kroków symulacji)
 */
public record SimProcess(Runnable action, int frequency){}
