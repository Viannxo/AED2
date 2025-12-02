#include <stdio.h>
#include <string.h>

int A[110], B[110], C[110];
int memo[110][110][110];
int n;

int solve(int i, int j, int k) {
    if (i == n && j == n && k == n) return 1;
    if (memo[i][j][k] != -1) return memo[i][j][k];

    int a = (i < n ? A[i] : -1);
    int b = (j < n ? B[j] : -1);
    int c = (k < n ? C[k] : -1);

    int sum;

    // Remove 1
    if (a != -1 && a % 3 == 0 && solve(i+1,j,k)) return memo[i][j][k] = 1;
    if (b != -1 && b % 3 == 0 && solve(i,j+1,k)) return memo[i][j][k] = 1;
    if (c != -1 && c % 3 == 0 && solve(i,j,k+1)) return memo[i][j][k] = 1;

    // Remove 2
    if (a != -1 && b != -1 && (a+b)%3 == 0 && solve(i+1,j+1,k)) return memo[i][j][k] = 1;
    if (a != -1 && c != -1 && (a+c)%3 == 0 && solve(i+1,j,k+1)) return memo[i][j][k] = 1;
    if (b != -1 && c != -1 && (b+c)%3 == 0 && solve(i,j+1,k+1)) return memo[i][j][k] = 1;

    // Remove 3
    if (a != -1 && b != -1 && c != -1 && (a+b+c)%3 == 0 &&
        solve(i+1,j+1,k+1))
        return memo[i][j][k] = 1;

    return memo[i][j][k] = 0;
}

int main() {
    while (1) {
        printf("Digite N (0 para sair): ");
        scanf("%d", &n);
        if (n == 0) break;

        printf("\nDigite as cartas no formato A B C:\n");
        for (int i = 0; i < n; i++)
            scanf("%d %d %d", &A[i], &B[i], &C[i]);

        memset(memo, -1, sizeof(memo));

        printf("Resultado: %d\n\n", solve(0,0,0));
    }

    return 0;
}
