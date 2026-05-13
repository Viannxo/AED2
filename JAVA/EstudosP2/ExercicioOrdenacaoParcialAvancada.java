/**
 * ORD11 - Top-k maiores vendas com HeapSort parcial
 * Complexidade: O(n + k*log n)
 *   - Construir heap máximo: O(n)  (heapify bottom-up)
 *   - k extrações do máximo: k * O(log n)
 *   - Total: O(n + k*log n) — melhor que O(n*log n) quando k << n.
 *
 * LÓGICA:
 * 1. Build-max-heap sobre o vetor inteiro.
 * 2. Faça k extrações: troque raiz (máximo) com o último,
 *    reduza tamanho do heap, restaure a propriedade (sift-down).
 * 3. Copie os k valores extraídos para o resultado.
 *
 * ============================================================
 *
 * ORD12 - QuickSelect: k menores em posições [0..k-1]
 * Complexidade média: O(n)  |  Pior caso: O(n^2) (pivôs ruins)
 *
 * LÓGICA (QuickSelect / partição parcial):
 * 1. Particione o subarray [esq..dir] em torno de um pivô.
 *    - Elementos < pivô vão para a esquerda.
 *    - Pivô fica na posição final p.
 * 2. Se p == k-1 → os k menores já estão em [0..k-1]. Fim.
 * 3. Se p > k-1 → recurse apenas para a esquerda (esq..p-1).
 * 4. Se p < k-1 → recurse apenas para a direita (p+1..dir).
 *
 * Diferença do QuickSort: só recurs​amos em UM lado, daí O(n) médio.
 */
public class ExercicioOrdenacaoParcialAvancada {

    /* ===== ORD11 — HeapSort parcial ===== */

    /* sift-down: restaura heap-max a partir do índice i, heap de tamanho n */
    static void siftDown(int[] vet, int i, int n) {
        while (true) {
            int maior = i;
            int esq   = 2 * i + 1;
            int dir   = 2 * i + 2;
            if (esq < n && vet[esq] > vet[maior]) maior = esq;
            if (dir < n && vet[dir] > vet[maior]) maior = dir;
            if (maior == i) break;
            int tmp = vet[i]; vet[i] = vet[maior]; vet[maior] = tmp;
            i = maior;
        }
    }

    public static int[] topKHeap(int[] vendas, int n, int k) {
        int[] v = vendas.clone(); /* não destruir o original */

        /* 1. Build-max-heap: O(n) */
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(v, i, n);
        }

        int[] resp = new int[k];
        int tamHeap = n;

        /* 2. k extrações: cada uma é O(log n) */
        for (int i = 0; i < k; i++) {
            resp[i] = v[0];              /* extrai o máximo */
            v[0] = v[--tamHeap];         /* move o último para a raiz */
            siftDown(v, 0, tamHeap);     /* restaura o heap */
        }

        return resp; /* resp[0] = 1º maior, resp[1] = 2º maior, etc. */
    }

    /* ===== ORD12 — QuickSelect / QuickSort parcial ===== */

    /* Particiona vet[esq..dir] e retorna a posição final do pivô */
    static int particionar(int[] vet, int esq, int dir) {
        int pivo = vet[dir]; /* último elemento como pivô */
        int i    = esq - 1;

        for (int j = esq; j < dir; j++) {
            if (vet[j] <= pivo) {
                i++;
                int tmp = vet[i]; vet[i] = vet[j]; vet[j] = tmp;
            }
        }
        int tmp = vet[i+1]; vet[i+1] = vet[dir]; vet[dir] = tmp;
        return i + 1;
    }

    /**
     * Garante que os k menores elementos estejam em vet[0..k-1] (não necessariamente ordenados).
     * Para ordená-los depois, basta ordenar apenas o prefixo de tamanho k.
     */
    public static void quickParcial(int[] leituras, int esq, int dir, int k) {
        if (esq >= dir) return;

        int p = particionar(leituras, esq, dir);

        if (p == k - 1) {
            return; /* k menores já em [0..k-1] */
        } else if (p > k - 1) {
            quickParcial(leituras, esq, p - 1, k); /* recurse só à esquerda */
        } else {
            quickParcial(leituras, p + 1, dir, k); /* recurse só à direita */
        }
    }

    /* ======================== TESTES ======================== */
    public static void main(String[] args) {
        System.out.println("=== ORD11: Top-3 maiores vendas ===");
        int[] vendas = {300, 100, 500, 200, 400, 150};
        int[] top = topKHeap(vendas, vendas.length, 3);
        for (int v : top) System.out.print(v + " "); /* 500 400 300 */
        System.out.println();

        System.out.println("=== ORD12: 3 menores leituras ===");
        int[] leituras = {7, 2, 10, 1, 9, 3, 8};
        quickParcial(leituras, 0, leituras.length - 1, 3);
        /* os 3 menores (1, 2, 3) estão em [0..2], sem garantia de ordem interna */
        System.out.print("Primeiros 3 posicionados: ");
        for (int i = 0; i < 3; i++) System.out.print(leituras[i] + " ");
        System.out.println();
    }
}
