public class MultiplicationWithoutMultiply {

    /**
     * Рекурсивное умножение через сложение
     * Время: O(n), где n = Math.min(a, b)
     */
    public static int multiplyRecursive(int a, int b) {
        // Базовый случай
        if (a == 0 || b == 0) return 0;
        if (a == 1) return b;
        if (b == 1) return a;

        // Для оптимизации выбираем меньший множитель
        if (Math.abs(a) < Math.abs(b)) {
            return multiplyRecursive(b, a);
        }

        // Рекурсивно складываем
        if (b > 0) {
            return a + multiplyRecursive(a, b - 1);
        } else {
            return -a + multiplyRecursive(a, b + 1);
        }
    }

    /**
     * Итеративное умножение через сложение
     * Время: O(n), где n = Math.min(|a|, |b|)
     */
    public static int multiplyIterative(int a, int b) {
        if (a == 0 || b == 0) return 0;

        // Определяем знак результата
        boolean negative = (a < 0 && b > 0) || (a > 0 && b < 0);
        int absA = Math.abs(a);
        int absB = Math.abs(b);

        // Для оптимизации выбираем меньший множитель
        if (absA < absB) {
            return multiplyIterative(b, a);
        }

        // Суммируем absA absB раз
        int result = 0;
        for (int i = 0; i < absB; i++) {
            result += absA;
        }

        return negative ? -result : result;
    }
}


