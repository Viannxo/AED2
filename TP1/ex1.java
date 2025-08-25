/*1. Palíndromo (Iterativo)
Crie um método iterativo que receba uma 
string como parâmetro e retorne se essa 
é um “Palíndromo”. Na saída padrão, para
cada linha de entrada, escreva uma linha
de saída com SIM/NÃO indicando se a linha 
é um palíndromo. Observe que a entrada 
pode conter caracteres não letras.*/
import java.util.Scanner;

public class ex1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String entrada = sc.nextLine();

            int i = 0, j = entrada.length() - 1;
            boolean ehPalindromo = true;
            while (i < j) {
                while (i < j && !Character.isLetter(entrada.charAt(i))) i++;
                while (i < j && !Character.isLetter(entrada.charAt(j))) j--;
                if (i < j && entrada.charAt(i) != entrada.charAt(j)) {
                    ehPalindromo = false;
                    break;
                }
                i++;
                j--;
            }

            if (ehPalindromo)
                System.out.println("SIM");
            else
                System.out.println("NAO");
        }

        sc.close();
    }
}