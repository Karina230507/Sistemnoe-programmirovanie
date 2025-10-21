public class AlternativeMultiplication {

    /**
     * Умножение через логарифмы
     * Используем формулу: a * b = exp(log(a) + log(b))
     */
    public static int multiplyWithLogarithms(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        // Логарифм от отрицательного числа не существует, поэтому берем модули
        double logResult = Math.log(Math.abs(a)) + Math.log(Math.abs(b));
        double result = Math.exp(logResult);

        int finalResult = (int) Math.round(result);

        // Определяем знак результата
        if ((a < 0 && b > 0) || (a > 0 && b < 0)) {
            return -finalResult;
        }
        return finalResult;
    }

    /**
     * Умножение через деление
     * Используем формулу: a * b = a / (1/b)
     */
    public static int multiplyWithDivision(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        // a * b = a / (1/b)
        double result = a / (1.0 / b);
        return (int) Math.round(result);
    }

    // Простое умножение для сравнения
    public static int simpleMultiply(int a, int b) {
        return a * b;
    }

    // Тестирование методов
    public static void main(String[] args) {
        System.out.println("Сравнение разных методов умножения");
        System.out.println("==================================");

        int[][] testCases = {
                {5, 3},        // 15
                {4, -2},       // -8
                {-3, -4},      // 12
                {10, 10},      // 100
                {7, 8}         // 56
        };

        for (int[] test : testCases) {
            int a = test[0];
            int b = test[1];

            int logResult = multiplyWithLogarithms(a, b);
            int divResult = multiplyWithDivision(a, b);
            int simpleResult = simpleMultiply(a, b);

            System.out.printf("%d * %d = ", a, b);
            System.out.printf("Логарифмы: %d, ", logResult);
            System.out.printf("Деление: %d, ", divResult);
            System.out.printf("Обычное: %d", simpleResult);

            // Проверка корректности
            boolean logCorrect = (logResult == simpleResult);
            boolean divCorrect = (divResult == simpleResult);

            System.out.printf(" [%s] [%s]%n",
                    logCorrect ? "✓" : "✗",
                    divCorrect ? "✓" : "✗");
        }

        // Объяснение методов
        System.out.println("\nКак работают методы:");
        System.out.println("1. Логарифмы: a * b = exp(log(a) + log(b))");
        System.out.println("2. Деление: a * b = a / (1/b)");
        System.out.println("3. Обычное: используется встроенный оператор *");
    }
}
