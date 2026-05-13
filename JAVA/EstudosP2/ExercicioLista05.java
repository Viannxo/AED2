/**
 * EXERCÍCIO 5 - Dividir lista simples em sublistas por índice par/ímpar
 * Complexidade: Theta(n) — percorre a lista uma única vez.
 *
 * LÓGICA:
 * - Mantém um contador de índice (começa em 0).
 * - Índice par (0, 2, 4...): insere na lista 'pares'.
 * - Índice ímpar (1, 3, 5...): insere na lista 'impares'.
 * - Retorna array com [pares, impares].
 *
 * Exemplo: [A, B, C, D, E]
 *   pares:   [A, C, E]   (índices 0, 2, 4)
 *   ímpares: [B, D]      (índices 1, 3)
 */
class Celula5 {
    int elemento;
    Celula5 prox;
    public Celula5() {}
    public Celula5(int e) { elemento = e; }
}

class Lista5 {
    Celula5 primeiro, ultimo;

    public Lista5() {
        primeiro = ultimo = new Celula5(); /* celula-cabeca */
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula5(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula5 i = primeiro.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public Lista5[] dividir() {
        Lista5 parIdx   = new Lista5(); /* índices 0, 2, 4 ... */
        Lista5 imparIdx = new Lista5(); /* índices 1, 3, 5 ... */

        int idx = 0;
        for (Celula5 atual = primeiro.prox; atual != null; atual = atual.prox, idx++) {
            if (idx % 2 == 0) {
                parIdx.inserirFim(atual.elemento);
            } else {
                imparIdx.inserirFim(atual.elemento);
            }
        }

        return new Lista5[]{parIdx, imparIdx};
    }
}

public class ExercicioLista05 {
    public static void main(String[] args) {
        Lista5 l = new Lista5();
        for (int i = 10; i <= 60; i += 10) l.inserirFim(i);

        System.out.print("Original: "); l.mostrar();

        Lista5[] resultado = l.dividir();
        System.out.print("Índices pares (0,2,4...):  "); resultado[0].mostrar();
        System.out.print("Índices ímpares (1,3,5...): "); resultado[1].mostrar();
        /* esperado pares: [10 30 50], impares: [20 40 60] */
    }
}
