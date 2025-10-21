
public class ChickenEggDebate {

    public static void main(String[] args) {
        System.out.println("=== СПОР: ЧТО ПОЯВИЛОСЬ РАНЬШЕ? ===\n");

        // Создаем два потока
        Thread chickenThread = new Thread(new ChickenTask(), "Курица");
        Thread eggThread = new Thread(new EggTask(), "Яйцо");

        // Запускаем потоки
        chickenThread.start();
        eggThread.start();

        // Демонстрация метода isAlive()
        System.out.println("\n=== ПРОВЕРКА СОСТОЯНИЯ ПОТОКОВ ===");
        System.out.println("Поток Курица активен: " + chickenThread.isAlive());
        System.out.println("Поток Яйцо активен: " + eggThread.isAlive());

        // Ждем завершения потоков с помощью join()
        try {
            System.out.println("\n=== ОЖИДАНИЕ ЗАВЕРШЕНИЯ ПОТОКОВ ===");

            // Ждем завершения потока Курица
            chickenThread.join();
            System.out.println("Поток Курица завершил работу");
            System.out.println("Поток Курица активен: " + chickenThread.isAlive());

            // Ждем завершения потока Яйцо
            eggThread.join();
            System.out.println("Поток Яйцо завершил работу");
            System.out.println("Поток Яйцо активен: " + eggThread.isAlive());

        } catch (InterruptedException e) {
            System.out.println("Ожидание прервано!");
        }

        // Определяем победителя спора
        determineWinner();
    }

    /**
     * Задача для потока "Курица"
     */
    static class ChickenTask implements Runnable {
        public void run() {
            try {
                // Курица "думает" 1.5 секунды
                Thread.sleep(1500);
                System.out.println("\n🐔 КУРИЦА: Я появилась первой!");

                // Курица говорит еще раз через 0.5 секунды
                Thread.sleep(500);
                System.out.println("🐔 КУРИЦА: Без меня не было бы яиц!");

            } catch (InterruptedException e) {
                System.out.println("Курица прервана!");
            }
        }
    }

    /**
     * Задача для потока "Яйцо"
     */
    static class EggTask implements Runnable {
        public void run() {
            try {
                // Яйцо "думает" 1 секунду
                Thread.sleep(1000);
                System.out.println("\n🥚 ЯЙЦО: Я появилось первым!");

                // Яйцо говорит еще раз через 1 секунду
                Thread.sleep(1000);
                System.out.println("🥚 ЯЙЦО: Без меня не было бы кур!");

            } catch (InterruptedException e) {
                System.out.println("Яйцо прервано!");
            }
        }
    }

    /**
     * Метод для определения победителя спора
     */
    public static void determineWinner() {
        System.out.println("\n=== РЕЗУЛЬТАТ СПОРА ===");
        System.out.println("Последнее слово осталось за...");

        // В реальном приложении здесь была бы логика определения,
        // но для простоты выберем случайного победителя
        if (Math.random() > 0.5) {
            System.out.println("🏆 ПОБЕДИЛА КУРИЦА!");
            System.out.println("Вывод: Сначала появилась курица!");
        } else {
            System.out.println("🏆 ПОБЕДИЛО ЯЙЦО!");
            System.out.println("Вывод: Сначала появилось яйцо!");
        }
    }
}
