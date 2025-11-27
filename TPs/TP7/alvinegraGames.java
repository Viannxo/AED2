import java.io.*;
import java.util.*;

public class alvinegraGames {

//games
        public static class Game implements Comparable<Game> {
        private int id;
        private String name;

        public Game() {}

        public int getId() { return id; }
        public String getName() { return name; }

        @Override
        public int compareTo(Game outro) {
            return this.name.compareTo(outro.name);
        }

        public void ler(String linha) {
            String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if (campos.length > 1) {
                try {
                    this.id = Integer.parseInt(campos[0]);
                } catch (NumberFormatException e) {
                    this.id = -1;
                }
                this.name = campos[1];
            } else {
                this.id = -1;
                this.name = "";
            }
        }
    }

        //alvinegra    
        public static class NoAVL {
        public Game elemento;
        public NoAVL esq, dir;
        public int altura;

        public NoAVL(Game elemento) {
            this.elemento = elemento;
            this.esq = this.dir = null;
            this.altura = 1;
        }
    }
    public static class ArvoreAVL {
        private NoAVL raiz;
        public int comparacoes = 0;

        private int altura(NoAVL no) {
            return no == null ? 0 : no.altura;
        }

        private int fatorBalanceamento(NoAVL no) {
            return no == null ? 0 : altura(no.esq) - altura(no.dir);
        }

        private void atualizaAltura(NoAVL no) {
            if (no != null) {
                no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
            }
        }

        private NoAVL rotacaoDireita(NoAVL y) {
            NoAVL x = y.esq;
            NoAVL T2 = x.dir;

            // Rotação
            x.dir = y;
            y.esq = T2;

            // Atualiza alturas
            atualizaAltura(y);
            atualizaAltura(x);

            return x;
        }

        private NoAVL rotacaoEsquerda(NoAVL x) {
            NoAVL y = x.dir;
            NoAVL T2 = y.esq;

            // Rotação
            y.esq = x;
            x.dir = T2;

            // Atualiza alturas
            atualizaAltura(x);
            atualizaAltura(y);

            return y;
        }

        public void inserir(Game g) {
            raiz = inserirRec(raiz, g);
        }

        private NoAVL inserirRec(NoAVL no, Game g) {
            if (no == null) {
                return new NoAVL(g);
            }

            int cmp = g.getName().compareTo(no.elemento.getName());
            if (cmp < 0) {
                no.esq = inserirRec(no.esq, g);
            } else if (cmp > 0) {
                no.dir = inserirRec(no.dir, g);
            } else {
                // duplicado: ignorar (não inserimos)
                return no;
            }

            // atualiza altura
            atualizaAltura(no);

            // balanceamento
            int balance = fatorBalanceamento(no);

            // LL
            if (balance > 1 && g.getName().compareTo(no.esq.elemento.getName()) < 0)
                return rotacaoDireita(no);

            // RR
            if (balance < -1 && g.getName().compareTo(no.dir.elemento.getName()) > 0)
                return rotacaoEsquerda(no);

            // LR
            if (balance > 1 && g.getName().compareTo(no.esq.elemento.getName()) > 0) {
                no.esq = rotacaoEsquerda(no.esq);
                return rotacaoDireita(no);
            }

            // RL
            if (balance < -1 && g.getName().compareTo(no.dir.elemento.getName()) < 0) {
                no.dir = rotacaoDireita(no.dir);
                return rotacaoEsquerda(no);
            }

            return no;
        }

        // Pesquisa com impressão do caminho
        public void pesquisar(String nome) {
            // Formato desejado: "Nome do Jogo: =>raiz  esq dir ... SIM/NAO"
            System.out.print(nome + ": =>raiz  ");
            boolean achou = pesquisarRec(raiz, nome);
            if (achou) System.out.println("SIM");
            else System.out.println("NAO");
        }

        private boolean pesquisarRec(NoAVL no, String nome) {
            if (no == null) return false;

            comparacoes++;
            int cmp = nome.compareTo(no.elemento.getName());
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                System.out.print("esq ");
                return pesquisarRec(no.esq, nome);
            } else {
                System.out.print("dir ");
                return pesquisarRec(no.dir, nome);
            }
        }
    }

// main    
        public static void main(String[] args) {
        float inicio = System.currentTimeMillis();

        HashMap<Integer, Game> tabelaGames = new HashMap<>();
        String pathArquivo = "/tmp/games.csv";

        // Ler CSV
        try (BufferedReader br = new BufferedReader(new FileReader(pathArquivo))) {
            String linha = br.readLine(); // descarta cabeçalho
            while ((linha = br.readLine()) != null) {
                try {
                    Game g = new Game();
                    g.ler(linha);
                    if (g.getId() != -1 && g.getName() != null) {
                        tabelaGames.put(g.getId(), g);
                    }
                } catch (Exception e) {
                    // ignora linhas inválidas
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        ArvoreAVL arv = new ArvoreAVL();

        // IDs para inserir
        while (sc.hasNext()) {
            String entrada = sc.nextLine().trim();
            if (entrada.equals("FIM")) break;

            try {
                int id = Integer.parseInt(entrada);
                Game g = tabelaGames.get(id);
                if (g != null) arv.inserir(g);
            } catch (Exception e) {
                // ignora entradas inválidas
            }
        }

        // Nomes para buscar
        while (sc.hasNext()) {
            String nome = sc.nextLine().trim();
            if (nome.equals("FIM")) break;
            arv.pesquisar(nome);
        }

        sc.close();

        long fim = System.currentTimeMillis();

        // Criar log
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("866800_avl.txt"))) {
            bw.write("866800\t" + (fim - inicio) + "ms\t" + arv.comparacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
