package JAVA.Lab;

import java.util.Random;
import java.util.Arrays;

public class compOrdenacao {

    private static final Random RANDOM = new Random();

    private static void swap(int[] vet, int i, int j) {
        int temp = vet[i];
        vet[i] = vet[j];
        vet[j] = temp;
    }

    private static int partition(int[] vet, int esq, int dir, int nPivo) {
        swap(vet, nPivo, dir);
        int pivotValue = vet[dir];
        
        int i = (esq - 1); 

        for (int j = esq; j < dir; j++) {
            if (vet[j] <= pivotValue) {
                i++;
                swap(vet, i, j);
            }
        }
        
        swap(vet, i + 1, dir);
        
        return (i + 1);
    }

    public static void QuickSortPrimPivo(int[] vet, int esq, int dir) {
        if (esq < dir) {
            int nPivo = esq;
            int pi = partition(vet, esq, dir, nPivo);
            
            QuickSortPrimPivo(vet, esq, pi - 1);
            QuickSortPrimPivo(vet, pi + 1, dir);
        }
    }

    public static void QuickSortUltmPivo(int[] vet, int esq, int dir) {
        if (esq < dir) {
            int nPivo = dir;
            int pi = partition(vet, esq, dir, nPivo);
            
            QuickSortUltmPivo(vet, esq, pi - 1);
            QuickSortUltmPivo(vet, pi + 1, dir);
        }
    }

    public static void QuickSortRandomPivo(int[] vet, int esq, int dir) {
        if (esq < dir) {
            int nPivo = esq + RANDOM.nextInt(dir - esq + 1);
            
            int pi = partition(vet, esq, dir, nPivo);
            
            QuickSortRandomPivo(vet, esq, pi - 1);
            QuickSortRandomPivo(vet, pi + 1, dir);
        }
    }

    public static void QuickSortMediana3(int[] vet, int esq, int dir) {
        if (esq < dir) {
            int meio = esq + (dir - esq) / 2;
            
            int nPivo;
            if ((vet[esq] < vet[meio] && vet[meio] < vet[dir]) || (vet[dir] < vet[meio] && vet[meio] < vet[esq])) {
                nPivo = meio;
            } else if ((vet[meio] < vet[esq] && vet[esq] < vet[dir]) || (vet[dir] < vet[esq] && vet[esq] < vet[meio])) {
                nPivo = esq;
            } else {
                nPivo = dir;
            }
            
            int pi = partition(vet, esq, dir, nPivo);
            
            QuickSortMediana3(vet, esq, pi - 1);
            QuickSortMediana3(vet, pi + 1, dir);
        }
    }

    public static void main(String[] args) {
        int[] original = {10, 80, 30, 90, 40, 50, 70};
        int n = original.length;

        System.out.println("Vetor original: " + Arrays.toString(original));

        int[] vet1 = Arrays.copyOf(original, n);
        QuickSortPrimPivo(vet1, 0, n - 1);
        System.out.println("\nQuickSortPrimPivo (Pivo no inicio): " + Arrays.toString(vet1));

        int[] vet2 = Arrays.copyOf(original, n);
        QuickSortUltmPivo(vet2, 0, n - 1);
        System.out.println("QuickSortUltmPivo (Pivo no fim): " + Arrays.toString(vet2));
        
        int[] vet3 = Arrays.copyOf(original, n);
        QuickSortRandomPivo(vet3, 0, n - 1);
        System.out.println("QuickSortRandomPivo (Pivo aleatorio): " + Arrays.toString(vet3));

        int[] vet4 = Arrays.copyOf(original, n);
        QuickSortMediana3(vet4, 0, n - 1);
        System.out.println("QuickSortMediana3 (Mediana de 3): " + Arrays.toString(vet4));
    }
}
