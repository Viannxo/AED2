package TPs.TP3;

import java.util.Scanner;

public class SomaDigitosRecursivo {

    // Verifica se a entrada é "FIM"
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    // Função recursiva que soma os dígitos de um número
    public static int somaDigitos(int n) {
        if (n == 0) {
            return 0; // condição de parada
        }
        return (n % 10) + somaDigitos(n / 10);
    }

        public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String entrada = sc.nextLine();

            if (Parada(entrada)) {
                break;
            }

            int num = Integer.parseInt(entrada);

            int soma = somaDigitos(num);

            System.out.println(soma);
        }

        sc.close();
    }
}
