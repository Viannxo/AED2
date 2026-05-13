/**
 * EXERCÍCIO 18 - Deque (fila dupla) com lista duplamente encadeada
 * Complexidade de TODAS as operações: O(1).
 *
 * ESTRUTURA:
 *   cabeca <-> [nó1] <-> [nó2] <-> ... <-> [nóN] <-> cauda
 *   (cabeca e cauda são sentinelas sem valor)
 *
 * LÓGICA:
 * - inserirFrente: insere novo nó entre cabeca e cabeca.prox.
 * - inserirFim:    insere novo nó entre cauda.ant e cauda.
 * - removerFrente: remove cabeca.prox.
 * - removerFim:    remove cauda.ant.
 *
 * Por que O(1)?
 *   Com ponteiros para os dois extremos, não precisamos percorrer a lista.
 */
class CelulaDupla18 {
    int elemento;
    CelulaDupla18 prox, ant;
    public CelulaDupla18() {}
    public CelulaDupla18(int e) { elemento = e; }
}

class Deque18 {
    CelulaDupla18 cabeca, cauda;

    public Deque18() {
        cabeca = new CelulaDupla18(); /* sentinela início */
        cauda  = new CelulaDupla18(); /* sentinela fim    */
        cabeca.prox = cauda;
        cauda.ant   = cabeca;
    }

    public boolean estaVazio() { return cabeca.prox == cauda; }

    /* ===== INSERIR NA FRENTE ===== */
    public void inserirFrente(int x) {
        CelulaDupla18 nova = new CelulaDupla18(x);
        nova.prox = cabeca.prox;
        nova.ant  = cabeca;
        cabeca.prox.ant = nova;
        cabeca.prox     = nova;
    }

    /* ===== INSERIR NO FIM ===== */
    public void inserirFim(int x) {
        CelulaDupla18 nova = new CelulaDupla18(x);
        nova.ant  = cauda.ant;
        nova.prox = cauda;
        cauda.ant.prox = nova;
        cauda.ant      = nova;
    }

    /* ===== REMOVER DA FRENTE ===== */
    public int removerFrente() {
        if (estaVazio()) throw new RuntimeException("Deque vazio");
        CelulaDupla18 alvo = cabeca.prox;
        cabeca.prox = alvo.prox;
        alvo.prox.ant = cabeca;
        return alvo.elemento;
    }

    /* ===== REMOVER DO FIM ===== */
    public int removerFim() {
        if (estaVazio()) throw new RuntimeException("Deque vazio");
        CelulaDupla18 alvo = cauda.ant;
        cauda.ant = alvo.ant;
        alvo.ant.prox = cauda;
        return alvo.elemento;
    }

    public void mostrar() {
        System.out.print("Deque [frente->fim]: [ ");
        for (CelulaDupla18 i = cabeca.prox; i != cauda; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }
}

public class ExercicioLista18 {
    public static void main(String[] args) {
        Deque18 d = new Deque18();

        d.inserirFim(2);
        d.inserirFrente(1);  /* [1, 2] */
        d.inserirFim(3);     /* [1, 2, 3] */
        d.inserirFrente(0);  /* [0, 1, 2, 3] */
        d.mostrar();

        System.out.println("RemoverFrente: " + d.removerFrente()); /* 0 */
        System.out.println("RemoverFim:    " + d.removerFim());    /* 3 */
        d.mostrar(); /* [1, 2] */
    }
}
