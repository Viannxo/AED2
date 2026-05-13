/**
 * EXERCÍCIO 7 - Verificar se expressão com parênteses/colchetes/chaves está balanceada
 * Complexidade: Theta(n) — percorre cada caractere da string uma vez.
 *
 * LÓGICA:
 * - Percorra cada caractere da expressão.
 * - Se for abre-delimitador ( ( [ { ) → empilha.
 * - Se for fecha-delimitador ( ) ] } ):
 *     - Se pilha vazia → não balanceado (falta abre).
 *     - Se topo não é o par correto → não balanceado.
 *     - Senão → desempilha.
 * - Ao final, se a pilha estiver vazia → balanceado.
 *
 * Exemplo: "([]{})" → true | "([)]" → false | "(((" → false
 */
class CelulaPilha7 {
    char elemento;
    CelulaPilha7 prox;
    public CelulaPilha7(char c) { elemento = c; }
}

class Pilha7 {
    CelulaPilha7 topo;

    public void empilhar(char c) {
        CelulaPilha7 nova = new CelulaPilha7(c);
        nova.prox = topo;
        topo = nova;
    }

    public char desempilhar() {
        if (topo == null) throw new RuntimeException("Pilha vazia");
        char resp = topo.elemento;
        topo = topo.prox;
        return resp;
    }

    public boolean estaVazia() { return topo == null; }

    public char verTopo() { return topo.elemento; }
}

class Verificador7 {

    /* retorna o abre-delimitador correspondente ao fecha-delimitador dado */
    private static char parCorrespondente(char fecha) {
        if (fecha == ')') return '(';
        if (fecha == ']') return '[';
        if (fecha == '}') return '{';
        return 0;
    }

    private static boolean isFecha(char c) {
        return c == ')' || c == ']' || c == '}';
    }

    private static boolean isAbre(char c) {
        return c == '(' || c == '[' || c == '{';
    }

    /* ===== IMPLEMENTAÇÃO ===== */
    public boolean estaBalanceada(String expr) {
        Pilha7 pilha = new Pilha7();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (isAbre(c)) {
                pilha.empilhar(c);
            } else if (isFecha(c)) {
                if (pilha.estaVazia()) return false;          /* falta abre */
                if (pilha.verTopo() != parCorrespondente(c)) return false; /* par errado */
                pilha.desempilhar();
            }
            /* outros caracteres são ignorados */
        }

        return pilha.estaVazia(); /* sobrou abre sem fecha? */
    }
}

public class ExercicioLista07 {
    public static void main(String[] args) {
        Verificador7 v = new Verificador7();

        System.out.println("([]{})  : " + v.estaBalanceada("([]{})"));   /* true  */
        System.out.println("([)]    : " + v.estaBalanceada("([)]"));     /* false */
        System.out.println("{[()]}  : " + v.estaBalanceada("{[()]}"));   /* true  */
        System.out.println("(((     : " + v.estaBalanceada("((("));      /* false */
        System.out.println("        : " + v.estaBalanceada(""));         /* true  */
    }
}
