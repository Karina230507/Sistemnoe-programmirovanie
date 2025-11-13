import java.util.Random;

/**
 * Поток для многократного пополнения счета
 */
public class DepositThread extends Thread {
    private final Account account;
    private final Random random = new Random();

    public DepositThread(Account account) {
        this.account = account;
        this.setName("Deposit-Thread");
    }

    @Override
    public void run() {
        try {
            // Совершаем 10 пополнений с случайными суммами
            for (int i = 1; i <= 10; i++) {
                // Случайная сумма от 50 до 150 долларов
                double amount = 50 + random.nextInt(101);
                account.deposit(amount);

                // Ждем случайное время между пополнениями (0.5-2 секунды)
                Thread.sleep(500 + random.nextInt(1501));
            }

            System.out.println("--- Пополнения завершены ---");

        } catch (InterruptedException e) {
            System.out.println("Поток пополнения прерван");
        }
    }
}