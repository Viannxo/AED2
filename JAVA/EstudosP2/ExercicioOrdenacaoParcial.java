/**
 * EXERCÍCIOS DE ORDENAÇÃO PARCIAL
 * Agrupa: Ord1..Ord10 (Selection/Insertion/Bubble parciais)
 * 
 * ============================================================
 * ORD1 - K menores salários com Selection Sort parcial  O(k*n)
 * ============================================================
 * LÓGICA: Faça apenas k iterações externas do Selection Sort.
 * A i-ésima iteração move o (i+1)-ésimo menor para sal[i].
 * Ao terminar, sal[0..k-1] contém os k menores EM ORDEM CRESCENTE.
 *
 * ============================================================
 * ORD2 - Placar parcial com Insertion Sort decrescente   O(j^2)
 * ============================================================
 * LÓGICA: Ordene somente o prefixo pts[0..j-1] em ordem decrescente.
 * Cada elemento inserido desloca os menores para a direita.
 *
 * ============================================================
 * ORD3 - Pódio: 3 primeiros com 3 passagens de Selection  O(3n)
 * ============================================================
 * Número de comparações: (n-1)+(n-2)+(n-3) = 3n-6.
 *
 * ============================================================
 * ORD7 - Top-k maiores com Bubble Sort parcial           O(k*n)
 * ============================================================
 * Cada passagem de Bubble Sort "borbulha" o maior para o final.
 * Com k passagens: os k maiores estarão nas últimas k posições.
 *
 * ============================================================
 * ORD9 - Ranking parcial com Insertion Sort decrescente  O(k^2)
 * ============================================================
 * Igual ao ORD2 mas retorna apenas o prefixo k.
 *
 * ============================================================
 * ORD10 - Menores tempos com Selection Sort parcial      O(k*n)
 * ============================================================
 * Igual ao ORD1 mas retorna cópia dos k primeiros.
 */
public class ExercicioOrdenacaoParcial {

    /* ======================== ORD1 ======================== */
    public static int[] kMenores(int[] sal, int n, int k) {
        for (int i = 0; i < k; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (sal[j] < sal[minIdx]) minIdx = j;
            }
            /* troca */
            int tmp = sal[i]; sal[i] = sal[minIdx]; sal[minIdx] = tmp;
        }
        int[] resp = new int[k];
        for (int i = 0; i < k; i++) resp[i] = sal[i];
        return resp;
    }

    /* ======================== ORD2 ======================== */
    public static void placarParcial(int[] pts, int n, int j) {
        /* Insertion Sort decrescente apenas no prefixo [0..j-1] */
        for (int i = 1; i < j && i < n; i++) {
            int chave = pts[i];
            int k2 = i - 1;
            /* desloca elementos menores para a direita */
            while (k2 >= 0 && pts[k2] < chave) {
                pts[k2 + 1] = pts[k2];
                k2--;
            }
            pts[k2 + 1] = chave;
        }
    }

    /* ======================== ORD3 ======================== */
    public static int[] podio(int[] tempos, int n) {
        int[] copia = tempos.clone();
        for (int i = 0; i < 3 && i < n; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (copia[j] < copia[minIdx]) minIdx = j;
            }
            int tmp = copia[i]; copia[i] = copia[minIdx]; copia[minIdx] = tmp;
        }
        int tam = Math.min(3, n);
        int[] resp = new int[tam];
        for (int i = 0; i < tam; i++) resp[i] = copia[i];
        return resp;
    }

    /* ======================== ORD7 ======================== */
    public static void topKMaiores(int[] notas, int n, int k) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (notas[j] > notas[j + 1]) {
                    int tmp = notas[j]; notas[j] = notas[j+1]; notas[j+1] = tmp;
                }
            }
        }
        /* agora notas[n-k .. n-1] contém os k maiores em ordem crescente */
    }

    /* ======================== ORD9 ======================== */
    public static void rankingParcial(int[] vendas, int n, int k) {
        /* Insertion Sort decrescente apenas no prefixo [0..k-1] */
        for (int i = 1; i < k && i < n; i++) {
            int chave = vendas[i];
            int j = i - 1;
            while (j >= 0 && vendas[j] < chave) {
                vendas[j + 1] = vendas[j];
                j--;
            }
            vendas[j + 1] = chave;
        }
    }

    /* ======================== ORD10 ======================== */
    public static int[] menoresTempos(int[] tempos, int n, int k) {
        int[] copia = tempos.clone();
        for (int i = 0; i < k; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (copia[j] < copia[minIdx]) minIdx = j;
            }
            int tmp = copia[i]; copia[i] = copia[minIdx]; copia[minIdx] = tmp;
        }
        int[] resp = new int[k];
        for (int i = 0; i < k; i++) resp[i] = copia[i];
        return resp;
    }

    /* ======================== TESTES ======================== */
    public static void main(String[] args) {
        System.out.println("=== ORD1: k menores salários ===");
        int[] sal = {500, 200, 800, 100, 350};
        int[] km = kMenores(sal, sal.length, 3);
        for (int v : km) System.out.print(v + " "); /* 100 200 350 */
        System.out.println();

        System.out.println("=== ORD2: placar parcial decrescente ===");
        int[] pts = {40, 90, 30, 70, 50};
        placarParcial(pts, pts.length, 3);
        for (int v : pts) System.out.print(v + " "); /* 90 40 30 70 50 */
        System.out.println();

        System.out.println("=== ORD3: pódio (3 menores tempos) ===");
        int[] tempos = {12, 7, 15, 9, 3, 11};
        int[] p = podio(tempos, tempos.length);
        for (int v : p) System.out.print(v + " "); /* 3 7 9 */
        System.out.println();

        System.out.println("=== ORD7: top-k maiores com Bubble ===");
        int[] notas = {60, 90, 50, 80, 70};
        topKMaiores(notas, notas.length, 2);
        for (int v : notas) System.out.print(v + " "); /* ... 80 90 no final */
        System.out.println();

        System.out.println("=== ORD10: menores tempos ===");
        int[] t2 = {12, 7, 15, 9, 3, 11};
        int[] mt = menoresTempos(t2, t2.length, 3);
        for (int v : mt) System.out.print(v + " "); /* 3 7 9 */
        System.out.println();
    }
}
