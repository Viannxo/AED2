import java.util.Scanner;

    // [3, 1 , 8 , 6 , 2]
    // [1, 3, 8 , 6 , 2 ]
    // [1, 3, 6 , 8 , 2 ]
    // [1, 2, 3 , 6 , 8 ]

public class InserSort {

    Scanner sc = new Scanner(System.in);
    int n;
    int[] vet;
    int j;

    public int[] input(int n) {
        this.n = n;
        vet = new int[n];
        for (int i = 0; i < n; i++) {
            vet[i] = sc.nextInt();
        }
        return vet;
    }

    private static void InsertionSort(int[] vet, int n) {
        for (int i = 1; i < n; i++) {
            int aux=vet[i];
            int j=i-1;
            while(j>=0 && vet[j]>aux){
                vet[j+1]=vet[j];
                j--;
            }
            vet[j+1]=aux;
        }
    }

    public void print(int[] vet, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(vet[i] + " ");
        }
    }
    public static void main(String[] args) {
        InserSort is = new InserSort();
        is.n = is.sc.nextInt();
        is.vet = is.input(is.n);
        InsertionSort(is.vet, is.n);
        is.print(is.vet, is.n);
        is.sc.close();
        
    }
}
