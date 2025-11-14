import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class Main {

    // Простое копирование файла
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        Files.copy(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // Создание тестовых файлов
    public static void createTestFiles() throws IOException {
        // Первый файл
        try (PrintWriter writer = new PrintWriter("file1.txt")) {
            for (int i = 1; i <= 10000; i++) {
                writer.println("Это строка номер " + i + " из первого файла");
            }
        }

        // Второй файл
        try (PrintWriter writer = new PrintWriter("file2.txt")) {
            for (int i = 1; i <= 10000; i++) {
                writer.println("Это строка номер " + i + " из второго файла");
            }
        }

        System.out.println("Созданы тестовые файлы: file1.txt и file2.txt");
    }

    // Последовательное копирование
    public static void sequentialCopy() throws IOException {
        System.out.println("=== ПОСЛЕДОВАТЕЛЬНОЕ КОПИРОВАНИЕ ===");
        long startTime = System.currentTimeMillis();

        copyFile("file1.txt", "copy1_seq.txt");
        System.out.println("Скопирован file1.txt -> copy1_seq.txt");

        copyFile("file2.txt", "copy2_seq.txt");
        System.out.println("Скопирован file2.txt -> copy2_seq.txt");

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс\n");
    }

    // Параллельное копирование
    public static void parallelCopy() throws Exception {
        System.out.println("=== ПАРАЛЛЕЛЬНОЕ КОПИРОВАНИЕ ===");
        long startTime = System.currentTimeMillis();

        // Создаем пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Запускаем копирование в отдельных потоках
        Future<?> task1 = executor.submit(() -> {
            try {
                copyFile("file1.txt", "copy1_par.txt");
                System.out.println("Скопирован file1.txt -> copy1_par.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Future<?> task2 = executor.submit(() -> {
            try {
                copyFile("file2.txt", "copy2_par.txt");
                System.out.println("Скопирован file2.txt -> copy2_par.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Жду завершения обоих задач
        task1.get();
        task2.get();

        // Завершаю работу пула потоков
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс\n");
    }

    // Проверка результатов
    public static void checkResults() throws IOException {
        System.out.println("=== ПРОВЕРКА РЕЗУЛЬТАТОВ ===");

        Path copy1Seq = Paths.get("copy1_seq.txt");
        Path copy2Seq = Paths.get("copy2_seq.txt");
        Path copy1Par = Paths.get("copy1_par.txt");
        Path copy2Par = Paths.get("copy2_par.txt");

        System.out.println("copy1_seq.txt существует: " + Files.exists(copy1Seq));
        System.out.println("copy2_seq.txt существует: " + Files.exists(copy2Seq));
        System.out.println("copy1_par.txt существует: " + Files.exists(copy1Par));
        System.out.println("copy2_par.txt существует: " + Files.exists(copy2Par));

        if (Files.exists(copy1Seq) && Files.exists(copy1Par)) {
            long size1 = Files.size(copy1Seq);
            long size2 = Files.size(copy1Par);
            System.out.println("Размер copy1_seq.txt: " + size1 + " байт");
            System.out.println("Размер copy1_par.txt: " + size2 + " байт");
            System.out.println("Файлы одинакового размера: " + (size1 == size2));
        }
    }

    // Очистка файлов
    public static void cleanup() {
        String[] filesToDelete = {
                "file1.txt", "file2.txt",
                "copy1_seq.txt", "copy2_seq.txt",
                "copy1_par.txt", "copy2_par.txt"
        };

        for (String filename : filesToDelete) {
            try {
                Files.deleteIfExists(Paths.get(filename));
            } catch (IOException e) {
                System.out.println("Не удалось удалить " + filename);
            }
        }
        System.out.println("Очистка завершена");
    }

    public static void main(String[] args) {
        try {
            // Очищаю предыдущие файлы (если есть)
            cleanup();

            // Создаю тестовые файлы
            createTestFiles();

            // Последовательное копирование
            sequentialCopy();

            // Небольшая пауза
            Thread.sleep(1000);

            // Параллельное копирование
            parallelCopy();

            // Проверяю результаты
            checkResults();

            // Очистка (раскомментируйте если нужно)
            // cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}