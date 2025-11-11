public class Main {

    /**
     * Вычисляет максимальное количество шоколадок, которые можно получить
     * @param money - имеющиеся деньги
     * @param price - цена за одну шоколадку
     * @param wrap - количество обёрток для получения дополнительной шоколадки
     * @return максимальное количество шоколадок
     */
    public static int calculateMaxChocolates(int money, int price, int wrap) {
        // Шаг 1: Покупаем столько шоколадок, сколько можем за деньги
        int chocolates = money / price;
        int wrappers = chocolates; // Изначально обёрток столько же, сколько шоколадок

        System.out.println("Изначально куплено шоколадок: " + chocolates);
        System.out.println("Изначально обёрток: " + wrappers);

        // Шаг 2: Продолжаем обменивать обёртки на новые шоколадки
        while (wrappers >= wrap) {
            // Обмениваем обёртки на новые шоколадки
            int newChocolates = wrappers / wrap;
            chocolates += newChocolates;

            // Вычисляем оставшиеся обёртки:
            // - те, что не хватило для обмена (остаток от деления)
            // - плюс обёртки от новых шоколадок
            wrappers = wrappers % wrap + newChocolates;

            System.out.println("Обменяли обёртки на " + newChocolates + " шоколадок");
            System.out.println("Всего шоколадок: " + chocolates);
            System.out.println("Осталось обёрток: " + wrappers);
            System.out.println("---");
        }

        return chocolates;
    }

    /**
     * Альтернативное решение с рекурсией
     */
    public static int calculateMaxChocolatesRecursive(int money, int price, int wrap) {
        // Покупаем шоколадки за деньги
        int chocolates = money / price;
        return chocolates + exchangeWrappers(chocolates, wrap);
    }

    private static int exchangeWrappers(int wrappers, int wrap) {
        // Базовый случай: нельзя больше обменять
        if (wrappers < wrap) {
            return 0;
        }

        // Обмениваем обёртки на шоколадки
        int newChocolates = wrappers / wrap;
        int remainingWrappers = wrappers % wrap + newChocolates;

        // Рекурсивно обмениваем оставшиеся обёртки
        return newChocolates + exchangeWrappers(remainingWrappers, wrap);
    }

    public static void main(String[] args) {
        // Тестовые случаи
        int money = 15;
        int price = 1;
        int wrap = 3;

        System.out.println("РАСЧЕТ МАКСИМАЛЬНОГО КОЛИЧЕСТВА ШОКОЛАДОК ");
        System.out.println("Деньги: $" + money);
        System.out.println("Цена за шоколадку: $" + price);
        System.out.println("Обёрток для обмена: " + wrap);
        System.out.println();

        int result = calculateMaxChocolates(money, price, wrap);
        System.out.println("\n МАКСИМАЛЬНОЕ КОЛИЧЕСТВО ШОКОЛАДОК: " + result);

        // Проверка рекурсивным методом
        int recursiveResult = calculateMaxChocolatesRecursive(money, price, wrap);
        System.out.println("Проверка рекурсивным методом: " + recursiveResult);

        // Дополнительные тесты
        testAdditionalCases();
    }

    public static void testAdditionalCases() {
        System.out.println("\n ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ");

        // Тест 1: Исходная задача
        testCase(15, 1, 3, 22); // 15 + 5 + 1 + 1 = 22

        // Тест 2: Другие значения
        testCase(10, 2, 5, 6);  // 5 + 1 = 6
        testCase(20, 3, 5, 9);  // 6 + 1 + 1 = 8? Проверим!
        testCase(100, 5, 10, 24); // 20 + 2 = 22
    }

    public static void testCase(int money, int price, int wrap, int expected) {
        int actual = calculateMaxChocolatesRecursive(money, price, wrap);
        System.out.printf("Деньги: $%d, Цена: $%d, Обёртки: %d -> ", money, price, wrap);
        System.out.printf("Ожидалось: %d, Получилось: %d %s%n",
                expected, actual, expected == actual ? "✅" : "❌");
    }
}