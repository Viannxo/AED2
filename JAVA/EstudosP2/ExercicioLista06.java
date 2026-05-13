/**
 * EXERCÍCIO 6 - Remover a k-ésima célula a partir do final (O(n), uma passagem)
 * Complexidade: Theta(n) — dois ponteiros com distância k.
 *
 * LÓGICA (algoritmo dos dois ponteiros):
 * 1. Avance o ponteiro 'frente' k+1 posições a partir da cabeça.
 *    - Se não for possível: k >= tamanho → nada a remover.
 * 2. Avance 'frente' e 'tras' juntos até 'frente' chegar ao null.
 * 3. Nesse momento, 'tras' está no nó ANTERIOR ao alvo.
 * 4. tras.prox = tras.prox.prox  (remove o alvo).
 *
 * Por que funciona?
 *   'frente' sai de cabeca+k posições antes de 'tras'.
 *   Quando 'frente' = null, 'tras' está exatamente k posições do final.
 *
 * Exemplo: [1,2,3,4,5], k=2 → remove o 4 (2º do final) → [1,2,3,5]
 */
class Celula6 {
    int elemento;
    Celula6 prox;
    public Celula6() {}
    public Celula6(int e) { elemento = e; }
}

class Lista6 {
    Celula6 primeiro, ultimo;

    public Lista6() {
        primeiro = ultimo = new Celula6(); /* celula-cabeca */
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula6(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula6 i = primeiro.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public void removerKDoFinal(int k) {
        /* 'frente' avança k+1 passos a partir da cabeça */
        Celula6 frente = primeiro;
        for (int i = 0; i <= k; i++) {
            if (frente == null) return; /* k maior que o tamanho */
            frente = frente.prox;
        }

        /* agora avança os dois juntos */
        Celula6 tras = primeiro;
        while (frente != null) {
            frente = frente.prox;
            tras   = tras.prox;
        }

        /* 'tras' é o nó anterior ao alvo */
        Celula6 alvo = tras.prox;
        if (alvo != null) {
            if (alvo == ultimo) ultimo = tras; /* removendo o último */
            tras.prox = alvo.prox;
        }
    }
}

public class ExercicioLista06 {
    public static void main(String[] args) {
        Lista6 l = new Lista6();
        for (int i = 1; i <= 5; i++) l.inserirFim(i);

        System.out.print("Original:   "); l.mostrar();

        l.removerKDoFinal(2); /* remove o 4 (2º do final) */
        System.out.print("Após k=2:   "); l.mostrar();
        /* esperado: [ 1 2 3 5 ] */

        l.removerKDoFinal(1); /* remove o 5 (1º do final) */
        System.out.print("Após k=1:   "); l.mostrar();
        /* esperado: [ 1 2 3 ] */
    }
}
