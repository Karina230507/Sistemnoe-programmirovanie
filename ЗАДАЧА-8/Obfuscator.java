package obfuscator;

import java.util.*;
import java.util.regex.*;

/**
 * Основной класс обфускации, координирующий все этапы обработки
 */
public class Obfuscator {
    private CodeProcessor codeProcessor;
    private IdentifierRenamer identifierRenamer;
    private ClassRenamer classRenamer;

    public Obfuscator() {
        this.codeProcessor = new CodeProcessor();
        this.identifierRenamer = new IdentifierRenamer();
        this.classRenamer = new ClassRenamer();
    }

    /**
     * Основной метод обфускации
     */
    public String obfuscate(String originalCode, String fileName) {
        System.out.println("   🔧 Этап 1/4: Удаление комментариев...");
        String step1 = codeProcessor.removeComments(originalCode);

        System.out.println("   🔧 Этап 2/4: Удаление лишних пробелов...");
        String step2 = codeProcessor.removeExtraSpaces(step1);

        System.out.println("   🔧 Этап 3/4: Переименование идентификаторов...");
        String step3 = identifierRenamer.renameIdentifiers(step2);

        System.out.println("   🔧 Этап 4/4: Переименование класса...");
        String step4 = classRenamer.renameClass(step3, fileName);

        return step4;
    }

    /**
     * Получить статистику по обфускации
     */
    public Map<String, Integer> getStatistics(String originalCode, String obfuscatedCode) {
        Map<String, Integer> stats = new HashMap<>();

        stats.put("original_length", originalCode.length());
        stats.put("obfuscated_length", obfuscatedCode.length());
        stats.put("spaces_removed", countOccurrences(originalCode, " ") - countOccurrences(obfuscatedCode, " "));
        stats.put("newlines_removed", countOccurrences(originalCode, "\n") - countOccurrences(obfuscatedCode, "\n"));

        return stats;
    }

    private int countOccurrences(String text, String pattern) {
        return text.length() - text.replace(pattern, "").length();
    }
}