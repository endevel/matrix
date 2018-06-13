Тестовое задание Ц.Банк
Задание:

 

Дана матрица из элементов равных 0 или 1. Единицы в такой матрице образуют 

домены, причем в домен могут входить только соседние элементы по горизонтали и 

вертикали. Программа должна определять количество доменов в заданной матрице.

Программа должна быть написана с использованием Java 7.

 

Входные данные: строка содержащая путь к файлу, в котором находится матрица. 

 

Пример матрицы:

1 0 0 0 1 0

1 1 0 0 0 1

0 0 0 0 0 0

0 0 0 0 0 1

0 0 0 0 0 1

Для этой матрицы можно определить 4 различных домена: 

{(1,1), (2,1), (2,2)}, {(1,5)}, {(2,6)}, {(4,6), (5,6)}.

Выходные данные: целое число – количество доменов в заданной матрице.  
