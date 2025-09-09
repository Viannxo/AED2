package JAVA.Lab;

import java.util.Scanner;

public class gridLargada {
    int n; //numero de carros
    int[] vet; //vetor posições
    int cont; //contador de movimentos

    Scanner sc = new Scanner(System.in);

    public int[] input (int n){
        this.n = n;
        vet = new int[n];
        for (int i = 0; i < n; i++) {
            vet[i] = i + 1;
        }
        return vet;
    }

    public static void main(String[] args) {
        gridLargada gl = new gridLargada();
        gl.n = gl.sc.nextInt();
        System.out.println(gl.n);
        gl.vet = gl.input(gl.n);
        for (int i = 0; i < gl.n; i++) {
            System.out.print(gl.vet[i] + " ");
        }
        gl.sc.close();
    }

}

//movimentação pra esquerda é positiva e movimentação pra direita é nula