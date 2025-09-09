

import java.util.Scanner;

public class gridLargada {
    int n; // numero de carros
    int[] vet; // vetor posições
    int[] vet2;
    int ultrapassagens;
    

    //Scanner sc = new Scanner(System.in);

    public int[] input(int n) {
        this.n = n;
        vet = new int[n];
        for (int i = 0; i < n; i++) {
            vet[i] = sc.nextInt();
        }
        return vet;
    }

    public static void troca(int[] vet, int i, int j) {
        int aux = vet[i];
        vet[i] = vet[j];
        vet[j] = aux;
    }

    public void bubbleSort(int[] vet) {
        for (int i = 0; i < vet.length - 1; i++) {
            for (int j = 0; j < vet.length - 1 - i; j++) {
                if (vet[j] > vet[j + 1]) {
                    troca(vet, j, j + 1);
                    ultrapassagens++;
                }
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int x=0;
        do{
        gridLargada gl = new gridLargada();
        gl.n = gl.sc.nextInt();
        gl.vet = gl.input(gl.n);

        gl.vet2 = gl.input(gl.n);
        gl.ultrapassagens = 0;
        gl.bubbleSort(gl.vet2);
        System.out.println(gl.ultrapassagens);
        x++;
        // Close the scanner after the loop
        
        }while(x<3);
        sc.close();
    }

}

// movimentação pra esquerda é positiva e movimentação pra direita é nula