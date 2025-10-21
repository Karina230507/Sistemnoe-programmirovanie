import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс Account представляет банковский аккаунт с потокобезопасными операциями
 */
class Account {
    private double balance; // Баланс счета
    private final Lock lock = new ReentrantLock(); // Мьютекс для синхронизации
    private final Condition sufficientBalance = lock.newCondition(); // Условие для ожидания пополнения

    /**
     * Конструктор с начальным балансом
     */
    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    /**
     * Пополнение баланса (потокобезопасное)
     */
    public void deposit(double amount) {
        lock.lock(); // Захватываем блокировку
        try {
            if (amount > 0) {
                balance += amount;
                System.out.println(Thread.currentThread().getName() + " пополнил на: " + amount +
                        " | Баланс: " + balance);
                // Уведомляем все потоки, ожидающие пополнения баланса
                sufficientBalance.signalAll();
            }
        } finally {
            lock.unlock(); // Всегда освобождаем блокировку
        }
    }

    /**
     * Снятие денег со счета (потокобезопасное)
     */
    public boolean withdraw(double amount) {
        lock.lock();
        try {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " снял: " + amount +
                        " | Баланс: " + balance);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Ожидание пополнения баланса до требуемой суммы для снятия
     */
    public void waitForBalance(double targetAmount) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Ожидание пополнения баланса до: " + targetAmount +
                    " | Текущий баланс: " + balance);

            // Ждем пока баланс не достигнет целевой суммы
            while (balance < targetAmount) {
                System.out.println("Недостаточно средств. Ожидание пополнения...");
                sufficientBalance.await(); // Освобождаем блокировку и ждем уведомления
            }

            System.out.println("Баланс достиг целевой суммы! Можно снимать деньги.");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Получение текущего баланса (потокобезопасное)
     */
    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}

/**
 * Класс для пополнения счета в отдельном потоке
 */
class DepositTask implements Runnable {
    private final Account account;
    private final Random random = new Random();
    private final int numberOfDeposits;

    public DepositTask(Account account, int numberOfDeposits) {
        this.account = account;
        this.numberOfDeposits = numberOfDeposits;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < numberOfDeposits; i++) {
                // Случайная сумма пополнения от 100 до 500
                double depositAmount = 100 + random.nextInt(401);
                account.deposit(depositAmount);

                // Случайная пауза между пополнениями
                Thread.sleep(500 + random.nextInt(501));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Поток пополнения прерван");
        }
    }
}

/**
 * Главный класс приложения
 */
public class BankAccountDemo {
    public static void main(String[] args) {
        System.out.println("=== БАНКОВСКОЕ ПРИЛОЖЕНИЕ ===");

        // Создаем аккаунт с начальным балансом 1000
        Account account = new Account(1000);
        System.out.println("Создан аккаунт с начальным балансом: " + account.getBalance());

        // Целевая сумма для снятия
        double targetWithdrawalAmount = 3000;
        System.out.println("Целевая сумма для снятия: " + targetWithdrawalAmount);

        // Создаем и запускаем поток для пополнения счета
        Thread depositThread = new Thread(new DepositTask(account, 10), "Deposit-Thread");
        depositThread.start();

        try {
            // Ожидаем пока баланс не достигнет целевой суммы
            account.waitForBalance(targetWithdrawalAmount);

            // Пытаемся снять деньги
            boolean success = account.withdraw(targetWithdrawalAmount);
            if (success) {
                System.out.println("Успешно снято: " + targetWithdrawalAmount);
            } else {
                System.out.println("Не удалось снять деньги");
            }

            // Выводим итоговый баланс
            System.out.println("Итоговый баланс: " + account.getBalance());

            // Ждем завершения потока пополнения
            depositThread.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток прерван");
        }

        System.out.println("Программа завершена");
    }
}
