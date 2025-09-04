/*
 * Crie um método iterativo que recebe duas strings como 
 * parâmetros e retorna true se as strings são anagramas 
 * uma da outra, ou false caso contrário.
 * Na saída padrão, para cada par de strings de entrada, escreva
 * uma linha de saída com SIM/NÃO indicando se as strings são 
 * anagramas. Por exemplo, se as entradas forem listen e silent,
 * a saída deve ser SIM.
 */

import java.util.Scanner;

public class Anagramas {

    // Verifica se a linha indica o fim
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    // Normaliza o caractere: A..Z -> a..z, outros permanecem iguais
    public static char normalizar(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char)(c + 32); // converte para minúscula
        }
        return c;
    }

    // Verifica se duas strings são anagramas
    public static boolean saoAnagramas(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        int[] contagem = new int[256];

        for (int i = 0; i < s1.length(); i++) {
            contagem[normalizar(s1.charAt(i))]++;
            contagem[normalizar(s2.charAt(i))]--;
        }

        for (int c : contagem) {
            if (c != 0) return false;
        }

        return true;
    }

    // Lê palavra ignorando espaços e normalizando caracteres
    public static String lerPalavra(String linha, int inicio, int fim) {
        String palavra = "";
        for (int i = inicio; i < fim; i++) {
            char c = linha.charAt(i);
            if (c != ' ') {
                palavra = palavra + normalizar(c);
            }
        }
        return palavra;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String linha = sc.nextLine();
            if (Parada(linha)) break;

            int indiceHifen = -1;
            for (int i = 0; i < linha.length(); i++) {
                if (linha.charAt(i) == '-') {
                    indiceHifen = i;
                    break;
                }
            }

            String palavra1 = lerPalavra(linha, 0, indiceHifen);
            String palavra2 = lerPalavra(linha, indiceHifen + 1, linha.length());

            if (saoAnagramas(palavra1, palavra2)) {
                System.out.println("SIM");
            } else {
                 System.out.println("N\u00C3O");
            }
        }

        sc.close();
    }
}

