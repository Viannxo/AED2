/**
 * EXERCÍCIO 1 - Remover todos os elementos pares de uma lista simples flexível
 * Complexidade: Theta(n) — percorre a lista uma única vez.
 *
 * LÓGICA:
 * - Dois ponteiros: 'ant' (anterior) e 'atual'.
 * - Se atual.elemento é par: ant.prox = atual.prox (pula o nó).
 * - Se não é par: apenas avança ant.
 * - A célula-cabeça garante que nunca tentamos remover antes do primeiro nó real.
 */
class Celula1 {
    int elemento;
    Celula1 prox;
    public Celula1() { prox = null; }
    public Celula1(int e) { elemento = e; prox = null; }
}

class Lista1 {
    Celula1 primeiro, ultimo;

    public Lista1() {
        primeiro = ultimo = new Celula1(); /* celula-cabeca */
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula1(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula1 i = primeiro.prox; i != null; i = i.prox) {
            System.out.print(i.elemento + " ");
        }
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public void removerPares() {
        Celula1 ant   = primeiro;          /* começa na cabeça */
        Celula1 atual = primeiro.prox;

        while (atual != null) {
            if (atual.elemento % 2 == 0) {
                /* elemento é par: remover */
                ant.prox = atual.prox;

                /* se removemos o último, atualizar ponteiro */
                if (atual == ultimo) {
                    ultimo = ant;
                }
                /* ant não avança; atual avança */
                atual = ant.prox;
            } else {
                /* elemento é ímpar: apenas avança */
                ant   = atual;
                atual = atual.prox;
            }
        }
    }
}

public class ExercicioLista01 {
    public static void main(String[] args) {
        Lista1 l = new Lista1();
        l.inserirFim(1);
        l.inserirFim(2);
        l.inserirFim(3);
        l.inserirFim(4);
        l.inserirFim(5);
        l.inserirFim(6);

        System.out.print("Antes:  ");
        l.mostrar();

        l.removerPares();

        System.out.print("Depois: ");
        l.mostrar();
        /* esperado: [ 1 3 5 ] */
    }
}
