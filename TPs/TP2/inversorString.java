/*Crie um método iterativo que recebe uma string como
 parâmetro e retorna a string invertida. Na saída padrão,
  para cada linha de entrada, escreva uma linha de saída 
  com a string invertida. Por exemplo, se a entrada for abcde,
   a saída deve ser edcba. */

import java.util.Scanner;

public class inversorString {

    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    
    public static String inverterString(String s) {
        // Cria um array de caracteres para montar a nova string invertida
        char[] arrayInvertido = new char[s.length()];
        int comprimento = s.length();
        
        // Percorre a string de trás para frente e preenche o novo array
        for (int i = 0; i < comprimento; i++) {
            arrayInvertido[i] = s.charAt(comprimento - 1 - i);
        }
        
        
        return new String(arrayInvertido);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (Parada(linha)) {
                break;
            }
            
            String stringInvertida = inverterString(linha);
            System.out.println(stringInvertida);
        }

        sc.close();
    }
}

