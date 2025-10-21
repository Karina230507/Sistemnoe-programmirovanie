import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Простая демонстрация многопоточности в Java
 * с подробными комментариями для понимания основных концепций
 */
public class Main {

    // Общая переменная, к которой будут обращаться несколько потоков
    // Это пример разделяемого ресурса - главный источник проблем в многопоточности
    private static int sharedNumber = 0;

    // Мьютекс (взаимное исключение) - гарантирует, что только один поток
    // может выполнить определенный участок кода в данный момент времени
    // ReentrantLock - реализация мьютекса в Java
    private static final Lock lock = new ReentrantLock();

    // Семафор - ограничивает количество потоков, которые могут одновременно
    // получить доступ к ресурсу. Здесь разрешено только 2 потока одновременно
    // Как турникет в метро - пропускает только ограниченное число людей
    private static final Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args) {
        System.out.println("=== ПРОСТАЯ ДЕМОНСТРАЦИЯ МНОГОПОТОЧНОСТИ ===\n");

        // Последовательно демонстрируем основные концепции многопоточности
        // от простых к более сложным
        showProcessVsThread();
        showSynchronizationProblem();
        showSynchronizedSolution();
        showMutexUsage();
        showSemaphoreUsage();
        showDeadlockExample();
    }

    /**
     * 1. ПРОЦЕСС vs ПОТОК - фундаментальное различие
     * Процесс - это отдельная запущенная программа
     * Поток - это часть процесса, которая может выполняться параллельно с другими потоками
     */
    public static void showProcessVsThread() {
        System.out.println("1. ПРОЦЕССЫ И ПОТОКИ");
        System.out.println("   Процесс: отдельная программа со своей памятью");
        System.out.println("   Пример: браузер, текстовый редактор, JVM");
        System.out.println("   Поток: часть процесса, делит память с другими потоками");
        System.out.println("   Пример: вкладка браузера, фоновая загрузка");
        System.out.println("   JVM запускается как процесс, в нем работают потоки\n");

        // Процессы изолированы друг от друга, потоки разделяют память процесса
        // Это важно понимать: потоки одного процесса видят одни и те же переменные
    }

    /**
     * 2. ПРОБЛЕМА БЕЗ СИНХРОНИЗАЦИИ - самая частая проблема в многопоточности
     * Когда несколько потоков одновременно меняют общие данные,
     * они могут "потерять" некоторые изменения или получить некорректные результаты
     * Это называется "состояние гонки" (race condition)
     */
    public static void showSynchronizationProblem() {
        System.out.println("2. ПРОБЛЕМА: потоки портят общие данные (Race Condition)");

        // Сбрасываем счетчик перед тестом
        sharedNumber = 0;

        // Создаем первый поток, который увеличивает sharedNumber 1000 раз
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                // ОПАСНО: операция sharedNumber++ состоит из трех шагов:
                // 1. Прочитать значение из памяти
                // 2. Увеличить его на 1
                // 3. Записать обратно в память
                // Другой поток может вмешаться между этими шагами!
                sharedNumber++;
            }
        });

        // Второй поток делает то же самое
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                sharedNumber++; // Та же проблема
            }
        });

        // Запускаем оба потока
        t1.start();
        t2.start();

        // Ждем завершения обоих потоков
        // join() заставляет главный поток ждать завершения указанного потока
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            // Обработка прерывания - в реальном коде нужно обрабатывать правильно
        }

        // В идеале должно получиться 2000, но из-за race condition
        // результат будет меньше, так как некоторые увеличения "потеряются"
        System.out.println("   Результат без синхронизации: " + sharedNumber);
        System.out.println("   Должно быть: 2000, но получается меньше!");
        System.out.println("   Почему? Потоки мешают друг другу при изменении общей переменной\n");
    }

    /**
     * 3. РЕШЕНИЕ: СИНХРОНИЗАЦИЯ и МОНИТОР
     * Ключевое слово synchronized создает "критическую секцию" -
     * участок кода, который может выполняться только одним потоком одновременно
     * Каждый объект в Java имеет associated с ним "монитор" (lock)
     */
    public static void showSynchronizedSolution() {
        System.out.println("3. РЕШЕНИЕ: synchronized блок (критическая секция)");

        // Сбрасываем счетчик
        sharedNumber = 0;

        // Создаем объект-монитор для синхронизации
        // Можно использовать любой объект, но лучше создавать специальный
        Object monitor = new Object();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                // synchronized блок: только один поток может войти в него
                // для данного объекта-монитора
                synchronized (monitor) {
                    sharedNumber++; // Теперь эта операция защищена
                }
                // После выхода из synchronized блока монитор освобождается
                // и другой поток может войти в свой synchronized блок с тем же монитором
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (monitor) {
                    sharedNumber++; // Защищенная операция
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        // Теперь результат всегда будет правильным - 2000
        // потому что операции увеличения защищены от вмешательства других потоков
        System.out.println("   Результат с synchronized: " + sharedNumber);
        System.out.println("   Теперь правильно: 2000!");
        System.out.println("   Как работает: потоки ждут своей очереди для входа в synchronized блок\n");
    }

    /**
     * 4. МЬЮТЕКС - альтернатива synchronized
     * Lock интерфейс предоставляет более гибкий механизм блокировки
     * Преимущества: можно пытаться захватить блокировку без ожидания,
     * можно прерывать ожидание блокировки и т.д.
     */
    public static void showMutexUsage() {
        System.out.println("4. МЬЮТЕКС: Lock объект (альтернатива synchronized)");

        sharedNumber = 0;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                lock.lock(); // Захватываем блокировку вручную
                try {
                    // Критическая секция - защищена мьютексом
                    sharedNumber++;
                } finally {
                    // ВАЖНО: всегда освобождать блокировку в finally блоке
                    // чтобы гарантировать освобождение даже при исключениях
                    lock.unlock(); // Освобождаем блокировку
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                lock.lock();
                try {
                    sharedNumber++;
                } finally {
                    lock.unlock();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        System.out.println("   Результат с мьютексом: " + sharedNumber);
        System.out.println("   Тоже работает правильно!");
        System.out.println("   Отличие от synchronized: больше контроля над блокировкой\n");
    }

    /**
     * 5. СЕМАФОР - ограничивает количество потоков в критической секции
     * В отличие от мьютекса, который разрешает доступ только одному потоку,
     * семафор может разрешать доступ нескольким потокам одновременно
     * Полезно для ограничения доступа к ресурсам с ограниченной емкостью
     */
    public static void showSemaphoreUsage() {
        System.out.println("5. СЕМАФОР: пропускает только 2 потока одновременно");
        System.out.println("   Как турникет в метро - ограничивает одновременный доступ");

        // Создаем 5 потоков, но семафор разрешит работать только 2 одновременно
        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("   Поток " + threadId + " ждет у семафора...");

                    // acquire() запрашивает разрешение у семафора
                    // Если разрешений нет (все заняты), поток блокируется и ждет
                    semaphore.acquire();

                    // Когда разрешение получено - поток может работать
                    System.out.println("   Поток " + threadId + " работает (захвачен семафор)");

                    // Имитация работы - поток "работает" 2 секунды
                    Thread.sleep(2000);

                    // release() возвращает разрешение обратно в семафор
                    // Теперь другой ожидающий поток может получить это разрешение
                    semaphore.release();
                    System.out.println("   Поток " + threadId + " закончил (освободил семафор)");

                } catch (InterruptedException e) {
                    // Обработка прерывания
                }
            }).start();
        }

        // Ждем достаточно времени чтобы все потоки успели поработать
        try { Thread.sleep(7000); } catch (InterruptedException e) {}
        System.out.println();
    }

    /**
     * 6. ТУПИК (Deadlock) - опасная ситуация взаимной блокировки
     * Происходит когда два или более потока ждут ресурсы, занятые друг другом
     * Программа "зависает" потому что ни один поток не может продолжить работу
     */
    public static void showDeadlockExample() {
        System.out.println("6. ТУПИК (Deadlock): потоки вечно ждут друг друга");
        System.out.println("   Как два вежливых человека в дверях: 'Ты первый', 'Нет, ты первый'");

        // Два ресурса, за которые будут бороться потоки
        final Object resource1 = new Object();
        final Object resource2 = new Object();

        // Первый поток захватывает resource1, затем пытается захватить resource2
        Thread t1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("   Поток 1: взял resource1");
                try {
                    // Небольшая задержка чтобы второй поток успел захватить resource2
                    Thread.sleep(100);
                } catch (InterruptedException e) {}

                // Теперь пытаемся взять второй ресурс
                // НО: если второй поток уже взял resource2, мы будем ждать вечно!
                synchronized (resource2) {
                    System.out.println("   Поток 1: взял resource2");
                }
            }
        });

        // Второй поток делает наоборот: захватывает resource2, затем resource1
        Thread t2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("   Поток 2: взял resource2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}

                // Пытаемся взять первый ресурс
                // НО: если первый поток уже взял resource1, мы будем ждать вечно!
                synchronized (resource1) {
                    System.out.println("   Поток 2: взял resource1");
                }
            }
        });

        t1.start();
        t2.start();

        // Даем время проявиться проблеме
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("   Если не видите сообщений 'взял resource2' и 'взял resource1' - это тупик!");
        System.out.println("   Решение: всегда захватывать ресурсы в одинаковом порядке\n");
    }
}
