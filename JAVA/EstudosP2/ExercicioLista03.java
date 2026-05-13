/**
 * EXERCÍCIO 3 - Intercalar duas listas simples ordenadas em uma nova lista ordenada
 * Complexidade: Theta(m + n) — cada elemento de ambas as listas é visitado uma vez.
 *
 * LÓGICA (Merge clássico):
 * - Ponteiro ia = primeiro real de A, ib = primeiro real de B.
 * - Compare ia.elemento e ib.elemento.
 * - Insira o menor no resultado e avance o ponteiro correspondente.
 * - Quando uma lista esgota, copie o restante da outra.
 *
 * Exemplo:
 *   A: [1, 3, 5]   B: [2, 4, 6]
 *   Resultado: [1, 2, 3, 4, 5, 6]
 */
class Celula3 {
    int elemento;
    Celula3 prox;
    public Celula3() {}
    public Celula3(int e) { elemento = e; }
}

class Lista3 {
    Celula3 primeiro, ultimo;

    public Lista3() {
        primeiro = ultimo = new Celula3(); /* celula-cabeca */
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula3(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula3 i = primeiro.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public static Lista3 intercalar(Lista3 a, Lista3 b) {
        Lista3 resultado = new Lista3();

        Celula3 ia = a.primeiro.prox; /* primeiro real de A */
        Celula3 ib = b.primeiro.prox; /* primeiro real de B */

        /* enquanto ambas têm elementos */
        while (ia != null && ib != null) {
            if (ia.elemento <= ib.elemento) {
                resultado.inserirFim(ia.elemento);
                ia = ia.prox;
            } else {
                resultado.inserirFim(ib.elemento);
                ib = ib.prox;
            }
        }

        /* copia sobras de A */
        while (ia != null) {
            resultado.inserirFim(ia.elemento);
            ia = ia.prox;
        }

        /* copia sobras de B */
        while (ib != null) {
            resultado.inserirFim(ib.elemento);
            ib = ib.prox;
        }

        return resultado;
    }
}

public class ExercicioLista03 {
    public static void main(String[] args) {
        Lista3 a = new Lista3();
        a.inserirFim(1); a.inserirFim(3); a.inserirFim(5);

        Lista3 b = new Lista3();
        b.inserirFim(2); b.inserirFim(4); b.inserirFim(6);

        System.out.print("A: "); a.mostrar();
        System.out.print("B: "); b.mostrar();

        Lista3 c = Lista3.intercalar(a, b);
        System.out.print("Resultado: "); c.mostrar();
        /* esperado: [ 1 2 3 4 5 6 ] */
    }
}
