
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс для инкрементирования общей переменной
 * Решает проблему гонки данных с помощью AtomicInteger
 */
public class InterferenceThread extends Thread {
    private final InterferenceExample checker;

    // ЗАМЕНА: статическая int на AtomicInteger для атомарных операций
    private static AtomicInteger i = new AtomicInteger(0);

    InterferenceThread(InterferenceExample checker) {
        this.checker = checker;
    }

    /**
     * Атомарно инкрементирует общую переменную
     * Решает проблему гонки данных - операция теперь атомарная
     */
    private void increment() {
        i.incrementAndGet(); // Атомарная операция вместо i++
    }

    /**
     * Возвращает текущее значение счетчика
     */
    int getI() {
        return i.get();
    }

    /**
     * Основной метод потока - инкрементирует счетчик пока не достигнут лимит
     */
    public void run() {
        while (!checker.stop()) {
            increment();
        }
    }
}