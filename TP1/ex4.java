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
    public static String substituir(String texto, Random gerador) {
        char letra1 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
        char letra2 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
        
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == letra1) {
                resultado.append(letra2);
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            if (linha.equals("FIM")) break;
            String resultado = substituir(linha, gerador);
            System.out.println(resultado);
        }

        sc.close();
    }
}

