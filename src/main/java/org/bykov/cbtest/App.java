package org.bykov.cbtest;

public class App {
    public static void main(String[] args) {
        System.out.println("App is running . . .");

        Matrix matrix = new Matrix();
        if (args.length > 0) {
            System.out.println("Read file: " + args[0]);
            matrix.readFromFile(args[0]);
        }

        if (matrix.getDomainCount() > 0) {
            System.out.println(matrix.getDomainCount());
            for ( int i = 0; i < matrix.getDomainCount(); i++) {
                System.out.println(matrix.getDomain(i));
            }
        }
    }
}
