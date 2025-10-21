/**
 * Простой пример запуска 10 потоков в Java
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Запускаем 10 потоков...");

        // Создаем и запускаем 10 потоков
        for (int i = 1; i <= 10; i++) {
            // Номер потока (должен быть final для использования в анонимном классе)
            final int threadNumber = i;

            // Создаем новый поток
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    // Этот код выполняется в отдельном потоке
                    System.out.println("Поток " + threadNumber + " начал работу");

                    // Имитируем работу потока - ждем 1 секунду
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Поток " + threadNumber + " был прерван");
                    }

                    System.out.println("Поток " + threadNumber + " закончил работу");
                }
            });

            // Запускаем поток
            thread.start();
        }

        System.out.println("Все потоки запущены (главный поток продолжает работу)");
    }
}
