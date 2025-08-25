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
    public static String cifrar(String texto) {
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= 32 && c <= 126) {
                c = (char) ((c - 32 + 3) % 95 + 32);
            }
            resultado.append(c);
        }
        
        return resultado.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String entrada = sc.nextLine();
            if(entrada.isEmpty()) break;
             if (entrada.equals("FIM")){
                break;
            }else{
                String cifrada = cifrar(entrada);
                System.out.println(cifrada);
            }
        }

        sc.close();
    }
}