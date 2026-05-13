/**
 * EXERCÍCIO 10 - Reverter os k primeiros elementos de uma fila usando pilha
 * Complexidade: Theta(n) — k operações de pilha + (n-k) recolocações.
 *
 * LÓGICA:
 * Passo 1: Desenfileire os k primeiros e empilhe → pilha inverte a ordem.
 * Passo 2: Desempilhe e enfileire → elementos k..1 entram na fila em ordem invertida.
 * Passo 3: Os n-k elementos restantes ainda estão na fila após os k. Mova-os
 *          para o final da fila para que fiquem depois dos k revertidos.
 *          (na prática, já estão na fila; apenas precisamos que os k entrem antes deles)
 *
 * Exemplo: Fila [1,2,3,4,5], k=3 → [3,2,1,4,5]
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ExercicioLista10 {

    /* usando as classes padrão Java para demonstrar a lógica
       (em prova, usa as estruturas flexíveis do projeto) */
    public static void reverterKPrimeiros(Queue<Integer> fila, int k) {
        if (k <= 0 || fila.isEmpty()) return;

        Stack<Integer> pilha = new Stack<>();

        /* Passo 1: empilha os k primeiros */
        for (int i = 0; i < k; i++) {
            pilha.push(fila.poll());
        }

        /* Passo 2: desempilha de volta para a fila (agora invertidos) */
        while (!pilha.isEmpty()) {
            fila.add(pilha.pop());
        }

        /* Passo 3: move os n-k elementos do início para o fim
           (eles foram adicionados antes dos k revertidos; precisamos colocá-los depois) */
        int tamanhoRestante = fila.size() - k;
        for (int i = 0; i < tamanhoRestante; i++) {
            fila.add(fila.poll());
        }
    }

    public static void main(String[] args) {
        Queue<Integer> fila = new LinkedList<>();
        for (int i = 1; i <= 5; i++) fila.add(i);

        System.out.println("Antes:  " + fila);

        reverterKPrimeiros(fila, 3);

        System.out.println("Após k=3: " + fila);
        /* esperado: [3, 2, 1, 4, 5] */
    }
}
