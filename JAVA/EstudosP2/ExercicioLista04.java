/**
 * EXERCÍCIO 4 - Contar elementos repetidos em lista simples
 * Complexidade: Theta(n^2) — para cada nó, varre o restante procurando duplicata.
 *
 * LÓGICA:
 * - Para cada nó 'i', percorra os nós seguintes 'j'.
 * - Se i.elemento == j.elemento, incremente o contador e saia do loop interno
 *   (evita contar o mesmo valor mais de uma vez).
 * - Usa flag 'encontrado' para contar cada valor repetido apenas uma vez.
 *
 * Exemplo: [1, 2, 2, 3, 3, 3] -> retorna 2 (valores 2 e 3)
 */
class Celula4 {
    int elemento;
    Celula4 prox;
    public Celula4() {}
    public Celula4(int e) { elemento = e; }
}

class Lista4 {
    Celula4 primeiro, ultimo;

    public Lista4() {
        primeiro = ultimo = new Celula4(); /* celula-cabeca */
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula4(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula4 i = primeiro.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public int contarRepetidos() {
        int cont = 0;

        for (Celula4 i = primeiro.prox; i != null; i = i.prox) {
            boolean encontrado = false;

            /* verifica se o mesmo valor aparece ANTES de i (já foi contado?) */
            for (Celula4 k = primeiro.prox; k != i; k = k.prox) {
                if (k.elemento == i.elemento) {
                    encontrado = true; /* já contamos este valor antes */
                    break;
                }
            }

            if (!encontrado) {
                /* verificar se este valor aparece alguma vez depois de i */
                for (Celula4 j = i.prox; j != null; j = j.prox) {
                    if (j.elemento == i.elemento) {
                        cont++; /* é repetido e ainda não foi contado */
                        break;
                    }
                }
            }
        }

        return cont;
    }
}

public class ExercicioLista04 {
    public static void main(String[] args) {
        Lista4 l = new Lista4();
        l.inserirFim(1);
        l.inserirFim(2);
        l.inserirFim(2);
        l.inserirFim(3);
        l.inserirFim(3);
        l.inserirFim(3);

        System.out.print("Lista: ");
        l.mostrar();

        System.out.println("Elementos repetidos: " + l.contarRepetidos());
        /* esperado: 2 */
    }
}
