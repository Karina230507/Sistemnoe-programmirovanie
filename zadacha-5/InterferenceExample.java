

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пример совместного использования ресурсов
 * Контролирует остановку потоков по достижению лимита
 */
class InterferenceExample {
    private static final int HUNDRED_MILLION = 100_000_000;
    private AtomicInteger counter = new AtomicInteger();

    /**
     * Проверяет, достигнут ли лимит инкрементов
     * Использует AtomicInteger для атомарного инкремента и проверки
     */
    boolean stop() {
        return counter.incrementAndGet() > HUNDRED_MILLION;
    }

    /**
     * Запускает два потока для демонстрации работы с общим ресурсом
     */
    void example() throws InterruptedException {
        InterferenceThread thread1 = new InterferenceThread(this);
        InterferenceThread thread2 = new InterferenceThread(this);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Expected: " + HUNDRED_MILLION);
        System.out.println("Result: " + thread1.getI());
    }
}