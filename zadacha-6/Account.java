/**
 * Класс для представления банковского счета
 * Обеспечивает потокобезопасные операции с балансом
 */
public class Account {
    private double balance;  // Баланс счета

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    /**
     * Пополнение баланса
     */
    public synchronized void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() +
                    " пополнил на: $" + amount +
                    " | Баланс: $" + balance);

            // Уведомляем все ожидающие потоки
            notifyAll();  // Теперь это synchronized метод, поэтому можно вызывать notifyAll()
        }
    }

    /**
     * Снятие денег со счета
     */
    public synchronized boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Снято: $" + amount +
                    " | Остаток: $" + balance);
            return true;
        } else {
            System.out.println("Недостаточно средств для снятия $" + amount);
            return false;
        }
    }

    /**
     * Ожидание пополнения баланса до требуемой суммы
     */
    public synchronized void waitForBalance(double targetAmount) throws InterruptedException {
        System.out.println("Ожидаем накопления $" + targetAmount +
                "... Текущий баланс: $" + balance);

        // Ждем пока баланс не достигнет целевой суммы
        while (balance < targetAmount) {
            System.out.println("Ждем... Баланс: $" + balance +
                    ", нужно: $" + targetAmount);
            // wait() можно вызывать потому что метод synchronized
            wait();  // Освобождаем монитор и ждем
        }

        System.out.println("✅ Накопили $" + targetAmount + "! Можно снимать.");
    }

    /**
     * Получение текущего баланса
     */
    public synchronized double getBalance() {
        return balance;
    }
}