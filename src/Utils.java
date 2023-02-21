import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    /*Проверямем сортировку данных*/
    public static <T extends Comparable<T>> List<T> checkSortFile(List<T> UncheckedSortingInFiles, String mode) {
        List<T> SortingInFiles = new ArrayList<>();
        SortingInFiles.add(UncheckedSortingInFiles.get(0));
        if (mode.contains("Asc")) {
            for (int i = 0; i < UncheckedSortingInFiles.size() - 1; i++) {
                if (UncheckedSortingInFiles.get(i).compareTo(UncheckedSortingInFiles.get(i + 1)) <= 0) {
                    SortingInFiles.add(UncheckedSortingInFiles.get(i + 1));
                } else {
                    SortingInFiles = Utils.mergeSort(UncheckedSortingInFiles);    //Если выявлена ошибка сортировки, производиться сортировка слиянием
                    break;
                }
            }
        } else {
            for (int i = 0; i < UncheckedSortingInFiles.size() - 1; i++) {
                if (UncheckedSortingInFiles.get(i).compareTo(UncheckedSortingInFiles.get(i + 1)) >= 0) {
                    SortingInFiles.add(UncheckedSortingInFiles.get(i + 1));
                } else {
                    SortingInFiles = Utils.mergeSort(UncheckedSortingInFiles);
                    break;
                }
            }
        }
        return SortingInFiles;
    }
    /*Для блоков данных с нарушениями сортировки выполняется сортировка слиянием.
      Рекурсивно разбиваем исходный список до списков с одним элементом в каждом. Т.е. каждый такой список становиться по умолчанию отсортированным.
      Далее сливаем попарно списки единый сортированный список (метод merge())*/
    public static <T extends Comparable<T>> List<T> mergeSort(List<T> sortValues) {
        List<T> left = new ArrayList<>();
        List<T> right = new ArrayList<>();
        int middle;

        if (sortValues.size() == 1) {
            return sortValues;
        } else {
            middle = sortValues.size() / 2;
            for (int i = 0; i < middle; i++) {
                left.add(sortValues.get(i));
            }

            for (int i = middle; i < sortValues.size(); i++) {
                right.add(sortValues.get(i));
            }

            mergeSort(left);
            mergeSort(right);

            merge(left, right, sortValues);
        }
        return sortValues;
    }

    public static <T extends Comparable<T>> void merge(List<T> left, List<T> right, List<T> sortValues) {
        int leftIndex = 0;
        int rightIndex = 0;
        int sortValuesIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ((left.get(leftIndex).compareTo(right.get(rightIndex))) < 0) {
                sortValues.set(sortValuesIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                sortValues.set(sortValuesIndex, right.get(rightIndex));
                rightIndex++;
            }
            sortValuesIndex++;
        }

        List<T> tail;
        int tailIndex;
        if (leftIndex >= left.size()) {
            tail = right;
            tailIndex = rightIndex;
        } else {
            tail = left;
            tailIndex = leftIndex;
        }

        for (int i = tailIndex; i < tail.size(); i++) {
            sortValues.set(sortValuesIndex, tail.get(i));
            sortValuesIndex++;
        }
    }

    /*Внешняя сортировка слиянием. Файлы под индексами j1 и j2 сливаются в файл под индексом i построчно.
      Далее j1 становиться i, а j2++  повторяем слияние, пока не сотанется один общий файл*/
    public static void finishMergeSort(List<Path> tempFiles, Path outPath, String mode) {
        int j1 = 1;
        int j2 = 2;
        int k = 0;
        for (int i = 0; i < tempFiles.size() - 2; i++) {
            try (BufferedReader reader = Files.newBufferedReader(tempFiles.get(j1));
                 BufferedReader reader2 = Files.newBufferedReader(tempFiles.get(j2));
                 BufferedWriter writer = Files.newBufferedWriter(tempFiles.get(i))) {

                String line1 = reader.readLine();
                String line2 = reader2.readLine();

                if (mode.contains("string")) {
                    while (line1 != null && line2 != null) {
                        if (mode.contains("Asc")) {
                            if (line1.compareTo(line2) <= 0) {
                                writer.write(line1);
                                writer.newLine();
                                line1 = reader.readLine();
                            } else {
                                writer.write(line2);
                                writer.newLine();
                                line2 = reader2.readLine();
                            }
                        } else {
                            if (line1.compareTo(line2) > 0) {
                                writer.write(line1);
                                writer.newLine();
                                line1 = reader.readLine();
                            } else {
                                writer.write(line2);
                                writer.newLine();
                                line2 = reader2.readLine();
                            }
                        }
                    }
                } else {
                    while (line1 != null && line2 != null) {
                        if (mode.contains("Asc")) {
                            if (Integer.parseInt(line1) <= Integer.parseInt(line2)) {
                                writer.write(line1);
                                writer.newLine();
                                line1 = reader.readLine();
                            } else {
                                writer.write(line2);
                                writer.newLine();
                                line2 = reader2.readLine();
                            }
                        } else {
                            if (Integer.parseInt(line1) > Integer.parseInt(line2)) {
                                writer.write(line1);
                                writer.newLine();
                                line1 = reader.readLine();
                            } else {
                                writer.write(line2);
                                writer.newLine();
                                line2 = reader2.readLine();
                            }
                        }
                    }
                }
                if (line1 != null) {
                    while (line1 != null) {
                        writer.write(line1);
                        writer.newLine();
                        line1 = reader.readLine();
                    }
                    j2 = i + 3;
                    j1 = i;
                } else if (line2 != null) {
                    while (line2 != null) {
                        writer.write(line2);
                        writer.newLine();
                        line2 = reader2.readLine();
                    }
                    j1 = i + 3;
                    j2 = i;
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Не найден временный файл с одним из блоков отсортированных данных!", e);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при вводе данных из временного файла!", e);
            }
            k = i;
        }
        writeResultMerge(tempFiles.get(k), outPath);
    }

    /*Записываем результат сортировки в целевой файл*/
    public static void writeResultMerge(Path inPath, Path outPath) {
        try (BufferedReader readerResult = Files.newBufferedReader(inPath);
             BufferedWriter writerResult = Files.newBufferedWriter(outPath)) {
            String line = "";
            while ((line = readerResult.readLine()) != null) {
                writerResult.write(line);
                writerResult.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не найден временный файл с отсортированными данными!", e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при вводе данных из временного файла в результирующий файл!", e);
        }
    }

    /*Удаляем временные файлы и директорию*/
    public static void deleteTempDirectory(Path directory) {
        if (Files.exists(directory)) {
            try {
                Files.walkFileTree(directory, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
                        try {
                            Files.delete(path);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("При попытке удаления, не был найден временный файл", e);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка соединения при попытке удаления временного файла", e);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path path, IOException ex) {
                        try {
                            Files.delete(path);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("При попытке удаления, не был найдена директория временных файлов", e);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка соединения при попытке удаления директории временных файлов", e);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Ошибка соединения при удалении временных файлов с директорией", e);
            }
        }
    }
}
