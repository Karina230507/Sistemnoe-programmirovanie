package obfuscator;

import java.util.*;
import java.util.regex.*;

/**
 * Класс для переименования идентификаторов (переменных, методов)
 */
public class IdentifierRenamer {
    private static final String[] SINGLE_CHARS = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    private static final String[] DOUBLE_CHARS = generateDoubleChars();

    private static String[] generateDoubleChars() {
        List<String> result = new ArrayList<>();
        for (char c1 = 'a'; c1 <= 'z'; c1++) {
            for (char c2 = 'a'; c2 <= 'z'; c2++) {
                result.add("" + c1 + c2);
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * Переименование всех идентификаторов в коде
     */
    public String renameIdentifiers(String code) {
        // Собираем все идентификаторы
        Set<String> identifiers = collectIdentifiers(code);

        // Исключаем ключевые слова Java
        Set<String> keywords = getJavaKeywords();
        identifiers.removeAll(keywords);

        // Сортируем по длине (сначала длинные, чтобы не заменять части других идентификаторов)
        List<String> sortedIdentifiers = new ArrayList<>(identifiers);
        sortedIdentifiers.sort((a, b) -> Integer.compare(b.length(), a.length()));

        // Генерируем новые имена
        Map<String, String> renameMap = generateNewNames(sortedIdentifiers);

        // Заменяем идентификаторы в коде
        String result = code;
        for (Map.Entry<String, String> entry : renameMap.entrySet()) {
            // Используем границы слова, чтобы не заменять части других идентификаторов
            String regex = "\\b" + Pattern.quote(entry.getKey()) + "\\b";
            result = result.replaceAll(regex, entry.getValue());
        }

        return result;
    }

    /**
     * Сбор всех идентификаторов из кода
     */
    private Set<String> collectIdentifiers(String code) {
        Set<String> identifiers = new HashSet<>();

        // Регулярное выражение для поиска идентификаторов
        // Идентификатор в Java: начинается с буквы, $ или _, далее буквы, цифры, $ или _
        Pattern pattern = Pattern.compile("\\b[a-zA-Z_$][a-zA-Z0-9_$]*\\b");
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String identifier = matcher.group();

            // Проверяем, что это не число и не ключевое слово
            if (!identifier.matches("\\d+") && !isKeyword(identifier)) {
                identifiers.add(identifier);
            }
        }

        return identifiers;
    }

    /**
     * Генерация новых имен для идентификаторов
     */
    private Map<String, String> generateNewNames(List<String> identifiers) {
        Map<String, String> renameMap = new HashMap<>();
        int singleCharIndex = 0;
        int doubleCharIndex = 0;

        for (String identifier : identifiers) {
            String newName;

            if (singleCharIndex < SINGLE_CHARS.length) {
                newName = SINGLE_CHARS[singleCharIndex++];
            } else if (doubleCharIndex < DOUBLE_CHARS.length) {
                newName = DOUBLE_CHARS[doubleCharIndex++];
            } else {
                // Если закончились имена, генерируем случайные
                newName = generateRandomName(renameMap.size());
            }

            renameMap.put(identifier, newName);
        }

        return renameMap;
    }

    /**
     * Генерация случайного имени
     */
    private String generateRandomName(int index) {
        return "v" + index;
    }

    /**
     * Получение множества ключевых слов Java
     */
    private Set<String> getJavaKeywords() {
        return new HashSet<>(Arrays.asList(
                "abstract", "assert", "boolean", "break", "byte", "case", "catch",
                "char", "class", "const", "continue", "default", "do", "double",
                "else", "enum", "extends", "final", "finally", "float", "for",
                "goto", "if", "implements", "import", "instanceof", "int", "interface",
                "long", "native", "new", "package", "private", "protected", "public",
                "return", "short", "static", "strictfp", "super", "switch",
                "synchronized", "this", "throw", "throws", "transient", "try",
                "void", "volatile", "while", "true", "false", "null"
        ));
    }

    private boolean isKeyword(String word) {
        return getJavaKeywords().contains(word);
    }
}