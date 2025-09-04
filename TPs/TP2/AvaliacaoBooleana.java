/*
 * Crie um método iterativo que recebe uma string contendo uma
 *  expressão booleana e o valor de suas entradas e retorna um 
 * booleano indicando se a expressão é verdadeira ou falsa.

Cada string de entrada é composta por um número inteiro n indicando
o número de entradas da expressão booleana corrente. Em seguida, a
string contém n valores binários (um para cada entrada) e a expressão
booleana.

Na saída padrão, para cada linha de entrada, escreva uma linha de saída
 com SIM / NÃO indicando se a expressão corrente é verdadeira ou falsa.
 */

import java.util.Scanner;

public class AvaliacaoBooleana {

    // Substitui variáveis A-Z por 0 ou 1
    public static void substituirVariaveis(char[] expr, int len, int[] valores) {
        for (int i = 0; i < len; i++) {
            if (expr[i] >= 'A' && expr[i] <= 'Z') {
                expr[i] = (char) (valores[expr[i] - 'A'] + '0');
            }
        }
    }

    // Avalia expressão booleana
    public static int avaliarExpressao(char[] expr, int start, int end) {
        while (start < end && expr[start] == ' ') start++;
        while (end > start && expr[end - 1] == ' ') end--;

        if (start == end - 1) { // 0 ou 1
            return expr[start] - '0';
        }

        // Detecta NOT
        if (expr[start] == 'n' && expr[start + 1] == 'o' && expr[start + 2] == 't' && expr[start + 3] == '(') {
            return 1 - avaliarExpressao(expr, start + 4, end - 1);
        }

        // Detecta AND
        if (expr[start] == 'a' && expr[start + 1] == 'n' && expr[start + 2] == 'd' && expr[start + 3] == '(') {
            return avaliarAndOr(expr, start + 4, end - 1, true);
        }

        // Detecta OR
        if (expr[start] == 'o' && expr[start + 1] == 'r' && expr[start + 2] == '(') {
            return avaliarAndOr(expr, start + 3, end - 1, false);
        }

        return 0; // caso inesperado
    }

    // Avalia AND ou OR com múltiplos argumentos
    public static int avaliarAndOr(char[] expr, int start, int end, boolean isAnd) {
        int resultado = isAnd ? 1 : 0;
        int nivel = 0, argStart = start;

        for (int i = start; i < end; i++) {
            if (expr[i] == '(') nivel++;
            else if (expr[i] == ')') nivel--;
            else if (expr[i] == ',' && nivel == 0) {
                int val = avaliarExpressao(expr, argStart, i);
                resultado = isAnd ? (resultado & val) : (resultado | val);
                argStart = i + 1;
            }
        }

        // último argumento
        int val = avaliarExpressao(expr, argStart, end);
        resultado = isAnd ? (resultado & val) : (resultado | val);

        return resultado;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String linha = sc.nextLine();
            if (linha.trim().length() == 0) continue; // ignora linhas vazias
            char[] chars = linha.toCharArray();
            int len = chars.length;

            // Lê n
            int i = 0;
            int n = 0;
            while (i < len && chars[i] != ' ') {
                n = n * 10 + (chars[i] - '0');
                i++;
            }

            if (n == 0) break; // condição de parada
            while (i < len && chars[i] == ' ') i++; // pular espaços extras

            // Lê os n valores
            int[] valores = new int[n];
            for (int v = 0; v < n; v++) {
                int val = 0;
                while (i < len && chars[i] != ' ') {
                    val = val * 10 + (chars[i] - '0');
                    i++;
                }
                valores[v] = val;
                while (i < len && chars[i] == ' ') i++; // pular espaços extras
            }

            // Expressão
            int exprLen = len - i;
            if (exprLen > 0) {
                char[] exprChars = new char[exprLen];
                System.arraycopy(chars, i, exprChars, 0, exprLen);

                substituirVariaveis(exprChars, exprLen, valores);
                int resultado = avaliarExpressao(exprChars, 0, exprLen);
                System.out.println(resultado);
            } else {
                System.out.println(0); // caso sem expressão
            }
        }

        sc.close();
    }
}

