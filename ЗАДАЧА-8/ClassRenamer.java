package obfuscator;

import java.util.*;
import java.util.regex.*;

/**
 * Класс для переименования классов, конструкторов и файлов
 */
public class ClassRenamer {
    private int classCounter = 0;
    private List<String> usedClassNames = new ArrayList<>();

    /**
     * Переименование класса и всех связанных с ним элементов
     */
    public String renameClass(String code, String fileName) {
        // Извлекаем имя класса из кода
        String className = extractClassName(code);
        if (className == null) {
            className = "UnknownClass";
        }

        // Генерируем новое имя для класса
        String newClassName = generateNewClassName();

        // Заменяем имя класса везде
        String result = code;

        // 1. Замена объявления класса
        result = result.replaceAll("class\\s+" + Pattern.quote(className) + "\\b", "class " + newClassName);

        // 2. Замена конструкторов
        result = result.replaceAll(Pattern.quote(className) + "\\s*\\(", newClassName + "(");

        // 3. Замена использования класса в качестве типа
        result = result.replaceAll("\\b" + Pattern.quote(className) + "\\b", newClassName);

        return result;
    }

    /**
     * Извлечение имени класса из кода
     */
    private String extractClassName(String code) {
        // Ищем объявление класса
        Pattern pattern = Pattern.compile("class\\s+([a-zA-Z_$][a-zA-Z0-9_$]*)");
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // Ищем public class
        pattern = Pattern.compile("public\\s+class\\s+([a-zA-Z_$][a-zA-Z0-9_$]*)");
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * Генерация нового имени для класса
     */
    private String generateNewClassName() {
        // Генерируем имена вида A, B, C, ..., Z, AA, AB, ...

        String className;
        int counter = classCounter++;

        if (counter < 26) {
            // A-Z
            className = String.valueOf((char) ('A' + counter));
        } else {
            // AA, AB, ...
            int firstChar = counter / 26 - 1;
            int secondChar = counter % 26;
            className = String.valueOf((char) ('A' + firstChar)) + (char) ('A' + secondChar);
        }

        // Проверяем, что имя не используется
        while (usedClassNames.contains(className)) {
            className = generateRandomClassName();
        }

        usedClassNames.add(className);
        return className;
    }

    /**
     * Генерация случайного имени класса
     */
    private String generateRandomClassName() {
        Random random = new Random();
        int length = random.nextInt(3) + 2; // 2-4 символа
        StringBuilder name = new StringBuilder();

        // Первый символ - заглавная буква
        name.append((char) ('A' + random.nextInt(26)));

        // Остальные символы - маленькие буквы
        for (int i = 1; i < length; i++) {
            name.append((char) ('a' + random.nextInt(26)));
        }

        return name.toString();
    }

    /**
     * Создание нового имени файла на основе нового имени класса
     */
    public String generateNewFileName(String oldFileName, String newClassName) {
        return newClassName + ".java";
    }
}