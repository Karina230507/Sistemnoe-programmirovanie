public class Main {

    /**
     * Умножение с использованием битовых операций
     * Временная сложность: O(log n), где n = min(|a|, |b|)
     */
    public static int multiplyBitwise(int a, int b) {
        // Базовые случаи
        if (a == 0 || b == 0) return 0;

        // Обработка специальных случаев
        if (a == 1) return b;
        if (b == 1) return a;
        if (a == -1) return -b;
        if (b == -1) return -a;

        // Определяем знак результата
        boolean negative = (a < 0) ^ (b < 0); // XOR для определения знака

        // Работаем с абсолютными значениями
        long absA = Math.abs((long)a); // Используем long для избежания переполнения
        long absB = Math.abs((long)b);

        // Выбираем меньший множитель для оптимизации
        if (absA < absB) {
            return multiplyBitwise(b, a);
        }

        long result = 0;

        // Умножение через битовые операции
        while (absB > 0) {
            // Если текущий бит b установлен, добавляем a с соответствующим сдвигом
            if ((absB & 1) == 1) {
                result += absA;
                // Проверка на переполнение
                if (result > Integer.MAX_VALUE) {
                    throw new ArithmeticException("Integer overflow");
                }
            }

            // Сдвигаем a влево (умножаем на 2)
            absA <<= 1;
            // Сдвигаем b вправо (делим на 2)
            absB >>= 1;

            // Проверка на переполнение при сдвиге
            if (absA > Integer.MAX_VALUE && absB > 0) {
                throw new ArithmeticException("Integer overflow");
            }
        }

        // Применяем знак
        int finalResult = (int) result;
        return negative ? -finalResult : finalResult;
    }

    /**
     * Альтернативная версия с обработкой краевых случаев
     */
    public static int multiplyBitwiseSafe(int a, int b) {
        // Базовые случаи
        if (a == 0 || b == 0) return 0;
        if (a == 1) return b;
        if (b == 1) return a;
        if (a == -1) return -b;
        if (b == -1) return -a;

        // Для больших чисел используем обычное умножение
        if (Math.abs(a) > 10000 || Math.abs(b) > 10000) {
            return a * b;
        }

        return multiplyBitwise(a, b);
    }

    // Метод для демонстрации работы
    public static void main(String[] args) {
        // Тестирование различных случаев
        int[][] testCases = {
                {5, 3},        // 15
                {4, -2},       // -8
                {-3, -4},      // 12
                {0, 5},        // 0
                {1, 10},       // 10
                {-1, 10},      // -10
                {10, 1},       // 10
                {7, 8}         // 56
        };

        System.out.println("Тестирование умножения с битовыми операциями:");
        for (int[] testCase : testCases) {
            int a = testCase[0];
            int b = testCase[1];
            int result = multiplyBitwiseSafe(a, b);
            int expected = a * b;
            System.out.printf("%d * %d = %d (ожидалось: %d) %s%n",
                    a, b, result, expected,
                    result == expected ? "✓" : "✗ ОШИБКА!");
        }

        // Дополнительные тесты
        System.out.println("\nДополнительные тесты:");
        System.out.println("256 * 4 = " + multiplyBitwise(256, 4));     // 1024
        System.out.println("128 * 2 = " + multiplyBitwise(128, 2));     // 256
    }
}
