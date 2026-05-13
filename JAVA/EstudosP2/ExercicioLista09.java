/**
 * EXERCÍCIO 9 - Avaliar expressão pós-fixa (notação polonesa reversa) com pilha
 * Complexidade: Theta(n) — percorre cada token da expressão uma vez.
 *
 * LÓGICA:
 * - Tokenize a expressão por espaços.
 * - Para cada token:
 *     - Se for número → empilha.
 *     - Se for operador (+,-,*,/) → desempilha dois operandos (b depois a),
 *       calcula a OP b, empilha o resultado.
 * - Ao final, o topo é o resultado.
 *
 * CUIDADO: a ordem importa!
 *   "3 4 -" significa 3 - 4 = -1.
 *   Primeiro desempilhado = b (direita), segundo = a (esquerda).
 *
 * Exemplos:
 *   "3 4 + 2 *"   → (3+4)*2 = 14
 *   "5 1 2 + 4 * + 3 -" → 14
 */
class CelulaPilha9 {
    int elemento;
    CelulaPilha9 prox;
    public CelulaPilha9(int e) { elemento = e; }
}

class Pilha9 {
    CelulaPilha9 topo;

    public void empilhar(int x) {
        CelulaPilha9 nova = new CelulaPilha9(x);
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

public class ExercicioLista09 {

    /* ===== IMPLEMENTAÇÃO ===== */
    public static int avaliarPosFixa(String expr) {
        Pilha9 pilha = new Pilha9();
        String[] tokens = expr.split(" ");

        for (String token : tokens) {
            if (token.equals("+") || token.equals("-")
                    || token.equals("*") || token.equals("/")) {

                int b = pilha.desempilhar(); /* segundo operando  */
                int a = pilha.desempilhar(); /* primeiro operando */
                int resultado;

                switch (token) {
                    case "+": resultado = a + b; break;
                    case "-": resultado = a - b; break;
                    case "*": resultado = a * b; break;
                    case "/": resultado = a / b; break;
                    default:  throw new RuntimeException("Operador desconhecido: " + token);
                }

                pilha.empilhar(resultado);
            } else {
                /* é um número */
                pilha.empilhar(Integer.parseInt(token));
            }
        }

        return pilha.desempilhar(); /* resultado final */
    }

    public static void main(String[] args) {
        System.out.println("3 4 + 2 *   = " + avaliarPosFixa("3 4 + 2 *"));   /* 14 */
        System.out.println("5 1 2 + 4 * + 3 - = " + avaliarPosFixa("5 1 2 + 4 * + 3 -")); /* 14 */
        System.out.println("8 3 -       = " + avaliarPosFixa("8 3 -"));         /*  5 */
    }
}
