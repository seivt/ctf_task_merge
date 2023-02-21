Версия Java - Java 18

Система сборки и сторонние библиотеки не использовались.

# Инструкция по запуску
В папке src находятся скомпилированные и исходные файлы.

Входные файлы в кодировке UTF-8 необходимо поместить в директорию src/files

Выходной файл будет генерироваться/перезаписываться также в директории src/files



Параметры программы задаются при запуске через аргументы командной строки, по порядку: 

1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию; 
 
2. тип данных (-s или -i), обязательный; 
 
3. имя выходного файла, обязательное; 
 
4. имена входных файлов, не менее одного. 

5. После входных параметров можно указать размер блока, для считывания данных с входных файлов. Размер задается в Mb. 
Параметр не обязательный, по умолчанию используется размер блока в 30 Mb.

Для запуска необходимо перейти в терминале к адресу нахождения файла Main.java и выполнить в командной строке команду согласно примерам ниже:


Примеры запуска из командной строки для Windows:

java Main -i -a out.txt in.txt 10 (для целых чисел по возрастанию, размер блока = 10 Mb)

java Main -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию, размер блока = 30 Mb)

java Main -d -s out.txt in1.txt in2.txt 100 (для строк по убыванию, размер блока = 100 Mb)

# Особенности реализации

В данной реализации данные считываются из входных файлов блоками, размер которых можно задавать при запуске в командной строке (по умолчанию 30Mb).
Данные в каждом блоке проверяются на ошибочные данные и на отсотрированность. 
Если будет обнаружено нарушение сортировки, то проводиться сортировка слиянием данных в блоке. 
(Так как в ТЗ указано, что "как именно обрабатывать поврежденный файл – на усмотрение разработчика"  и "файл должен содержать отсортированные данные даже в случае ошибок, однако 
возможна потеря ошибочных данных" я решил проводить сортировку, вместо отсечения ошибочных данных, чтобы сохранить максимум данных). 

Затем отсортированные данные из блока сохраняются во внешние файлы и производиться внешняя сортировка слиянием в один результирующий файл.





