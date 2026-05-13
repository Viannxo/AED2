/**
 * EXERCÍCIO 11 - Implementar fila usando duas pilhas flexíveis
 * 
 * Complexidade AMORTIZADA:
 *   - enfileirar: O(1) sempre (empilha em p1).
 *   - desenfileirar: O(1) amortizado. 
 *     Quando p2 está vazia, move tudo de p1 para p2 (O(n) naquela operação).
 *     Mas cada elemento é movido no máximo uma vez, então amortizado é O(1).
 *
 * LÓGICA:
 * - p1 é a pilha de "entrada" (novos elementos chegam aqui).
 * - p2 é a pilha de "saída"  (elementos saem daqui).
 * - Ao desenfileirar, se p2 vazia → mova tudo de p1 para p2 (inverte a ordem).
 * - Retire do topo de p2.
 *
 * Por que funciona?
 *   Empilhar em p1: 1,2,3 → p1 tem 3 no topo.
 *   Mover para p2:  vira 1 no topo (ordem FIFO restaurada).
 */
class CelulaPilha11 {
    int elemento;
    CelulaPilha11 prox;
    public CelulaPilha11(int e) { elemento = e; }
}

class Pilha11 {
    CelulaPilha11 topo;

    public void empilhar(int x) {
        CelulaPilha11 nova = new CelulaPilha11(x);
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
}

class FilaComPilhas11 {
    Pilha11 p1, p2;

    public FilaComPilhas11() {
        p1 = new Pilha11(); /* entrada */
        p2 = new Pilha11(); /* saída   */
    }

    /* ===== ENFILEIRAR: sempre em p1 ===== */
    public void enfileirar(int x) {
        p1.empilhar(x);
    }

    /* ===== DESENFILEIRAR ===== */
    public int desenfileirar() {
        if (p2.estaVazia()) {
            /* transfere tudo de p1 para p2 (inverte a ordem → FIFO) */
            while (!p1.estaVazia()) {
                p2.empilhar(p1.desempilhar());
            }
        }
        if (p2.estaVazia()) throw new RuntimeException("Fila vazia");
        return p2.desempilhar();
    }

    public boolean estaVazia() {
        return p1.estaVazia() && p2.estaVazia();
    }
}

public class ExercicioLista11 {
    public static void main(String[] args) {
        FilaComPilhas11 fila = new FilaComPilhas11();

        fila.enfileirar(1);
        fila.enfileirar(2);
        fila.enfileirar(3);

        System.out.println("Desenfileirar: " + fila.desenfileirar()); /* 1 */
        System.out.println("Desenfileirar: " + fila.desenfileirar()); /* 2 */

        fila.enfileirar(4);
        fila.enfileirar(5);

        System.out.println("Desenfileirar: " + fila.desenfileirar()); /* 3 */
        System.out.println("Desenfileirar: " + fila.desenfileirar()); /* 4 */
        System.out.println("Desenfileirar: " + fila.desenfileirar()); /* 5 */
    }
}
