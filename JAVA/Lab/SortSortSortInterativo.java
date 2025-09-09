package JAVA.Lab;

import java.util.Scanner;

public class SortSortSortInterativo {

    Scanner sc = new Scanner(System.in);

    int n;
    int m;
    int[] vetor;

    public void fillVec(int n) {
        this.n = n;
        vetor = new int[n];
        // Preenche o vetor com os números inteiros fornecidos pelo usuário
        for (int i = 0; i < n; i++) {
            vetor[i] = sc.nextInt();
        }
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

        for (int j = 0; j < vetor.length; j++) {
            System.out.println(vetor[j]);
        }
        // System.out.println("]");
    }

    public static void main(String[] args) {
        SortSortSortInterativo sssI = new SortSortSortInterativo();
        int n;

        do{

            // System.out.println("Digite o valor de N: ");
        n = sssI.sc.nextInt();

        // System.out.println("Digite o valor de M: ");
        sssI.m = sssI.sc.nextInt();

        //print de N e M
        System.out.println(n + " " + sssI.m);

        
        sssI.fillVec(n);

        sssI.ordenaMod(sssI.m);
        

        }while(n!= 0 && sssI.m != 0);
        sssI.sc.close();
    }
    
}
