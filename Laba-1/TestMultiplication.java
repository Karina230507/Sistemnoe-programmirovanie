public class TestMultiplication {
    public static void main(String[] args) {
        // Тестовые случаи
        int[][] testCases = {
                {5, 3}, {4, 7}, {12, 12}, {0, 5}, {5, 0},
                {-3, 4}, {3, -4}, {-3, -4}, {1, 100}, {100, 1}
        };

        System.out.println("Тестирование методов умножения:\n");
        System.out.printf("%-10s %-10s %-15s %-15s %-15s %-15s%n",
                "a", "b", "Ожидаемый", "Рекурсивный", "Итеративный", "Быстрый");
        System.out.println("=".repeat(80));

        for (int[] test : testCases) {
            int a = test[0];
            int b = test[1];
            int expected = a * b; // Для проверки

            int recursive = MultiplicationWithoutMultiply.multiplyRecursive(a, b);
            int iterative = MultiplicationWithoutMultiply.multiplyIterative(a, b);
            int fast = FastMultiplication.multiplyFast(a, b);

            System.out.printf("%-10d %-10d %-15d %-15d %-15d %-15d%n",
                    a, b, expected, recursive, iterative, fast);
        }

        // Тест производительности для больших чисел
        System.out.println("\nПроизводительность для больших чисел:");
        int bigA = 12345;
        int bigB = 6789;

        long startTime = System.nanoTime();
        int result1 = MultiplicationWithoutMultiply.multiplyIterative(bigA, bigB);
        long time1 = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int result2 = FastMultiplication.multiplyFast(bigA, bigB);
        long time2 = System.nanoTime() - startTime;

        System.out.printf("Итеративный: %d нс, результат: %d%n", time1, result1);
        System.out.printf("Быстрый:     %d нс, результат: %d%n", time2, result2);
    }
}