/**
 * Класс для потока-зверушки, расширяет Thread
 */
class AnimalThread extends Thread {
    private String animalName;  // имя зверушки
    private int distance = 0;   // пройденная дистанция в метрах

    // Конструктор с именем и приоритетом
    public AnimalThread(String name, int priority) {
        this.animalName = name;
        this.setPriority(priority); // устанавливаем приоритет потока
    }

    // Метод run() - что делает поток при запуске
    public void run() {
        System.out.println(animalName + " начинает гонку! Приоритет: " + this.getPriority());

        // Бежим 100 метров
        while (distance < 100) {
            distance++; // пробегаем 1 метр
            System.out.println(animalName + " пробежал " + distance + " метров");

            try {
                // Задержка зависит от приоритета - чем выше приоритет, тем быстрее бежим
                Thread.sleep(100 - (this.getPriority() * 5));
            } catch (InterruptedException e) {
                System.out.println(animalName + " прерван!");
            }
        }

        System.out.println("=== " + animalName + " ФИНИШИРОВАЛ! ===");
    }

    // Метод для получения текущей дистанции
    public int getDistance() {
        return distance;
    }

    // Метод для получения имени зверушки
    public String getAnimalName() {
        return animalName;
    }
}

/**
 * Главный класс для запуска гонки
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== ГОНКА ЗАЙЦА И ЧЕРЕПАХИ ===");

        // Создаем потоки-зверушки
        AnimalThread rabbit = new AnimalThread("Зайчик", Thread.MIN_PRIORITY); // низкий приоритет
        AnimalThread turtle = new AnimalThread("Черепашка", Thread.MAX_PRIORITY); // высокий приоритет

        // Запускаем потоки
        rabbit.start();
        turtle.start();

        // Ждем завершения гонки
        try {
            rabbit.join();
            turtle.join();
        } catch (InterruptedException e) {
            System.out.println("Гонка прервана!");
        }

        System.out.println("\n=== РЕЗУЛЬТАТЫ ГОНКИ ===");
        System.out.println("Зайчик пробежал: " + rabbit.getDistance() + " метров");
        System.out.println("Черепашка пробежала: " + turtle.getDistance() + " метров");

        // Определяем победителя
        if (rabbit.getDistance() > turtle.getDistance()) {
            System.out.println("ПОБЕДИЛ ЗАЙЧИК!");
        } else if (turtle.getDistance() > rabbit.getDistance()) {
            System.out.println("ПОБЕДИЛА ЧЕРЕПАШКА!");
        } else {
            System.out.println("НИЧЬЯ!");
        }
    }
}
