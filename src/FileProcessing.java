import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileProcessing {
    private final Path pathTemp = Path.of("temp");   //Путь к папке с временными файлами
    public void mergeSort(String mode, Path outPath, List<Path> inFileNames, long limitBlockSize) {

        /* Создание директории для вреенных файлов*/
        try {
            Files.createDirectories(pathTemp);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка соединения при создания директории для временных файлов!", e);
        }

        /*Создание списка адресов временных файлов*/
        List<Path> tempFiles = new ArrayList<>();

        /*Создание и добавление временного файла, в который будет выполняться первое слияние считанных отсортированных блоков*/
        Path temp = null;
        try {
            temp = Files.createTempFile(pathTemp, null, ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFiles.add(temp);

        /*код для файла со строками*/
        if (mode.contains("string")) {
            for (Path inFiles : inFileNames) {
                try (BufferedReader bufferedReader = Files.newBufferedReader(inFiles)) {
                    List<String> UncheckedSortingInFiles = new ArrayList<>();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        List<String> SortingInFiles = null;
                        long blockSize = 52 + line.length() * 2L;   // максимальные значения(приближенные): 52 байта служебной информации(для 64 разрядной, для 32 разр. будет меньше) + 2 байта на каждый символ;
                        while ((limitBlockSize > blockSize) && (line != null)) {
                            if (!line.contains(" ") && !line.isEmpty()) {    //фильтр на наличие пробела в строке и пустой строки
                                UncheckedSortingInFiles.add(line);
                                blockSize += 52 + line.length() * 2L;
                            } else {
                                System.out.println("Файл " + inFiles + " с данными содержит пробельный символ или пустой элемент. " +
                                        "Элемент считается ошибочным, поэтому не будет считан из файла");
                            }
                            line = bufferedReader.readLine();
                        }

                        /*Проверка сортировки данных в файле. Если в сортировке допущена ошибка, то для данного блока выполняется сортировка слиянием*/
                        SortingInFiles = Utils.checkSortFile(UncheckedSortingInFiles, mode);

                        /*Разворачиваем элементы списка, если необходима сортировка по убыванию*/
                        if (mode.contains("Desc")) {
                            Collections.reverse(SortingInFiles);
                        }

                        /*Создаем временные файлы для внешней сортировки слиянием*/
                        temp = Files.createTempFile(pathTemp, null, ".txt");
                        tempFiles.add(temp);

                        /*Пишем данные из блоков во временные файлы*/
                        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(temp)) {
                            for (String str : SortingInFiles) {
                                bufferedWriter.write(str);
                                bufferedWriter.newLine();
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("Не найден файл для вывода данных!", e);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка при записи данных в файл!", e);
                        }
                        UncheckedSortingInFiles.clear();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Не найдены файлы для ввода данных!", e);
                } catch (IOException e) {
                    throw new RuntimeException("Ошибка при вводе данных из файла! " +
                            "Возможно вы неправильно указали расширение исходно файла или указали путь к несуществующему файлу." +
                            "Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.", e);
                }

            }
            /*код для файла с числами*/
        } else {
            for (Path inFiles : inFileNames) {
                try (BufferedReader bufferedReader = Files.newBufferedReader(inFiles)) {
                    List<Integer> UncheckedSortingInFiles = new ArrayList<>();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        List<Integer> SortingInFiles = null;
                        long blockSize = 52 + line.length() * 2L;    // максимальные значения(приближенные): 52 байта служебной информации(для 64 разрядной, для 32 разр. будет меньше) + 2 байта на каждый символ;
                        while ((limitBlockSize > blockSize) && (line != null)) {
                            if (!line.contains(" ") && !line.isEmpty()) {    //фильтр на наличие пробела в строке и пустой строки
                                UncheckedSortingInFiles.add(Integer.parseInt(line));
                                blockSize += 52 + line.length() * 2L;
                            } else {
                                System.out.println("Файл " + inFiles + " с данными содержит пробельный символ или пустой элемент. " +
                                        "Элемент считается ошибочным, поэтому не будет считан из файла");
                            }
                            line = bufferedReader.readLine();
                        }
                        /*Проверка сортировки данных в файле. Если в сортировке допущена ошибка, то для данного блока выполняется сортировка слиянием*/
                        SortingInFiles = Utils.checkSortFile(UncheckedSortingInFiles, mode);

                        /*Разворачиваем элементы списка, если необходима сортировка по убыванию*/
                        if (mode.contains("Desc")) {
                            Collections.reverse(SortingInFiles);
                        }

                        /*Создаем временные файлы для внешней сортировки слиянием*/
                        temp = Files.createTempFile(pathTemp, null, ".txt");
                        tempFiles.add(temp);

                        /*Пишем данные из блоков во временные файлы*/
                        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(temp)) {
                            assert SortingInFiles != null;
                            for (Integer num : SortingInFiles) {
                                bufferedWriter.write(num.toString());
                                bufferedWriter.newLine();
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("Не найден файл для вывода данных!", e);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка при записи данных в файл!", e);
                        }
                        UncheckedSortingInFiles.clear();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Не найдены файлы для ввода данных!", e);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Одно или несколько значений из файла не соответствуют типу int. " +
                            "По инструкции требуется использовать числа типа int!", e);
                } catch (IOException e) {
                    throw new RuntimeException("Ошибка при вводе данных из файла! " +
                            "Возможно вы неправильно указали расширение исходно файла или указали путь к несуществующему файлу." +
                            "Ознакомьтесь с инструкцией по работе с программой и повторите ввод параметров.", e);
                }
            }
        }

        /*Сортировка слиянием во внешний файл*/
        if (tempFiles.size() == 2) {
            Utils.writeResultMerge(tempFiles.get(1), outPath); //Если все данные из исходных файлов поместились в один блок
        } else {
            Utils.finishMergeSort(tempFiles, outPath, mode); //Если блоков/временных файлов больше одного
        }

        System.out.println("Сортировка успешно завершена. Адрес файла с отсортированными данными относительно корневой директории: " + outPath);

        /*Удаляем временные файлы и директорию*/
        Utils.deleteTempDirectory(pathTemp);
    }
}
