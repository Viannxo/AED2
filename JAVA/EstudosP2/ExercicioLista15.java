/**
 * EXERCÍCIO 15 - Detectar ciclo em lista simples (Floyd / tartaruga e lebre)
 * Complexidade de tempo:  O(n) — lebre e tartaruga se encontram em no máximo n passos.
 * Complexidade de espaço: O(1) — apenas dois ponteiros extras.
 *
 * LÓGICA:
 * - 'lento'  avança 1 nó por iteração (tartaruga).
 * - 'rapido' avança 2 nós por iteração (lebre).
 * - Se 'rapido' ou 'rapido.prox' for null → lista finita → sem ciclo.
 * - Se 'lento == rapido' → ciclo detectado.
 *
 * Por que funciona?
 *   Se há ciclo, a lebre entra no ciclo e começa a "rodar".
 *   A tartaruga chega depois e entra no ciclo.
 *   Como a lebre é mais rápida, ela alcança a tartaruga dentro do ciclo.
 */
class Celula15 {
    int elemento;
    Celula15 prox;
    public Celula15(int e) { elemento = e; }
}

class Lista15 {
    Celula15 primeiro; /* sem célula-cabeça aqui para facilitar criação de ciclo */

    public Lista15() { primeiro = null; }

    /* ===== IMPLEMENTAÇÃO ===== */
    public boolean temCiclo() {
        Celula15 lento  = primeiro;
        Celula15 rapido = primeiro;

        while (rapido != null && rapido.prox != null) {
            lento  = lento.prox;         /* 1 passo */
            rapido = rapido.prox.prox;   /* 2 passos */

            if (lento == rapido) return true; /* ciclo encontrado */
        }

        return false; /* lista finita */
    }
}

public class ExercicioLista15 {
    public static void main(String[] args) {
        /* Teste 1: sem ciclo */
        Lista15 l1 = new Lista15();
        Celula15 a = new Celula15(1);
        Celula15 b = new Celula15(2);
        Celula15 c = new Celula15(3);
        a.prox = b; b.prox = c; /* c.prox = null */
        l1.primeiro = a;
        System.out.println("Sem ciclo: " + l1.temCiclo()); /* false */

        /* Teste 2: com ciclo (c -> b) */
        Lista15 l2 = new Lista15();
        Celula15 x = new Celula15(1);
        Celula15 y = new Celula15(2);
        Celula15 z = new Celula15(3);
        x.prox = y; y.prox = z; z.prox = y; /* ciclo: z aponta de volta para y */
        l2.primeiro = x;
        System.out.println("Com ciclo:  " + l2.temCiclo()); /* true */
    }
}
