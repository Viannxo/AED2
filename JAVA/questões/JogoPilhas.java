/*
1.  Jogo das Pilhas 
    Claudio inventou um novo jogo, chamado Jogo das Pilhas, e quer
    submetê-lo ao próximo concurso da ICPC (International Collegiate
    Programming Contest). Apesar de muito divertido, o jogo parece 
    ser muito difícil de ganhar. Claudio pediu sua ajuda para avaliar 
    se algumas instâncias do jogo podem ser vencidas.O Jogo das Pilhas
    é individual, e é jogado com três pilhas inicialmente com o mesmo 
    número de cartas. Cada carta tem um valor numérico inteiro de 0 a 9.
    O jogador pode, a qualquer momento, ver o valor de qualquer carta, 
    mas só pode jogar com as cartas que estão no topo das pilhas. Em cada
    rodada, o jogador pode remover qualquer combinação de cartas que estejam 
    no topo da pilha (pode escolher 1, 2 ou até 3 cartas), cuj1os valores 
    somem um múltiplo de 3. O jogo é ganho quando todas as cartas forem removidas
    das pilhas. Se alguma carta não puder ser removida, perde-se o jogo.
    2Entrada3A entrada é composta por várias instâncias. Cada instância 
    é iniciada por um intei4ro $N$ ($0 \le N \le 100$), que identifica o 
    número de cartas em cada pilha. A entrada termina quando $N = 0$. Cada
    uma das $N$ linhas seguintes contém três inteiros $A, B, C$, que descrevem 
    os valores numéricos das cartas em um nível da pilha ($0 \le A, B, C \le 9$).
    As pilhas são descritas do topo até o fundo.


*/



package JAVA.questões;

import java.util.*;

public class JogoPilhas {
    static int[] A, B, C;
    static int[][][] memo;
    static int n;

    static int solve(int i, int j, int k) {
        if (i == n && j == n && k == n) return 1;
        if (memo[i][j][k] != -1) return memo[i][j][k];

        int a = (i < n ? A[i] : -1);
        int b = (j < n ? B[j] : -1);
        int c = (k < n ? C[k] : -1);

        // remove 1
        if (a != -1 && a % 3 == 0 && solve(i+1,j,k) == 1) return memo[i][j][k] = 1;
        if (b != -1 && b % 3 == 0 && solve(i,j+1,k) == 1) return memo[i][j][k] = 1;
        if (c != -1 && c % 3 == 0 && solve(i,j,k+1) == 1) return memo[i][j][k] = 1;

        // remove 2
        if (a != -1 && b != -1 && (a+b)%3 == 0 && solve(i+1,j+1,k) == 1)
            return memo[i][j][k] = 1;
        if (a != -1 && c != -1 && (a+c)%3 == 0 && solve(i+1,j,k+1) == 1)
            return memo[i][j][k] = 1;
        if (b != -1 && c != -1 && (b+c)%3 == 0 && solve(i,j+1,k+1) == 1)
            return memo[i][j][k] = 1;

        // remove 3
        if (a != -1 && b != -1 && c != -1 &&
            (a+b+c)%3 == 0 &&
            solve(i+1,j+1,k+1) == 1)
            return memo[i][j][k] = 1;

        return memo[i][j][k] = 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Digite N (0 para sair): ");
            n = sc.nextInt();
            if (n == 0) break;

            A = new int[n];
            B = new int[n];
            C = new int[n];

            System.out.println("Digite as cartas (A B C):");
            for (int i = 0; i < n; i++) {
                A[i] = sc.nextInt();
                B[i] = sc.nextInt();
                C[i] = sc.nextInt();
            }

            memo = new int[n+1][n+1][n+1];
            for (int[][] m2 : memo)
                for (int[] m1 : m2)
                    Arrays.fill(m1, -1);

            System.out.println("Resultado: " + solve(0,0,0));
            System.out.println();
        }
    }
}
