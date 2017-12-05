package org.bykov.cbtest;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixTest {
    @Test
    public void simpleCreate() {
        Matrix matrix = new Matrix();
        assertNotNull(matrix);
    }

    @Test
    public void testReadFile() {
        Matrix matrix = new Matrix();
        matrix.readFromFile("C:\\matrix_01.txt");
        assertEquals(6, matrix.getHeight());
        assertEquals(7, matrix.getWidth());
    }

    @Test
    public void testFindDomains() {
        Matrix matrix = new Matrix();
        matrix.readFromFile("C:\\test_matrix_02.txt");
        assertEquals(5, matrix.getHeight());
        assertEquals(6, matrix.getWidth());
        assertEquals(4, matrix.getDomainCount());
    }
}