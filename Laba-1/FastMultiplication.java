public class FastMultiplication {

    /**
     * Простая и эффективная рекурсивная версия
     * Время: O(log n)
     */
    public static int multiplyFastRecursive(int a, int b) {
        // Базовые случаи
        if (a == 0 || b == 0) return 0;
        if (a == 1) return b;
        if (b == 1) return a;

        // Обрабатываем отрицательные числа
        if (a < 0 && b < 0) {
            return multiplyFastRecursive(-a, -b);
        }
        if (a < 0) {
            return -multiplyFastRecursive(-a, b);
        }
        if (b < 0) {
            return -multiplyFastRecursive(a, -b);
        }

        // Основная логика: используем свойства умножения
        // Если b четное: a * b = 2 * (a * (b/2))
        // Если b нечетное: a * b = a + 2 * (a * (b/2))

        int half = multiplyFastRecursive(a, b >> 1); // b/2
        int result = half << 1; // умножаем на 2

        if ((b & 1) == 1) { // если b нечетное
            result += a;
        }

        return result;
    }

    /**
     * Итеративная версия быстрого умножения
     */
    public static int multiplyFast(int a, int b) {
        if (a == 0 || b == 0) return 0;

        boolean negative = (a < 0) != (b < 0);
        int absA = Math.abs(a);
        int absB = Math.abs(b);

        // Для оптимизации работаем с меньшим числом
        if (absA < absB) {
            return multiplyFast(b, a);
        }

        int result = 0;
        while (absB > 0) {
            if ((absB & 1) == 1) {
                result += absA;
            }
            absA <<= 1;
            absB >>= 1;
        }

        return negative ? -result : result;
    }
}