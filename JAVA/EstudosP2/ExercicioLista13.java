/**
 * EXERCÍCIO 13 - Fila circular com array e campo 'tamanho' para diferenciar cheio/vazio
 * Complexidade de cada operação: Theta(1).
 *
 * LÓGICA:
 * - Array circular: fim = (fim + 1) % capacidade.
 * - 'tamanho' conta os elementos presentes.
 * - estaCheia(): tamanho == capacidade.
 * - estaVazia(): tamanho == 0.
 * - Evitamos o problema clássico de "como distinguir cheio de vazio"
 *   (que ocorre quando usamos apenas inicio e fim) ao manter 'tamanho'.
 *
 * Exemplo circular: capacidade=4, insere 1,2,3,4 → cheio.
 *   Remove 1 → libera espaço. Insere 5 → inicio avança, circula.
 */
public class ExercicioLista13 {

    static class FilaCircular {
        int[] dados;
        int inicio, fim, tamanho, capacidade;

        public FilaCircular(int cap) {
            dados      = new int[cap];
            capacidade = cap;
            inicio = fim = tamanho = 0;
        }

        /* ===== ENFILEIRAR ===== */
        public void enfileirar(int x) {
            if (estaCheia()) throw new RuntimeException("Fila cheia");
            dados[fim] = x;
            fim = (fim + 1) % capacidade; /* circula */
            tamanho++;
        }

        /* ===== DESENFILEIRAR ===== */
        public int desenfileirar() {
            if (estaVazia()) throw new RuntimeException("Fila vazia");
            int resp = dados[inicio];
            inicio = (inicio + 1) % capacidade; /* circula */
            tamanho--;
            return resp;
        }

        /* ===== CHEIO / VAZIO ===== */
        public boolean estaCheia() { return tamanho == capacidade; }
        public boolean estaVazia() { return tamanho == 0; }

        public void mostrar() {
            System.out.print("Fila [tamanho=" + tamanho + "]: [ ");
            int idx = inicio;
            for (int i = 0; i < tamanho; i++) {
                System.out.print(dados[idx] + " ");
                idx = (idx + 1) % capacidade;
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {
        FilaCircular f = new FilaCircular(4);

        f.enfileirar(1); f.enfileirar(2); f.enfileirar(3); f.enfileirar(4);
        f.mostrar(); /* [ 1 2 3 4 ] cheio */

        System.out.println("Cheia? " + f.estaCheia()); /* true */

        System.out.println("Removido: " + f.desenfileirar()); /* 1 */
        f.enfileirar(5); /* cabe agora */
        f.mostrar(); /* [ 2 3 4 5 ] — circulou! */

        System.out.println("Removido: " + f.desenfileirar()); /* 2 */
        System.out.println("Removido: " + f.desenfileirar()); /* 3 */
        System.out.println("Removido: " + f.desenfileirar()); /* 4 */
        System.out.println("Removido: " + f.desenfileirar()); /* 5 */
        System.out.println("Vazia? " + f.estaVazia()); /* true */
    }
}
