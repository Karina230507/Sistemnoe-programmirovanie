package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ПРОГРАММА ДЛЯ ПОИСКА И СКАЧИВАНИЯ ФАЙЛОВ С САЙТОВ
 * Сначала парсит страницу, находит ссылки, потом скачивает
 */
public class ParserDownloader {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎵 ПРОГРАММА ДЛЯ СКАЧИВАНИЯ МУЗЫКИ/ФАЙЛОВ 🎵");
        System.out.println("==========================================");
        System.out.println();

        // 1. ЗАПРАШИВАЕМ URL СТРАНИЦЫ
        System.out.print("🌐 Введите URL страницы с файлами: ");
        String pageUrl = scanner.nextLine().trim();

        if (pageUrl.isEmpty()) {
            // URL для тестирования
            pageUrl = "https://example.com/music";
            System.out.println("⚠️ Используем тестовый URL: " + pageUrl);
        }

        try {
            System.out.println();
            System.out.println("🔍 Начинаем поиск файлов на странице...");

            // 2. ПАРСИМ СТРАНИЦУ И ИЩЕМ ССЫЛКИ
            String[] downloadLinks = findDownloadLinks(pageUrl);

            if (downloadLinks.length == 0) {
                System.out.println("❌ Файлы для скачивания не найдены");
                return;
            }

            System.out.println();
            System.out.println("✅ Найдено " + downloadLinks.length + " файлов:");

            // 3. ПОКАЗЫВАЕМ НАЙДЕННЫЕ ФАЙЛЫ
            for (int i = 0; i < downloadLinks.length; i++) {
                System.out.println("   " + (i + 1) + ". " + getFileName(downloadLinks[i]));
            }

            // 4. ВЫБИРАЕМ ФАЙЛ ДЛЯ СКАЧИВАНИЯ
            System.out.println();
            System.out.print("📥 Какой файл скачать? (номер или 0 для всех): ");
            String choice = scanner.nextLine();

            // 5. СКАЧИВАЕМ ВЫБРАННЫЕ ФАЙЛЫ
            if (choice.equals("0")) {
                System.out.println("📦 Скачиваем все файлы...");
                for (int i = 0; i < downloadLinks.length; i++) {
                    downloadSelectedFile(downloadLinks[i], i + 1);
                }
            } else {
                try {
                    int fileNumber = Integer.parseInt(choice) - 1;
                    if (fileNumber >= 0 && fileNumber < downloadLinks.length) {
                        downloadSelectedFile(downloadLinks[fileNumber], fileNumber + 1);
                    } else {
                        System.out.println("❌ Неправильный номер файла");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Введите число");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * ИЩЕТ ССЫЛКИ ДЛЯ СКАЧИВАНИЯ НА СТРАНИЦЕ
     */
    private static String[] findDownloadLinks(String pageUrl) throws IOException {
        System.out.println("📄 Загружаем страницу: " + pageUrl);

        // Подключаемся к странице с имитацией браузера
        Document doc = Jsoup.connect(pageUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(15000)
                .get();

        System.out.println("🔎 Анализируем HTML код...");

        // СПИСОК СЕЛЕКТОРОВ ДЛЯ ПОИСКА ССЫЛОК
        String[] cssSelectors = {
                "a[href*='download']",          // Ссылки с "download" в адресе
                "a[href$='.mp3']",              // Прямые ссылки на mp3
                "a[href$='.mp4']",              // Прямые ссылки на mp4
                "a[href$='.wav']",              // Прямые ссылки на wav
                ".download-link",               // Элементы с классом download-link
                ".jp-play",                     // Как в вашем примере
                "button[data-url]",             // Кнопки с data-url атрибутом
                "[data-file]",                  // Элементы с data-file
                "source[src]",                  // Теги source (часто для медиа)
                "audio source[src]",            // Источники для audio
                "video source[src]"             // Источники для video
        };

        // Собираем все найденные ссылки
        java.util.ArrayList<String> links = new java.util.ArrayList<>();

        for (String selector : cssSelectors) {
            Elements elements = doc.select(selector);

            for (Element element : elements) {
                String url = null;

                // Получаем URL из разных атрибутов
                if (element.hasAttr("href")) {
                    url = element.absUrl("href"); // Абсолютный URL
                } else if (element.hasAttr("src")) {
                    url = element.absUrl("src");
                } else if (element.hasAttr("data-url")) {
                    url = element.absUrl("data-url");
                } else if (element.hasAttr("data-file")) {
                    url = element.attr("data-file");
                    // Делаем URL абсолютным, если он относительный
                    if (!url.startsWith("http")) {
                        url = new URL(new URL(pageUrl), url).toString();
                    }
                }

                // Добавляем URL, если он подходит для скачивания
                if (url != null && !url.isEmpty() && isDownloadableFile(url)) {
                    if (!links.contains(url)) {
                        links.add(url);
                        System.out.println("   ✓ Найдена: " + getFileName(url));
                    }
                }
            }
        }

        return links.toArray(new String[0]);
    }

    /**
     * ПРОВЕРЯЕТ, МОЖНО ЛИ СКАЧАТЬ ФАЙЛ ПО ЭТОЙ ССЫЛКЕ
     */
    private static boolean isDownloadableFile(String url) {
        // Список расширений файлов для скачивания
        String[] extensions = {
                // Аудио
                ".mp3", ".wav", ".ogg", ".flac", ".m4a", ".aac", ".wma",
                // Видео
                ".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm",
                // Архивы
                ".zip", ".rar", ".7z", ".tar", ".gz",
                // Документы
                ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
                // Изображения
                ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg", ".webp",
                // Прочее
                ".exe", ".msi", ".apk", ".dmg", ".iso"
        };

        url = url.toLowerCase();

        // Проверяем расширение файла
        for (String ext : extensions) {
            if (url.contains(ext) && !url.contains("?" + ext)) {
                return true;
            }
        }

        // Проверяем другие признаки скачивания
        return url.contains("download") ||
                url.contains("getfile") ||
                url.contains("file=") ||
                url.contains("download.php") ||
                url.contains("download.aspx");
    }

    /**
     * ПОЛУЧАЕТ ИМЯ ФАЙЛА ИЗ URL
     */
    private static String getFileName(String url) {
        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();

            // Извлекаем имя файла из пути
            if (path.contains("/")) {
                String name = path.substring(path.lastIndexOf("/") + 1);

                // Убираем параметры, если есть
                if (name.contains("?")) {
                    name = name.substring(0, name.indexOf("?"));
                }

                if (!name.isEmpty()) {
                    return name;
                }
            }

            // Если не получилось извлечь имя, возвращаем часть URL
            return url.length() > 50 ? url.substring(0, 50) + "..." : url;

        } catch (Exception e) {
            return url;
        }
    }

    /**
     * СКАЧИВАЕТ ВЫБРАННЫЙ ФАЙЛ
     */
    private static void downloadSelectedFile(String fileUrl, int fileNumber) {
        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println("💾 ФАЙЛ " + fileNumber + ": " + getFileName(fileUrl));
        System.out.println("=".repeat(50));

        try {
            // Создаю имя файла для сохранения
            String fileName = getFileName(fileUrl);
            if (fileName.length() > 100) {
                fileName = "file_" + fileNumber + "_" + System.currentTimeMillis() + ".tmp";
            }

            // Скачиваю   файл
            SimpleDownloader.downloadFile(fileUrl, fileName);

            System.out.println("✅ Файл " + fileNumber + " успешно скачан!");

        } catch (Exception e) {
            System.err.println("❌ Ошибка при скачивании файла " + fileNumber + ": " + e.getMessage());
        }
    }
}
