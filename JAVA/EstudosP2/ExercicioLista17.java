/**
 * EXERCÍCIO 17 - Pilha que suporta getMinimo() em O(1)
 * Complexidade de todas as operações: O(1).
 *
 * LÓGICA:
 * - Usa DUAS pilhas: 'dados' (pilha normal) e 'minimos' (rastreia mínimos).
 * - empilhar(x):
 *     - Empilha x em 'dados'.
 *     - Empilha min(x, minimos.topo()) em 'minimos'.
 *       (se minimos vazia, empilha x).
 * - desempilhar():
 *     - Desempilha de ambas as pilhas.
 *     - Retorna o valor de 'dados'.
 * - getMinimo():
 *     - Retorna o topo de 'minimos' (sem remover).
 *
 * Por que funciona?
 *   'minimos[i]' sempre guarda o mínimo dos elementos em 'dados[0..i]'.
 *   Ao desempilhar um elemento, o mínimo anterior é restaurado automaticamente.
 *
 * Exemplo:
 *   Empilha 3 → dados:[3], minimos:[3]
 *   Empilha 1 → dados:[1,3], minimos:[1,3]
 *   Empilha 4 → dados:[4,1,3], minimos:[1,1,3]
 *   getMinimo() = 1
 *   Desempilha 4 → dados:[1,3], minimos:[1,3] → getMinimo() = 1
 *   Desempilha 1 → dados:[3], minimos:[3] → getMinimo() = 3
 */
class CelulaPilha17 {
    int elemento;
    CelulaPilha17 prox;
    public CelulaPilha17(int e) { elemento = e; }
}

class PilhaSimples17 {
    CelulaPilha17 topo;

    public void empilhar(int x) {
        CelulaPilha17 nova = new CelulaPilha17(x);
        nova.prox = topo;
        topo = nova;
    }

    public int desempilhar() {
        if (topo == null) throw new RuntimeException("Pilha vazia");
        int resp = topo.elemento;
        topo = topo.prox;
        return resp;
    }

    public int verTopo() {
        if (topo == null) throw new RuntimeException("Pilha vazia");
        return topo.elemento;
    }

    public boolean estaVazia() { return topo == null; }
}

class PilhaMin17 {
    PilhaSimples17 dados   = new PilhaSimples17();
    PilhaSimples17 minimos = new PilhaSimples17();

    /* ===== EMPILHAR ===== */
    public void empilhar(int x) {
        dados.empilhar(x);
        if (minimos.estaVazia()) {
            minimos.empilhar(x);
        } else {
            minimos.empilhar(Math.min(x, minimos.verTopo()));
        }
    }

    /* ===== DESEMPILHAR ===== */
    public int desempilhar() {
        minimos.desempilhar(); /* sincroniza */
        return dados.desempilhar();
    }

    /* ===== GET MÍNIMO ===== */
    public int getMinimo() {
        if (minimos.estaVazia()) throw new RuntimeException("Pilha vazia");
        return minimos.verTopo();
    }
}

public class ExercicioLista17 {
    public static void main(String[] args) {
        PilhaMin17 p = new PilhaMin17();

        p.empilhar(3);
        System.out.println("Mínimo após empilhar 3: " + p.getMinimo()); /* 3 */

        p.empilhar(1);
        System.out.println("Mínimo após empilhar 1: " + p.getMinimo()); /* 1 */

        p.empilhar(4);
        System.out.println("Mínimo após empilhar 4: " + p.getMinimo()); /* 1 */

        p.desempilhar(); /* remove 4 */
        System.out.println("Mínimo após desempilhar 4: " + p.getMinimo()); /* 1 */

        p.desempilhar(); /* remove 1 */
        System.out.println("Mínimo após desempilhar 1: " + p.getMinimo()); /* 3 */
    }
}
