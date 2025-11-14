import java.nio.file.*;
import java.util.concurrent.*;

public class Main {

    // Синхронное копирование с NIO
    public static void syncCopy(String source, String target) throws Exception {
        Path sourcePath = Paths.get(source);
        Path targetPath = Paths.get(target);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Синхронно скопирован: " + source + " -> " + target);
    }

    // Асинхронное копирование с NIO и CompletableFuture
    public static CompletableFuture<Void> asyncCopy(String source, String target) {
        return CompletableFuture.runAsync(() -> {
            try {
                Path sourcePath = Paths.get(source);
                Path targetPath = Paths.get(target);
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Асинхронно скопирован: " + source + " -> " + target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        // Создаем тестовые файлы
        Files.writeString(Paths.get("test1.txt"), "Содержимое первого файла\n".repeat(1000));
        Files.writeString(Paths.get("test2.txt"), "Содержимое второго файла\n".repeat(1000));
        System.out.println("Тестовые файлы созданы\n");

        // СИНХРОННОЕ КОПИРОВАНИЕ
        System.out.println("=== СИНХРОННОЕ КОПИРОВАНИЕ ===");
        long syncStart = System.currentTimeMillis();

        syncCopy("test1.txt", "sync_copy1.txt");
        syncCopy("test2.txt", "sync_copy2.txt");

        long syncTime = System.currentTimeMillis() - syncStart;
        System.out.println("Время синхронного копирования: " + syncTime + " мс\n");

        // Пауза между тестами
        Thread.sleep(500);

        // АСИНХРОННОЕ КОПИРОВАНИЕ
        System.out.println("=== АСИНХРОННОЕ КОПИРОВАНИЕ ===");
        long asyncStart = System.currentTimeMillis();

        CompletableFuture<Void> copy1 = asyncCopy("test1.txt", "async_copy1.txt");
        CompletableFuture<Void> copy2 = asyncCopy("test2.txt", "async_copy2.txt");

        // Ждем завершения всех асинхронных задач
        CompletableFuture.allOf(copy1, copy2).get();

        long asyncTime = System.currentTimeMillis() - asyncStart;
        System.out.println("Время асинхронного копирования: " + asyncTime + " мс\n");

        // РЕЗУЛЬТАТЫ
        System.out.println("=== РЕЗУЛЬТАТЫ ===");
        System.out.println("Синхронное время: " + syncTime + " мс");
        System.out.println("Асинхронное время: " + asyncTime + " мс");
        System.out.println("Разница: " + (syncTime - asyncTime) + " мс");

        // Проверяем что файлы созданы
        System.out.println("\n=== ПРОВЕРКА ФАЙЛОВ ===");
        checkFile("sync_copy1.txt");
        checkFile("sync_copy2.txt");
        checkFile("async_copy1.txt");
        checkFile("async_copy2.txt");
    }

    public static void checkFile(String filename) {
        try {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                long size = Files.size(path);
                System.out.println(filename + ": существует, размер: " + size + " байт");
            } else {
                System.out.println(filename + ": не существует");
            }
        } catch (Exception e) {
            System.out.println(filename + ": ошибка проверки");
        }
    }
}