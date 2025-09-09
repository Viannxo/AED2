package JAVA.Lab;

import java.util.Random;
import java.util.Scanner;

public class sortSortSort {
    Scanner sc = new Scanner(System.in);
    Random random = new Random();
    int n;
    int m;
    // orderar numero na ordem ascendente de seu modulo M
    int[] vetor;

    public void fillVec() {
        System.out.println("Digite o tamanho do vetor: ");
        n = sc.nextInt();
        vetor = new int[n];
        for (int i = 0; i < n; i++) {
            vetor[i] = random.nextInt(100);
        }
        System.out.print("Vetor gerado: [");
        for (int j = 0; j < vetor.length; j++) {
            System.out.print(vetor[j]);
            if (j < vetor.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public int mod(int x, int m) {
        return x % m;
    }

    public void ordenaMod(int m) {
        for (int i = 0; i < vetor.length - 1; i++) {
            for (int j = 0; j < vetor.length - 1 - i; j++) {
                int mod1 = mod(vetor[j], m);
                int mod2 = mod(vetor[j + 1], m);

                boolean troca = false;

                // Comparar pelos módulos
                if (mod1 > mod2) {
                    troca = true;
                } else if (mod1 == mod2) {
                    // Desempate: ímpar antes de par
                    if (vetor[j] % 2 == 0 && vetor[j + 1] % 2 != 0) {
                        troca = true;
                    }
                    // Se ambos ímpares: maior primeiro
                    else if (vetor[j] % 2 != 0 && vetor[j + 1] % 2 != 0) {
                        if (vetor[j] < vetor[j + 1]) {
                            troca = true;
                        }
                    }
                    // Se ambos pares: menor primeiro
                    else if (vetor[j] % 2 == 0 && vetor[j + 1] % 2 == 0) {
                        if (vetor[j] > vetor[j + 1]) {
                            troca = true;
                        }
                    }
                }

                if (troca) {
                    int temp = vetor[j];
                    vetor[j] = vetor[j + 1];
                    vetor[j + 1] = temp;
                }
            }
        }

        // Imprime vetor ordenado
        System.out.print("Vetor ordenado: [");
        for (int j = 0; j < vetor.length; j++) {
            System.out.print(vetor[j]);
            if (j < vetor.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        sortSortSort sss = new sortSortSort();
        sss.fillVec();

        System.out.println("Digite o valor de M: ");
        sss.m = sss.sc.nextInt();

        sss.ordenaMod(sss.m);
    }
}
