package JAVA.AlgoritmosOrdenacao;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class selectSort {
    int n;
    int[] vetor;
    Scanner scan = new Scanner(System.in);
    Random random = new Random();

    public void fillVec(    ){
        System.out.println("Digite o tamanho do vetor: ");
        n = scan.nextInt();
        vetor = new int[n];
        for(int i=0; i<n; i++){
            vetor[i] = random.nextInt(100);
        }
        System.out.println("Vetor gerado: " + Arrays.toString(vetor));
    }
    public void swap( int i, int j){
        int aux = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = aux;
    }



    public static void main(String[] agrs){
        selectSort ss = new selectSort();
        ss.fillVec();
        int min;
        int count2=0;
        int count = 0;
        for(int i=0; i<ss.n-1; i++){
            min = i;
            count2++;
            for(int j=i+1; j<ss.n; j++){
                if(ss.vetor[j] < ss.vetor[min]){
                    min = j;
                }
                count++;
            }
            ss.swap(i, min);

        }
        System.out.println("Vetor ordenado: " + Arrays.toString(ss.vetor));
        System.out.println("Numero de comparações: " + count);
        System.out.println("Numero de iterações: " + count2);
    }   
}
