package org.bykov.cbtest;

import java.io.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * Представление для исходной матрицы
 *
 * Т.к. наша цель просто просканировать содержимое матрицы и найти домены, то немного трансформируем ее
 * для упрощения перебора элементов.
 * 1. Запишем все элементы в однмерный массив
 * 2. Добавим к исходной матрице поля-ограничители (x) - это упростит нам процесс перебора элементов
 *    Минус такой схемы - нестандартная нумерация элементов/координат
 *
 *   x x x x x x
 *   x 0 0 1 0 x
 *   x 1 1 0 0 x
 *   x 0 0 0 0 x
 *   x x x x x x
 *
 *   А тестовая матрица-пример из письма будет представлена так:
 *
 *   0  0 0 0 0 0 0  0

     0  1 0 0 0 1 0  0
     0  1 1 0 0 0 1  0
     0  0 0 0 0 0 0  0
     0  0 0 0 0 0 1  0
     0  0 0 0 0 0 1  0

     0  0 0 0 0 0 0  0

    Нумерация элементов
 00  01 02 03 04 05 06  07

 08  09 10 11 12 13 14  15
 16  17 18 19 20 21 22  23
 24  25 26 27 28 29 30  31
 32  33 34 35 36 37 38  39
 40  41 42 43 44 45 46  47

 48  49 50 51 52 53 54  55

 */
public class Matrix {
    public Matrix() {
        values = new ArrayList<Integer>();
        usedMap = new ArrayList<Integer>();
        domains = new ArrayList<Domain>();
        findDir = new int[DIR_COUNT];
    }

    public void readFromFile(String source) {
        logger.debug("Read data file: " + source);
        values.clear();
        usedMap.clear();
        height = 0;
        width = 0;

        try {
            String line;

            BufferedReader br = new BufferedReader(new FileReader(source));

            while ((line = br.readLine()) != null) {
                width = addRow(line);
                height++;
            }

            addFictionRow(width);
            initValues();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Read file error : "  + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Read file error : "  + e.getMessage());
        }
    }

    /**
     * Выполняет поиск доменов
     * @return количество найденных доменов
     */
    public int findDomains() {
        int result = 0;
        domains.clear();

        for ( int i = firstNdx; i < lastNdx; i++) {
            if (values.get(i) == DOMAIN_VALUE) {
                if (usedMap.get(i) == FREE_CELL) {
                    // Элемент не входит ни в один домен - можно добавлять...
                    createDomain(i);
                    result++;
                }
            }
        }

        return result;
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int getDomainCount() {
        if (domains.size() == 0) {
            return findDomains();
        }

        return domains.size();
    }

    public Domain getDomain(int index) {
        return domains.get(index);
    }

    private void createDomain(int index) {
        Domain domain = new Domain();
        domain.addCell(index);
        usedMap.set(index, USED_CELL);

        appendNextCell(domain, index, DIR_RIGHT);
        appendNextCell(domain, index, DIR_DOWN);
        // остальные направления не смотрим, т.к. двигаясь по массиву сверху-вниз и слева-направо,
        // мы уже смотрели элементы "слева-сверху"
    }

    /**
     * Проверяет соседние с fromNdx элементы
     * @param domain  - формируемый домен
     * @param fromNdx - последний добавленный элемент
     * @param dir     - направление поиска
     */
    private void appendNextCell(Domain domain, int fromNdx, int dir) {
        boolean found = false;
        for (int d = 0; d < DIR_COUNT; d++) {
            if (findDir[dir] == -findDir[d])
                // направление с которого пришли, не проверяем, там все, что надо, уже отмечено
                continue;

            int next = fromNdx + findDir[d];
            if (values.get(next) == DOMAIN_VALUE) {
                if (usedMap.get(next) == FREE_CELL) {
                    // Еще один элемент можно добавить
                    domain.addCell(next);
                    usedMap.set(next, USED_CELL);
                    found = true;
                    // ... и проверим соседние элементы,
                    // возможно их тоже можно включить в текущий домен....
                    appendNextCell(domain, next, d);
                }
            }
        }

        if (!found) {
            if (!domain.isFreeze()) {
                domains.add(domain);
                // "зафиксируем" что уже есть в списке...
                domain.setFreeze(true);
            }
        }
    }

    private void initValues() {
        findDir[DIR_TOP]        = -width-2;
        findDir[DIR_RIGHT]      = 1;

        findDir[DIR_DOWN]       = -findDir[DIR_TOP];
        findDir[DIR_LEFT]       = -findDir[DIR_RIGHT];

        firstNdx = width + 3;
        lastNdx = values.size() - 3;

        for ( int i = 0; i < values.size(); i++) {
            usedMap.add(FREE_CELL);
        }
    }


    /**
     * добавляет содержимое строки матрицы в массив
     * @param strRow
     * @return размер матрицы по оси X
     */
    private int addRow(String strRow) {
        String[] cells = strRow.split(" ");

        if (height == 0) {
            addFictionRow(cells.length);
        }

        // ограничитель слева
        values.add(BORDER_VALUE);
        for (String cell : cells) {
            int val = Integer.parseInt(cell);
            values.add(val);
        }
        //  ограничитель справа
        values.add(BORDER_VALUE);

        return cells.length;
    }

    /**
     * Добавляет строку с элементами-ограничителями
     * @param rowSize
     */
    private void addFictionRow(int rowSize) {
        for ( int i = 0; i < rowSize + 2; i++) {
            values.add(BORDER_VALUE);
        }
    }

    /** Массив для хранения элементов матрицы */
    private ArrayList<Integer> values;
    /** Отмечает элементы матрицы, вошедшие в какой-либо домен*/
    private ArrayList<Integer> usedMap;

    private ArrayList<Domain> domains;

    private int height;
    private int width;

    private int[] findDir;
    private int firstNdx = 0;
    private int lastNdx = 0;

    // Направления поиска следующего элемента для включения в домен
    private static int DIR_TOP        = 0;
    private static int DIR_RIGHT      = 1;
    private static int DIR_DOWN       = 2;
    private static int DIR_LEFT       = 3;

    private static int DIR_COUNT = 4;

    private static int BORDER_VALUE = 0;
    private static int DOMAIN_VALUE = 1;
    private static int FREE_CELL = 0;
    private static int USED_CELL = 1;

    private static final Logger logger = Logger.getLogger(Matrix.class);
}
