/**
 * Главный класс для демонстрации работы банковского счета
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== БАНКОВСКИЙ СЧЕТ С МНОГОПОТОЧНОСТЬЮ ===\n");

        // Создаем счет с начальным балансом $100
        Account account = new Account(100);
        System.out.println("Начальный баланс: $" + account.getBalance());

        // Целевая сумма для снятия
        double targetAmount = 500;
        System.out.println("Целевая сумма для снятия: $" + targetAmount + "\n");

        // Запускаем поток для пополнения счета
        DepositThread depositThread = new DepositThread(account);
        depositThread.start();

        try {
            // Ждем пока накопится нужная сумма
            account.waitForBalance(targetAmount);

            // Снимаем деньги
            boolean success = account.withdraw(targetAmount);
            if (success) {
                System.out.println("🎉 Успешно сняли $" + targetAmount);
            }

            // Ждем завершения потока пополнения
            depositThread.join();

            // Выводим финальный баланс
            System.out.println("\n💵 ФИНАЛЬНЫЙ БАЛАНС: $" + account.getBalance());

        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}