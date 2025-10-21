
    public class RussianKrestianin {

    //Русский крестьянский метод умножения
        /**
         * Простое умножение с использованием битовых операций
         * Идея: a * b = a + a + a + ... (b раз)
         * Но используем битовые операции для ускорения
         */
        public static int multiplySimple(int a, int b) {
            // Если один из множителей ноль - результат ноль
            if (a == 0 || b == 0) {
                return 0;
            }

            // Определяем знак результата
            boolean isNegative = (a < 0 && b > 0) || (a > 0 && b < 0);

            // Работаем с положительными числами
            int absA = Math.abs(a);
            int absB = Math.abs(b);

            int result = 0;

            // Пока второй множитель не станет нулем
            while (absB > 0) {
                // Если младший бит установлен, добавляем к результату
                if ((absB & 1) == 1) {
                    result += absA;
                }

                // Умножаем первый множитель на 2 (сдвиг влево)
                absA <<= 1;
                // Делим второй множитель на 2 (сдвиг вправо)
                absB >>= 1;
            }

            // Возвращаем результат с правильным знаком
            return isNegative ? -result : result;
        }

        // Метод для демонстрации работы
        public static void main(String[] args) {
            System.out.println("Умножение без оператора *");
            System.out.println("=========================");

            // Простые примеры
            System.out.println("5 * 3 = " + multiplySimple(5, 3));      // 15
            System.out.println("4 * (-2) = " + multiplySimple(4, -2));  // -8
            System.out.println("(-3) * (-4) = " + multiplySimple(-3, -4)); // 12
            System.out.println("0 * 5 = " + multiplySimple(0, 5));      // 0

            // Еще примеры
            System.out.println("\nДругие примеры:");
            System.out.println("10 * 10 = " + multiplySimple(10, 10));  // 100
            System.out.println("7 * 8 = " + multiplySimple(7, 8));      // 56
            System.out.println("25 * 4 = " + multiplySimple(25, 4));    // 100

            // Проверка с обычным умножением
            System.out.println("\nПроверка корректности:");
            int a = 6, b = 7;
            int ourResult = multiplySimple(a, b);
            int javaResult = a * b;
            System.out.println(a + " * " + b + " = " + ourResult);
            System.out.println("Java результат: " + javaResult);
            System.out.println("Совпадает: " + (ourResult == javaResult));
        }
    }

