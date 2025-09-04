/* 
Alteração Aleatória
Crie um método iterativo que receba uma string,
sorteie uma letra minúscula aleatória (ASCII entre ‘a’ e ‘z’)
e substitua todas as ocorrências dessa letra na string pela 
letra sorteada. O método deve retornar a string com as alterações
feitas.
Na saída padrão, para cada linha de entrada, escreva a string 
resultante após a substituição
 * 
*/

import java.util.Scanner;
import java.util.Random;

public class ex4 {

    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    public static String substituir(String texto, Random gerador) {
        char letra1 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
        char letra2 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));

        char[] resultado = new char[texto.length()];

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == letra1) {
                resultado[i] = letra2;
            } else {
                resultado[i] = c;
            }
        }

        String resp = "";
        for (int i = 0; i < resultado.length; i++) {
            resp += resultado[i];
        }

        return resp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4);

        String linha = sc.nextLine();
        while (!Parada(linha)) {
            String resultado = substituir(linha, gerador);
            System.out.println(resultado);
            linha = sc.nextLine();
        }

        sc.close();
    }
}
