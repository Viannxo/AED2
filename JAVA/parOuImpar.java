package JAVA;

import java.util.Scanner;
public class parOuImpar {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // Cria um objeto Scanner para entrada de dados

        System.out.print("Digite um numero inteiro: ");
        int numero = scanner.nextInt(); // Lê o número digitado e armazena na variável

        // Verifica se o número é par ou ímpar
        if (numero % 2 == 0) {
            System.out.println("O numero " + numero + " e PAR.");
        } else {
            System.out.println("O numero " + numero + " e IMPAR.");
        }

        scanner.close(); // Fecha o Scanner (boa prática)
    }
}

