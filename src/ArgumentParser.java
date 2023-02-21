import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private String mode;                                    //Режим сортировки
    private Path outPath;                                   //Путь исходящего файла
    private final List<Path> inFileNames = new ArrayList<>();   //Список имен входящих файлов
    private long limitBlockSize;    // Максимальный размер блоков, считываемых за раз из внешних файла.


    public void parse(String[] args) {
        /*Проверка на минимальное допустимое количество аргументов*/
        if (args.length < 3) {
            throw new IllegalArgumentException("Не были введены все обязательные параметры для выполнения программы." +
                    " Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.");
        }

        /*Определение индекса раздела команд и файлов в командной строке*/
        int indexPoint;
        if (args[1].contains("-")) {
            indexPoint = 2;
        } else {
            indexPoint = 1;
        }
        /*Проверка выходного файла на соответствие расширению .txt*/
        if (!args[indexPoint].contains(".txt")) {
            throw new RuntimeException("Параметр " + args[indexPoint] + " должен иметь расширение \".txt\"" +
                    " Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.");
        }

        /*Определение режима сортировки*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indexPoint; i++) {
            stringBuilder.append(args[i]);
        }

        String joinedString = stringBuilder.toString();
        if (joinedString.equals("-s") || joinedString.equals("-a-s") || joinedString.equals("-s-a")) {
            mode = "stringAsc";
        } else if (joinedString.equals("-i") || joinedString.equals("-a-i") || joinedString.equals("-i-a")) {
            mode = "intAsc";
        } else if (joinedString.equals("-d-s") || joinedString.equals("-s-d")) {
            mode = "stringDesc";
        } else if (joinedString.equals("-d-i") || joinedString.equals("-i-d")) {
            mode = "intDesc";
        } else {
            throw new IllegalArgumentException("Были введены недопустимые параметры или их последовательность." +
                    " Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.");
        }

        /*Путь сохранения исходящего файла*/
        outPath = Path.of("files", args[indexPoint]);

        /*Установка максимального размера блока данных в Mb*/
        int limitFileNamesArgs = args.length;
        try {
            limitBlockSize = Long.parseLong(args[args.length - 1]) * 1024 * 1024;
            limitFileNamesArgs = args.length - 1;
        } catch (NumberFormatException e) {
            limitBlockSize = 31457280L; //Если значение не задано, размер блока равен 30 Mb
        }

        /*Заполнение списка именами входящих файлов*/
        for (int i = indexPoint + 1; i < limitFileNamesArgs; i++) {
            if (!args[i].contains(".txt")) {    //Проверка входных файлов на соответствие расширению .txt
                throw new RuntimeException("Параметр " + args[i] + " должен иметь расширение \".txt\"" +
                        " Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.");
            }
            inFileNames.add(Path.of("files", args[i]));
        }
    }

    public String getMode() {
        return mode;
    }

    public Path getOutPath() {
        return outPath;
    }

    public List<Path> getInFileNames() {
        return inFileNames;
    }

    public long getLimitBlockSize() {
        return limitBlockSize;
    }
}
