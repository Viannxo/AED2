/**
 * EXERCÍCIO 14 - Fila de prioridade mínima com lista duplamente encadeada ordenada
 * Complexidade:
 *   - inserir:        O(n) — precisa encontrar a posição correta.
 *   - removerMinimo:  O(1) — sempre remove do início (menor elemento).
 *
 * LÓGICA:
 * - A lista é mantida SEMPRE em ordem crescente.
 * - inserir: percorra até encontrar posição onde proximo >= x, insira antes.
 * - removerMinimo: retire cabeca.prox (o menor, por definição).
 *
 * Estrutura: cabeca <-> [1] <-> [3] <-> [5] <-> null
 *                        ^mínimo
 */
class CelulaDupla14 {
    int elemento;
    CelulaDupla14 prox, ant;
    public CelulaDupla14() {}
    public CelulaDupla14(int e) { elemento = e; }
}

class FilaPrioridade14 {
    CelulaDupla14 cabeca, fim;

    public FilaPrioridade14() {
        cabeca = new CelulaDupla14(); /* cabeça sentinela */
        fim    = cabeca;
    }

    public void mostrar() {
        System.out.print("[ ");
        for (CelulaDupla14 i = cabeca.prox; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }

    /* ===== INSERIR (mantém ordem crescente) ===== */
    public void inserir(int x) {
        CelulaDupla14 nova = new CelulaDupla14(x);

        /* encontra posição: o primeiro nó cujo elemento >= x */
        CelulaDupla14 pos = cabeca.prox;
        while (pos != null && pos.elemento < x) {
            pos = pos.prox;
        }

        if (pos == null) {
            /* insere no fim */
            nova.ant = fim;
            fim.prox = nova;
            fim = nova;
        } else {
            /* insere antes de 'pos' */
            nova.prox = pos;
            nova.ant  = pos.ant;
            pos.ant.prox = nova;
            pos.ant = nova;
        }
    }

    /* ===== REMOVER MÍNIMO ===== */
    public int removerMinimo() {
        if (cabeca.prox == null) throw new RuntimeException("Fila vazia");
        int resp = cabeca.prox.elemento;
        cabeca.prox = cabeca.prox.prox;
        if (cabeca.prox != null) {
            cabeca.prox.ant = cabeca;
        } else {
            fim = cabeca; /* ficou vazia */
        }
        return resp;
    }
}

public class ExercicioLista14 {
    public static void main(String[] args) {
        FilaPrioridade14 fp = new FilaPrioridade14();

        fp.inserir(5); fp.inserir(3); fp.inserir(8); fp.inserir(1); fp.inserir(4);

        System.out.print("Após inserções: ");
        fp.mostrar(); /* [ 1 3 4 5 8 ] */

        System.out.println("Removido mínimo: " + fp.removerMinimo()); /* 1 */
        System.out.println("Removido mínimo: " + fp.removerMinimo()); /* 3 */
        System.out.print("Restante: ");
        fp.mostrar(); /* [ 4 5 8 ] */
    }
}
