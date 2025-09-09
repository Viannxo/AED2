package TPs.TP3;

import java.util.Scanner;

public class somaDigRecur {
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    }
}
