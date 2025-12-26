package obfuscator;

import java.util.*;
import java.util.regex.*;

/**
 * Класс для обработки кода: удаление комментариев и лишних пробелов
 */
public class CodeProcessor {

    /**
     * Удаление всех комментариев из Java-кода
     */
    public String removeComments(String code) {
        // Регулярное выражение для удаления комментариев
        // 1. Однострочные комментарии: // комментарий
        // 2. Многострочные комментарии: /* комментарий */
        // 3. Javadoc комментарии: /** комментарий */

        // ВАЖНО: Обрабатываем код построчно, чтобы сохранить структуру
        String[] lines = code.split("\n");
        StringBuilder result = new StringBuilder();
        boolean inBlockComment = false;

        for (String line : lines) {
            String processedLine = line;

            // Обработка блочных комментариев
            if (inBlockComment) {
                int endBlockIndex = processedLine.indexOf("*/");
                if (endBlockIndex != -1) {
                    // Конец блочного комментария
                    processedLine = processedLine.substring(endBlockIndex + 2);
                    inBlockComment = false;
                } else {
                    // Вся строка внутри блочного комментария
                    processedLine = "";
                }
            }

            // Если не в блочном комментарии
            if (!inBlockComment) {
                // Удаляем блочные комментарии в строке
                processedLine = processedLine.replaceAll("/\\*.*?\\*/", "");

                // Проверяем, начинается ли блочный комментарий
                int startBlockIndex = processedLine.indexOf("/*");
                if (startBlockIndex != -1) {
                    inBlockComment = true;
                    processedLine = processedLine.substring(0, startBlockIndex);
                }

                // Удаляем однострочные комментарии
                int lineCommentIndex = processedLine.indexOf("//");
                if (lineCommentIndex != -1) {
                    processedLine = processedLine.substring(0, lineCommentIndex);
                }
            }

            // Добавляем строку в результат
            result.append(processedLine).append("\n");
        }

        return result.toString();
    }

    /**
     * Удаление лишних пробелов и символов перевода строки
     */
    public String removeExtraSpaces(String code) {
        // Сохраняем строковые литералы, чтобы не трогать пробелы внутри них
        List<String> stringLiterals = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        Pattern stringPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
        Matcher matcher = stringPattern.matcher(code);

        // Временная замена строковых литералов
        String tempCode = code;
        int counter = 0;
        int startPos = 0;

        // Используем StringBuffer для безопасной замены
        StringBuffer tempBuffer = new StringBuffer();

        while (matcher.find()) {
            String literal = matcher.group();
            stringLiterals.add(literal);
            matcher.appendReplacement(tempBuffer, "__STRING_" + counter + "__");
            counter++;
        }
        matcher.appendTail(tempBuffer);
        tempCode = tempBuffer.toString();

        // Удаляем лишние пробелы (но сохраняем пробелы между словами)
        tempCode = tempCode
                // Удаляем пробелы в начале строки
                .replaceAll("(?m)^[ \t]+", "")
                // Удаляем пробелы в конце строки
                .replaceAll("(?m)[ \t]+$", "")
                // Заменяем множественные пробелы на один
                .replaceAll("[ \t]{2,}", " ")
                // Удаляем пробелы вокруг операторов, но не в операторах (например, "==")
                .replaceAll("\\s*([=!<>+\\-*/%&|^]=?)\\s*", "$1")
                // Удаляем пробелы после открывающих и перед закрывающими скобками
                .replaceAll("\\(\\s+", "(")
                .replaceAll("\\s+\\)", ")")
                .replaceAll("\\{\\s+", "{")
                .replaceAll("\\s+\\}", "}")
                // Удаляем пробелы вокруг точек, запятых, точек с запятой
                .replaceAll("\\s*([.,;])\\s*", "$1")
                // Удаляем лишние переводы строк (оставляем после точек с запятой, фигурных скобок)
                .replaceAll(";[ \t]*\r?\n", ";")
                .replaceAll("\\}[ \t]*\r?\n", "}")
                .replaceAll("\\{[ \t]*\r?\n", "{")
                // Удаляем пустые строки
                .replaceAll("(?m)^[ \t]*\r?\n", "");

        // Восстанавливаем строковые литералы
        String finalCode = tempCode;
        for (int i = 0; i < stringLiterals.size(); i++) {
            String tempMarker = "__STRING_" + i + "__";
            finalCode = finalCode.replace(tempMarker, stringLiterals.get(i));
        }

        return finalCode;
    }

    /**
     * Минимизация кода (удаление всех ненужных пробелов)
     */
    public String minimizeCode(String code) {
        String withoutComments = removeComments(code);
        String minimized = removeExtraSpaces(withoutComments);

        // Дополнительная минимизация
        minimized = minimized
                .replaceAll("\\s*;\\s*", ";")
                .replaceAll("\\)\\s*\\{", "){")
                .replaceAll("\\s+", " ")
                // Удаляем пробелы вокруг операторов присваивания
                .replaceAll("\\s*=\\s*", "=")
                // Удаляем пробелы вокруг точек
                .replaceAll("\\s*\\.\\s*", ".")
                // Удаляем пробелы после запятых
                .replaceAll(",\\s*", ",")
                .trim();

        // Удаляем лишние переводы строк
        minimized = minimized.replaceAll("\n+", "\n");

        return minimized;
    }

    /**
     * Комплексная обработка: удаление комментариев + минимизация
     */
    public String process(String code) {
        return minimizeCode(code);
    }
}