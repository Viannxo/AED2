/* 
Ciframento de César
O Imperador Júlio César foi um dos principais nomes do 
Império Romano e, segundo historiadores, utilizava um 
algoritmo de criptografia chamado “Ciframento de César”
para enviar mensagens a seus generais.
Esse algoritmo substitui cada letra por outra deslocada no
alfabeto um número fixo de posições. Por exemplo, se a chave
for 3, o caractere ‘a’ é substituído por ‘d’, ‘b’ por ‘e’, 
e assim por diante.
Crie um método iterativo que receba uma string como parâmetro 
e retorne outra contendo o resultado do ciframento de César,
considerando uma chave fixa de 3.
Na saída padrão, para cada linha de entrada, escreva a linha 
criptografada.
 * 
*/
import java.util.Scanner;

public class ex3 {

    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    public static String cifrar(String texto) {
        char[] resultado = new char[texto.length()];

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (c >= 32 && c <= 126) {
                c = (char) ((c - 32 + 3) % 95 + 32);
            }
            resultado[i] = c;
        }

        String resp = "";
        for (int i = 0; i < resultado.length; i++) {
            resp += resultado[i];
        }

        return resp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String entrada = sc.nextLine();
        while (!Parada(entrada)) {
            String cifrada = cifrar(entrada);
            System.out.println(cifrada);
            entrada = sc.nextLine();
        }

        sc.close();
    }
}
