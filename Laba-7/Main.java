// Собственное исключение для неверного логина
class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}

// Собственное исключение для неверного пароля
class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

// Класс для валидации пользователя
class UserValidator {
    // Правильные логин и пароль
    private static final String CORRECT_LOGIN = "Karina";
    private static final String CORRECT_PASSWORD = "230507";
    public boolean validateUser(String login, String password)
            throws InvalidLoginException, InvalidPasswordException {

        // Проверяю логин
        if (!CORRECT_LOGIN.equals(login)) {
            throw new InvalidLoginException("Неверный логин: '" + login + "'");
        }

        // Проверяю пароль
        if (!CORRECT_PASSWORD.equals(password)) {
            throw new InvalidPasswordException("Неверный пароль для пользователя: " + login);
        }

        // Если все проверки пройдены
        return true;
    }
}

// Главный класс
public class Main {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА ВАЛИДАЦИИ ПОЛЬЗОВАТЕЛЯ ===\n");

        // Создаю валидатор
        UserValidator validator = new UserValidator();

        // Тестовые данные (логин, пароль)
        String[][] testUsers = {
                {"Karina", "230507"},     // правильные данные
                {"user", "230507"},      // неверный логин
                {"Karina", "password"},  // неверный пароль
                {"guest", "qwerty"}     // все неверно
        };

        // Проверяю каждого пользователя
        for (int i = 0; i < testUsers.length; i++) {
            String login = testUsers[i][0];
            String password = testUsers[i][1];

            System.out.println("Попытка " + (i + 1) + ": логин='" + login + "', пароль='" + password + "'");

            try {
                boolean isValid = validator.validateUser(login, password);
                if (isValid) {
                    System.out.println("✅ УСПЕХ: Пользователь '" + login + "' авторизован!\n");
                }
            } catch (InvalidLoginException e) {
                System.out.println("❌ ОШИБКА ЛОГИНА: " + e.getMessage() + "\n");
            } catch (InvalidPasswordException e) {
                System.out.println("❌ ОШИБКА ПАРОЛЯ: " + e.getMessage() + "\n");
            }
        }

        // Дополнительный пример с вводом данных
        System.out.println("=== Еще сделала РУЧНОЙ ВВОД! ===");
        manualValidationExample(validator);
    }

    // Пример с ручным вводом
    public static void manualValidationExample(UserValidator validator) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.println("\nДемонстрация ручного ввода:");
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        try {
            boolean isValid = validator.validateUser(login, password);
            if (isValid) {
                System.out.println("🎉 ДОСТУП РАЗРЕШЕН! Добро пожаловать, " + login + "!");
            }
        } catch (InvalidLoginException e) {
            System.out.println("🚫 ОШИБКА: " + e.getMessage());
        } catch (InvalidPasswordException e) {
            System.out.println("🚫 ОШИБКА: " + e.getMessage());
        }

        scanner.close();
    }
}