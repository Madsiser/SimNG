import java.util.Random;

public class MapGenerator {
    public static int[][] generate() {
        int[][] grid = new int[100][100];
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                // Możesz dostosować zakres wartości, które chcesz umieścić w mapie
                if (random.nextDouble() < 0.1) {
                    grid[i][j] = 5; // Przykładowa wartość specjalna
                } else if (random.nextDouble() < 0.1) {
                    grid[i][j] = 10; // Inna wartość specjalna
                } else if (random.nextDouble() < 0.1) {
                    grid[i][j] = 9; // Jeszcze inna wartość specjalna
                } else {
                    grid[i][j] = 1; // Domyślna wartość
                }
            }
        }

        return grid;
    }
}
