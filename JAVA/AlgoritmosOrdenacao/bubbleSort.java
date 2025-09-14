import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class bubbleSort {
    Scanner scan = new Scanner(System.in);
    Random random = new Random();
    int n;
    int[] vetor;

    public void fillVec() {
        System.out.println("Digite o tamanho do vetor: ");
        n = scan.nextInt();
        vetor = new int[n];
        for (int i = 0; i < n; i++) {
            vetor[i] = random.nextInt(100);
        }
        System.out.println("Vetor gerado: " + Arrays.toString(vetor));
    }

    public void swap(int i, int j) {
        int aux = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = aux;
    }

    public static void main(String[] agrs) {
        bubbleSort bs = new bubbleSort();
        bs.fillVec();
        int count = 0;
        int count2 = 0;
        int i = 0;
        boolean troca;

        do {
            troca = false;
            count2++;

            for (int j = 0; j < bs.n - 1 - i; j++) {
                if (bs.vetor[j] > bs.vetor[j + 1]) {
                    bs.swap(j, j + 1);
                    troca = true;
                }
                count++;
            }
            i++;
        } while (troca && i < bs.n - 1);
        System.out.println("Vetor ordenado: " + Arrays.toString(bs.vetor));
        System.out.println("Numero de comparações: " + count);
        System.out.println("Numero de iterações: " + count2);
    }
}
