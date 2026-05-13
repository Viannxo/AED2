/**
 * EXERCÍCIO 8 - Ordenar uma pilha usando apenas uma pilha auxiliar
 * Complexidade: Theta(n^2) — n elementos, cada um potencialmente movido n vezes.
 * Resultado: topo da pilha original = maior elemento.
 *
 * LÓGICA (insertion sort em pilha):
 * Repita enquanto original não estiver vazia:
 *   1. Retire o topo da original → guarda em 'temp'.
 *   2. Enquanto auxiliar não está vazia E auxiliar.topo() > temp:
 *      - mova da auxiliar para a original.
 *   3. Empilhe 'temp' na auxiliar.
 * No final, mova tudo de auxiliar de volta para original.
 *
 * Exemplo: original [3,1,4,2] (topo=2) → resultado [1,2,3,4] (topo=4)
 */
class CelulaPilha8 {
    int elemento;
    CelulaPilha8 prox;
    public CelulaPilha8(int e) { elemento = e; }
}

class Pilha8 {
    CelulaPilha8 topo;

    public void empilhar(int x) {
        CelulaPilha8 nova = new CelulaPilha8(x);
        nova.prox = topo;
        topo = nova;
    }

    public int desempilhar() {
        if (topo == null) throw new RuntimeException("Pilha vazia");
        int resp = topo.elemento;
        topo = topo.prox;
        return resp;
    }

    public boolean estaVazia() { return topo == null; }

    public int verTopo() { return topo.elemento; }

    public void mostrar() {
        System.out.print("Pilha (topo->base): [ ");
        for (CelulaPilha8 i = topo; i != null; i = i.prox)
            System.out.print(i.elemento + " ");
        System.out.println("]");
    }
}

public class ExercicioLista08 {

    /* ===== IMPLEMENTAÇÃO ===== */
    public static void ordenarPilha(Pilha8 original) {
        Pilha8 auxiliar = new Pilha8();

        while (!original.estaVazia()) {
            int temp = original.desempilhar();

            /* move da auxiliar para original enquanto auxiliar.topo > temp */
            while (!auxiliar.estaVazia() && auxiliar.verTopo() > temp) {
                original.empilhar(auxiliar.desempilhar());
            }

            /* insere temp na posição correta na auxiliar */
            auxiliar.empilhar(temp);
        }

        /* devolve tudo para original (agora ordenado: topo = maior) */
        while (!auxiliar.estaVazia()) {
            original.empilhar(auxiliar.desempilhar());
        }
    }

    public static void main(String[] args) {
        Pilha8 p = new Pilha8();
        p.empilhar(3); p.empilhar(1); p.empilhar(4); p.empilhar(2);

        System.out.print("Antes:  "); p.mostrar();

        ordenarPilha(p);

        System.out.print("Depois: "); p.mostrar();
        /* esperado topo=4: [ 4 3 2 1 ] */
    }
}
