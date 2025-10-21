public class MultiplicationWithoutStar {

    //Рекурсивное умножение через сложение
    public static int multiply(int a, int b) {
        // Базовый случай - умножение на 0
        if (b == 0) {
            return 0;
        }

        // Обработка отрицательных чисел
        if (b < 0) {
            return -multiply(a, -b);
        }

        // Рекурсивный случай: a * b = a + a * (b-1)
        return a + multiply(a, b - 1);
    }

    // Дополнительный метод для оптимизации (меньшее количество рекурсивных вызовов)
    public static int multiplyOptimized(int a, int b) {
        // Для оптимизации выбираем меньший множитель для рекурсии
        if (Math.abs(a) < Math.abs(b)) {
            return multiplyOptimized(b, a);
        }

        if (b == 0) {
            return 0;
        }

        if (b < 0) {
            return -multiplyOptimized(a, -b);
        }

        return a + multiplyOptimized(a, b - 1);
    }

    // Пример использования
    public static void main(String[] args) {
        System.out.println("5 * 3 = " + multiply(5, 3)); // 15
        System.out.println("4 * (-2) = " + multiply(4, -2)); // -8
        System.out.println("0 * 5 = " + multiply(0, 5)); // 0

        System.out.println("Оптимизированная версия:");
        System.out.println("5 * 3 = " + multiplyOptimized(5, 3)); // 15
        System.out.println("4 * (-2) = " + multiplyOptimized(4, -2)); // -8
    }
}
