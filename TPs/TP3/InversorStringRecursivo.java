// Inversor de String Recursivo
package TPs.TP3;

import java.util.Scanner;

public class InversorStringRecursivo {


    // Verifica se a entrada é "FIM"
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    // Função recursiva para inverter a string usando índices
    public static String inverterString(String s, int i) {
        if (i < 0) {
            return ""; // condição de parada
        }
        return s.charAt(i) + inverterString(s, i - 1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (Parada(linha)) {
                break;
            }

            String stringInvertida = inverterString(linha, linha.length() - 1);
            System.out.println(stringInvertida);
        }

        sc.close();
    }
}

