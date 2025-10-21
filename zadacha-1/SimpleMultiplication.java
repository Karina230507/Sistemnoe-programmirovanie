public class SimpleMultiplication {

    /**
     * Простое умножение через сложение
     * Идея: a * b = a + a + a + ... (b раз)
     */
    public static int multiply(int a, int b) {
        // Если любое число ноль - результат ноль
        if (a == 0 || b == 0) {
            return 0;
        }

        int result = 0;

        // Складываем число 'a' само с собой 'b' раз
        for (int i = 0; i < Math.abs(b); i++) {
            result += Math.abs(a);
        }

        // Если знаки разные - результат отрицательный
        if ((a < 0 && b > 0) || (a > 0 && b < 0)) {
            return -result;
        }

        return result;
    }

    // Простой пример использования
    public static void main(String[] args) {
        System.out.println("Простое умножение через сложение");
        System.out.println("================================");

        // Примеры
        System.out.println("5 * 3 = " + multiply(5, 3));      // 15
        System.out.println("4 * (-2) = " + multiply(4, -2));  // -8
        System.out.println("(-3) * (-4) = " + multiply(-3, -4)); // 12
        System.out.println("0 * 5 = " + multiply(0, 5));      // 0
        System.out.println("5 * 0 = " + multiply(5, 0));      // 0

        // Проверка
        System.out.println("\nПроверка:");
        System.out.println("7 * 8 = " + multiply(7, 8) + " (должно быть 56)");
        System.out.println("10 * 10 = " + multiply(10, 10) + " (должно быть 100)");
    }
}
