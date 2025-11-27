import java.io.*;
import java.util.*;

public class arvBinaria {

    // games

    public static class Game implements Comparable<Game> {
        private int id;
        private String name;

        public Game() {}

        public int getId() { return id; }
        public String getName() { return name; }
        
        // Comparador para a Árvore
        public int compareTo(Game outro) {
            return this.name.compareTo(outro.name);
        }

        public void ler(String linha) {
            //separar por vírgula
            String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            this.id = Integer.parseInt(campos[0]);
            this.name = campos[1];
        }
    }

    // arvore binaria
    public static class No {
        public Game elemento;
        public No esq, dir;

        public No(Game elemento) {
            this.elemento = elemento;
            this.esq = this.dir = null;
        }
    }

    public static class ArvoreBinaria {
        private No raiz;
        public int comparacoes = 0;

        public ArvoreBinaria() {
            raiz = null;
        }

        // Inserir
        public void inserir(Game g) throws Exception {
            raiz = inserir(g, raiz);
        }

        private No inserir(Game g, No i) throws Exception {
            if (i == null) {
                return new No(g);
            } else if (g.getName().compareTo(i.elemento.getName()) < 0) {
                i.esq = inserir(g, i.esq);
            } else if (g.getName().compareTo(i.elemento.getName()) > 0) {
                i.dir = inserir(g, i.dir);
            } else {
                throw new Exception("Erro ao inserir!"); 
            }
            return i;
        }

        public void pesquisar(String nome) {
            System.out.print(nome + ": =>raiz ");
            boolean resp = pesquisar(nome, raiz);
            
            if (resp) {
                System.out.println(" SIM");
            } else {
                System.out.println(" NAO");
            }
        }

        private boolean pesquisar(String nome, No i) {
            if (i == null) {
                return false;
            }
            
            int cmp = nome.compareTo(i.elemento.getName());
            comparacoes++;
            
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                System.out.print(" esq");
                return pesquisar(nome, i.esq);
            } else {
                System.out.print(" dir");
                return pesquisar(nome, i.dir);
            }
        }
    }

    // main

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        
        //
        HashMap<Integer, Game> tabelaGames = new HashMap<>();
        String pathArquivo = "/tmp/games.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(pathArquivo))) {
            String linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                try {
                    Game g = new Game();
                    g.ler(linha);
                    tabelaGames.put(g.getId(), g);
                } catch (Exception e) {
                    // Pula linhas quebradas
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        ArvoreBinaria arvore = new ArvoreBinaria();

        // 2. Leitura de IDs e Inserção
        while (sc.hasNext()) {
            String entrada = sc.nextLine().trim();
            if (entrada.equals("FIM")) break;

            try {
                int id = Integer.parseInt(entrada);
                Game g = tabelaGames.get(id);
                if (g != null) {
                    arvore.inserir(g);
                }
            } catch (Exception e) {
                // Ignora erro de inserção (duplicado) ou parse
            }
        }

        //Leitura de Nomes
        while (sc.hasNext()) {
            String nome = sc.nextLine().trim();
            if (nome.equals("FIM")) break;
            
            arvore.pesquisar(nome);
        }
        
        sc.close();
        long fim = System.currentTimeMillis();

        // Arquivo de Log
        try (BufferedWriter log = new BufferedWriter(new FileWriter("866800_arvoreBinaria.txt"))) {
            log.write("866800" + "\t" + (fim - inicio) + "ms" + "\t" + arvore.comparacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}