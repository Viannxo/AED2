/**
 * EXERCÍCIO 16 - Mesclar k listas ordenadas em uma única lista ordenada
 * Complexidade: O(n*k) onde n = total de elementos.
 *   - Cada seleção direta percorre k ponteiros para achar o menor: O(k).
 *   - Repetido n vezes: O(n*k).
 *
 * LÓGICA (seleção direta entre k ponteiros):
 * - Mantenha um array de ponteiros 'ptr[0..k-1]', cada um aponta para o
 *   próximo elemento não-inserido de cada lista.
 * - A cada iteração, encontre o ptr com o menor elemento.
 * - Insira esse elemento no resultado e avance o ptr correspondente.
 * - Repita até todos os ptrs serem null.
 *
 * Exemplo: k=3, listas [1,4],[2,5],[3,6] → [1,2,3,4,5,6]
 */
class Celula16 {
    int elemento;
    Celula16 prox;
    public Celula16() {}
    public Celula16(int e) { elemento = e; }
}

class Lista16 {
    Celula16 primeiro, ultimo;

    public Lista16() {
        primeiro = ultimo = new Celula16();
    }

    public void inserirFim(int x) {
        ultimo.prox = new Celula16(x);
        ultimo = ultimo.prox;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (Celula16 i = primeiro.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public static Lista16 mesclarK(Lista16[] listas) {
        int k = listas.length;
        Celula16[] ptr = new Celula16[k];

        /* inicializa ponteiros para o primeiro elemento real de cada lista */
        for (int i = 0; i < k; i++) {
            ptr[i] = listas[i].primeiro.prox;
        }

        Lista16 resultado = new Lista16();

        while (true) {
            /* encontra o índice com o menor elemento atual */
            int menorIdx = -1;
            for (int i = 0; i < k; i++) {
                if (ptr[i] != null) {
                    if (menorIdx == -1 || ptr[i].elemento < ptr[menorIdx].elemento) {
                        menorIdx = i;
                    }
                }
            }

            if (menorIdx == -1) break; /* todas as listas esgotadas */

            resultado.inserirFim(ptr[menorIdx].elemento);
            ptr[menorIdx] = ptr[menorIdx].prox; /* avança o ponteiro vencedor */
        }

        return resultado;
    }
}

public class ExercicioLista16 {
    public static void main(String[] args) {
        Lista16 a = new Lista16();
        a.inserirFim(1); a.inserirFim(4); a.inserirFim(7);

        Lista16 b = new Lista16();
        b.inserirFim(2); b.inserirFim(5); b.inserirFim(8);

        Lista16 c = new Lista16();
        c.inserirFim(3); c.inserirFim(6); c.inserirFim(9);

        Lista16 resultado = Lista16.mesclarK(new Lista16[]{a, b, c});
        System.out.print("Resultado: ");
        resultado.mostrar(); /* [ 1 2 3 4 5 6 7 8 9 ] */
    }
}
