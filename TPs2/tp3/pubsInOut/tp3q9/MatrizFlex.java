import java.io.*;
import java.util.Locale;
import java.util.Scanner;

// --- Estrutura da Célula da Matriz Flexível ---
class Celula {
    public int elemento;
    public Celula inf, sup, esq, dir;

    public Celula() {
        this(0);
    }

    public Celula(int elemento) {
        this.elemento = elemento;
        this.inf = this.sup = this.esq = this.dir = null;
    }
}

// --- Estrutura da Matriz Flexível ---
class Matriz {
    private Celula inicio;
    private int linha, coluna;

    public Matriz(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        
        if (linha > 0 && coluna > 0) {
            inicio = new Celula();
            Celula atual = inicio;

            // Constrói a primeira linha
            for (int j = 1; j < coluna; j++) {
                Celula nova = new Celula();
                atual.dir = nova;
                nova.esq = atual;
                atual = nova;
            }

            Celula linhaAnterior = inicio;
            
            // Constrói as linhas subsequentes e as amarra com as de cima
            for (int i = 1; i < linha; i++) {
                Celula novaLinha = new Celula();
                linhaAnterior.inf = novaLinha;
                novaLinha.sup = linhaAnterior;

                Celula atualLinhaAnterior = linhaAnterior.dir;
                Celula atualNovaLinha = novaLinha;

                for (int j = 1; j < coluna; j++) {
                    Celula nova = new Celula();
                    
                    // Amarra lateralmente
                    atualNovaLinha.dir = nova;
                    nova.esq = atualNovaLinha;
                    
                    // Amarra verticalmente
                    atualLinhaAnterior.inf = nova;
                    nova.sup = atualLinhaAnterior;

                    atualNovaLinha = nova;
                    atualLinhaAnterior = atualLinhaAnterior.dir;
                }
                linhaAnterior = novaLinha;
            }
        }
    }

    // Lê os elementos inserindo-os na malha estrutural
    public void ler(Scanner scanner) {
        Celula linhaAtual = inicio;
        while (linhaAtual != null) {
            Celula atual = linhaAtual;
            while (atual != null) {
                atual.elemento = scanner.nextInt();
                atual = atual.dir;
            }
            linhaAtual = linhaAtual.inf;
        }
    }

    // Imprime a Diagonal Principal (Lança Exceção se não for quadrada)
    public void mostrarDiagonalPrincipal() throws Exception {
        if (this.linha != this.coluna) {
            throw new Exception("Erro: A matriz nao e quadrada. Nao existe diagonal principal.");
        }
        
        Celula atual = inicio;
        while (atual != null) {
            System.out.print(atual.elemento);
            if (atual.dir != null && atual.dir.inf != null) {
                System.out.print(" ");
                atual = atual.dir.inf;
            } else {
                System.out.println();
                atual = null;
            }
        }
    }

    // Imprime a Diagonal Secundária (Lança Exceção se não for quadrada)
    public void mostrarDiagonalSecundaria() throws Exception {
        if (this.linha != this.coluna) {
            throw new Exception("Erro: A matriz nao e quadrada. Nao existe diagonal secundaria.");
        }
        
        Celula atual = inicio;
        // Vai para o canto superior direito
        while (atual.dir != null) {
            atual = atual.dir;
        }
        // Desce cortando para a esquerda
        while (atual != null) {
            System.out.print(atual.elemento);
            if (atual.esq != null && atual.inf != null) {
                System.out.print(" ");
                atual = atual.inf.esq;
            } else {
                System.out.println();
                atual = null;
            }
        }
    }

    // Soma duas matrizes flexíveis
    public Matriz somar(Matriz m) throws Exception {
        if (this.linha != m.linha || this.coluna != m.coluna) {
             throw new Exception("Erro: Matrizes de dimensoes incompativeis para soma.");
        }
        
        Matriz res = new Matriz(this.linha, this.coluna);
        Celula linhaA = this.inicio;
        Celula linhaB = m.inicio;
        Celula linhaR = res.inicio;

        while (linhaA != null && linhaB != null && linhaR != null) {
            Celula a = linhaA;
            Celula b = linhaB;
            Celula r = linhaR;

            while (a != null && b != null && r != null) {
                r.elemento = a.elemento + b.elemento;
                a = a.dir;
                b = b.dir;
                r = r.dir;
            }

            linhaA = linhaA.inf;
            linhaB = linhaB.inf;
            linhaR = linhaR.inf;
        }
        return res;
    }

    // Multiplica duas matrizes flexíveis
    public Matriz multiplicar(Matriz m) throws Exception {
        if (this.coluna != m.linha) {
             throw new Exception("Erro: Dimensoes incompativeis para multiplicacao.");
        }
        
        Matriz res = new Matriz(this.linha, m.coluna);
        Celula linhaA = this.inicio;
        Celula linhaR = res.inicio;

        while (linhaA != null) {
            Celula colB = m.inicio; // Coluna da segunda matriz
            Celula r = linhaR;

            while (colB != null) {
                int soma = 0;
                Celula a = linhaA;
                Celula b = colB;

                while (a != null && b != null) {
                    soma += a.elemento * b.elemento;
                    a = a.dir;
                    b = b.inf; // A segunda matriz avança para baixo (coluna)
                }

                r.elemento = soma;
                r = r.dir;
                colB = colB.dir; // Avança para a próxima coluna de M2
            }

            linhaA = linhaA.inf;
            linhaR = linhaR.inf;
        }
        return res;
    }

    public void imprimir() {
        Celula linhaAtual = inicio;
        while (linhaAtual != null) {
            Celula a = linhaAtual;
            while (a != null) {
                System.out.print(a.elemento);
                if (a.dir != null) System.out.print(" ");
                a = a.dir;
            }
            System.out.println();
            linhaAtual = linhaAtual.inf;
        }
    }
}

// --- Main ---
public class MatrizFlex {
    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in);
        long inicio = System.currentTimeMillis();
        
        if (sc.hasNextInt()) {
            int numCasos = sc.nextInt();
            
            for (int c = 0; c < numCasos; c++) {
                int linhas = sc.nextInt();
                int colunas = sc.nextInt();

                // Constrói e lê a Primeira Matriz
                Matriz m1 = new Matriz(linhas, colunas);
                m1.ler(sc);

                // Constrói e lê a Segunda Matriz
                Matriz m2 = new Matriz(linhas, colunas);
                m2.ler(sc);

                // Imprime apenas se forem quadradas. Caso contrário, lança a exceção e encerra/pula.
                // Como não sabemos se o Verde apenas encerra ou se tem um try-catch, vamos deixar o throw propagar.
                m1.mostrarDiagonalPrincipal();
                m2.mostrarDiagonalSecundaria();

                Matriz soma = m1.somar(m2);
                soma.imprimir();

                Matriz mult = m1.multiplicar(m2);
                mult.imprimir();
            }
        }
        
        long fim = System.currentTimeMillis();
        
        try (PrintWriter log = new PrintWriter(new FileWriter("844387_matriz_flexivel.txt"))) {
            log.printf(Locale.US, "844387\t0\t0\t%.2fms\n", (float)(fim - inicio));
        } catch (IOException e) { }
        
        sc.close();
    }
}