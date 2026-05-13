/**
 * EXERCÍCIO 2 - Inverter lista duplamente encadeada flexível in-place
 * Complexidade: Theta(n) — troca prox/ant de cada nó uma única vez.
 *
 * LÓGICA:
 * - Percorra do primeiro nó real até o fim.
 * - Para cada nó: troque prox <-> ant.
 * - Ao terminar, o que era ultimo agora é o primeiro real.
 * - Ajuste cabeca.prox para apontar para o antigo ultimo.
 *
 * Antes:  cabeca -> [A] <-> [B] <-> [C] -> null
 * Depois: cabeca -> [C] <-> [B] <-> [A] -> null
 */
class CelulaDupla2 {

    int elemento;
    CelulaDupla2 prox, ant;

    public CelulaDupla2() {
    }

    public CelulaDupla2(int e) {
        elemento = e;
    }
}

class ListaDupla2 {

    CelulaDupla2 cabeca;

    public ListaDupla2() {
        cabeca = new CelulaDupla2();
        /* celula-cabeca sem valor */
    }

    public void inserirFim(int x) {
        CelulaDupla2 nova = new CelulaDupla2(x);
        if (cabeca.prox == null) {
            cabeca.prox = nova;
            nova.ant = cabeca;
        } else {
            /* acha o último */
            CelulaDupla2 i = cabeca.prox;
            while (i.prox != null) {
                i = i.prox;
            }
            i.prox = nova;
            nova.ant = i;
        }
    }

    public void mostrar() {
        System.out.print("[ ");
        for (CelulaDupla2 i = cabeca.prox; i != null; i = i.prox) {
            System.out.print(i.elemento + " ");
        }
        System.out.println("]");
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public void inverter() {
        if (cabeca.prox == null || cabeca.prox.prox == null) {
            return;
        }

        CelulaDupla2 atual = cabeca.prox;
        CelulaDupla2 novoPrimeiro = null;

        while (atual != null) {
            // guarda o próximo original
            CelulaDupla2 temp = atual.prox;

            // inverte os ponteiros
            atual.prox = atual.ant;
            atual.ant = temp;

            // último processado será o novo primeiro
            novoPrimeiro = atual;

            // avança
            atual = temp;
        }

        // antigo primeiro virou último
        CelulaDupla2 antigoPrimeiro = cabeca.prox;

        // atualiza início da lista
        cabeca.prox = novoPrimeiro;
        novoPrimeiro.ant = cabeca;

        // antigo primeiro agora é o último
        antigoPrimeiro.prox = null;
    }
}

public class ExercicioLista02 {
    public static void main(String[] args) {
        ListaDupla2 l = new ListaDupla2();
        l.inserirFim(1);
        l.inserirFim(2);
        l.inserirFim(3);
        l.inserirFim(4);

        System.out.print("Antes:  ");
        l.mostrar();

        l.inverter();


        System.out.print("Depois: ");
        l.mostrar();
        /* esperado: [ 4 3 2 1 ] */
    }
}
