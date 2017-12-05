package org.bykov.cbtest;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Domain {

    public Domain() {
        cells = new HashSet<Integer>();
    }

    public int getSize() {
        return cells.size();
    }

    public boolean contain(int cell) {
        return cells.contains(cell);
    }

    public void addCell(int cell) {
        cells.add(cell);
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();

        Iterator<Integer> iterator = cells.iterator();
        while (iterator.hasNext()) {
            if (value.length() > 0) {
                value.append(":");
            }
            value.append(iterator.next());
        }

        return value.toString();
    }

    private HashSet<Integer> cells;
}
