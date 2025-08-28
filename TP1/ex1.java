import java.util.*;

/*1. Palíndromo (Iterativo)
Crie um método iterativo que receba uma 
string como parâmetro e retorne se essa 
é um “Palíndromo”. Na saída padrão, para
cada linha de entrada, escreva uma linha
de saída com SIM/NÃO indicando se a linha 
é um palíndromo. Observe que a entrada 
pode conter caracteres não letras.*/
import java.util.*;

public class ex1 {

    // Método que verifica se a entrada é "FIM"
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String entrada = sc.nextLine();

        // Continua até encontrar "FIM"
        while (!Parada(entrada)) {
            int i = 0, j = entrada.length() - 1;
            boolean ehPalindromo = true;

            while (i < j && ehPalindromo) {
                if (entrada.charAt(i) != entrada.charAt(j)) {
                    ehPalindromo = false;
                }
                i++;
                j--;
            }

            if (ehPalindromo)
                System.out.println("SIM");
            else
                System.out.println("NAO");

            entrada = sc.nextLine();
        }

        sc.close();
    }
}