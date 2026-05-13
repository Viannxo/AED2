/**
 * EXERCÍCIO 12 - Gerar números binários de 1 a N usando fila
 * Complexidade: Theta(n) — n iterações, cada uma com O(1) operações de fila.
 *
 * LÓGICA (BFS para binários):
 * - Enfileire "1".
 * - Para cada iteração de 1 a n:
 *     - Retire x da fila → adicione ao resultado.
 *     - Enfileire x+"0" e x+"1".
 *
 * Por que funciona?
 *   A fila naturalmente produz os binários em ordem crescente:
 *   nível 1: "1"
 *   nível 2: "10", "11"
 *   nível 3: "100", "101", "110", "111" ...
 *
 * Exemplo: n=5 → ["1","10","11","100","101"]
 */
class CelulaFila12 {
    String elemento;
    CelulaFila12 prox;
    public CelulaFila12(String s) { elemento = s; }
}

class Fila12 {
    CelulaFila12 primeiro, ultimo;

    public Fila12() {
        primeiro = new CelulaFila12(null); /* cabeca */
        ultimo = primeiro;
    }

    public void enfileirar(String s) {
        ultimo.prox = new CelulaFila12(s);
        ultimo = ultimo.prox;
    }

    public String desenfileirar() {
        if (primeiro == ultimo) throw new RuntimeException("Fila vazia");
        primeiro = primeiro.prox;
        return primeiro.elemento;
    }

    public boolean estaVazia() { return primeiro == ultimo; }
}

public class ExercicioLista12 {

    /* ===== IMPLEMENTAÇÃO ===== */
    public static String[] gerarBinarios(int n) {
        String[] resultado = new String[n];
        Fila12 fila = new Fila12();

        fila.enfileirar("1");

        for (int i = 0; i < n; i++) {
            String x = fila.desenfileirar();
            resultado[i] = x;

            /* gera os filhos: x0 e x1 */
            fila.enfileirar(x + "0");
            fila.enfileirar(x + "1");
        }

        return resultado;
    }

    public static void main(String[] args) {
        int n = 10;
        String[] binarios = gerarBinarios(n);

        System.out.println("Binários de 1 a " + n + ":");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d -> %s%n", i + 1, binarios[i]);
        }
        /* esperado: 1,10,11,100,101,110,111,1000,1001,1010 */
    }
}
