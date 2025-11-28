import java.io.*;
import java.util.*;

//games
class Game {
    private int id;
    private String name;
    private int estimatedOwners;

    public Game() {}

    public int getId() { return id; }
    public String getName() { return name; }
    public int getEstimatedOwners() { return estimatedOwners; }

    public void ler(String linha) {
        String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        try { 
            this.id = Integer.parseInt(campos[0]); 
        } catch (Exception e) { this.id = -1; }

        try { 
            this.name = campos[1].replaceAll("^\"|\"$", ""); 
        } catch (Exception e) { this.name = ""; }

        try {
            String raw = campos[3].replaceAll("\"", "");
            String[] parts = raw.split("[^0-9]+");
            if(parts.length > 0 && !parts[0].isEmpty()){
                this.estimatedOwners = Integer.parseInt(parts[0]);
            } else {
                this.estimatedOwners = 0;
            }
        } catch (Exception e) {
            this.estimatedOwners = 0;
        }
    }
}

    // nó da primeira arvore (inteiro)
    class No {
    public int elemento;
    public No esq, dir;
    public No2 outro; // Aponta para a raiz da segunda árvore

    No(int elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
        this.outro = null;
    }
}

//no da segunda arvore (jogo)
class No2 {
    public Game elemento;
    public No2 esq, dir;

    No2(Game elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
    }
}

// arv de arv
public class ArvoreArvore {
    private No raiz;
    public int comparacoes = 0;
    private boolean found = false; // Flag global para controle de parada

    public ArvoreArvore() {
        raiz = null;
        // Inserção fixa para garantir a estrutura da primeira árvore
        int[] fixos = {7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14};
        for (int x : fixos) {
            try { raiz = inserirPrimeira(x, raiz); } catch (Exception e) {}
        }
    }

    // Insere na primeira árvore (apenas inteiros)
    private No inserirPrimeira(int x, No i) throws Exception {
        if (i == null) return new No(x);
        else if (x < i.elemento) i.esq = inserirPrimeira(x, i.esq);
        else if (x > i.elemento) i.dir = inserirPrimeira(x, i.dir);
        return i;
    }

    // Método público de inserção de Game
    public void inserir(Game g) throws Exception {
        int bucket = g.getEstimatedOwners() % 15;
        inserirNaEstrutura(g, bucket, raiz);
    }

    // Navega até o bucket correto na 1ª árvore e insere na 2ª
    private void inserirNaEstrutura(Game g, int chave1, No i) throws Exception {
        if (i == null) return; 

        if (chave1 < i.elemento) {
            inserirNaEstrutura(g, chave1, i.esq);
        } else if (chave1 > i.elemento) {
            inserirNaEstrutura(g, chave1, i.dir);
        } else {
            // Achou o nó, insere na árvore secundária
            i.outro = inserirSegunda(g, i.outro);
        }
    }

    // Insere na segunda árvore (BST simples por nome)
    private No2 inserirSegunda(Game g, No2 i) throws Exception {
        if (i == null) return new No2(g);
        int cmp = g.getName().compareTo(i.elemento.getName());
        if (cmp < 0) i.esq = inserirSegunda(g, i.esq);
        else if (cmp > 0) i.dir = inserirSegunda(g, i.dir);
        return i;
    }

// pesquisa
    public void pesquisar(String nome) {
        System.out.print("=> " + nome + " =>");
        found = false;
        
        caminhar(raiz, nome, "raiz");
        
        if (!found) {
            System.out.println(" NAO");
        }
    }

    // Percorre a 1ª árvore em Pré-Ordem
    private void caminhar(No no, String nome, String path) {
        if (found || no == null) return;

        if (no.outro != null) {
            // Imprime o caminho da 1ª árvore ATUAL
            System.out.print(" " + path);
            
            // Pesquisa na 2ª árvore
            String[] res = pesquisarSegunda(no.outro, nome);
            String pathSec = res[1];
            boolean achouAqui = Boolean.parseBoolean(res[0]);

            // Imprime o caminho percorrido dentro da 2ª árvore
            System.out.print(pathSec);

            if (achouAqui) {
                System.out.println(" SIM");
                found = true;
                return;
            }
        }

        // Continua a busca na esquerda e direita
        caminhar(no.esq, nome, path + "  ESQ");
        caminhar(no.dir, nome, path + "  DIR");
    }

    // Pesquisa na 2ª árvore e retorna { "true/false", " caminho" }
    private String[] pesquisarSegunda(No2 i, String nome) {
        StringBuilder sb = new StringBuilder();
        boolean resp = false;
        
        while (i != null) {
            comparacoes++; // Conta a comparação
            int cmp = nome.compareTo(i.elemento.getName());

            if (cmp == 0) {
                resp = true;
                break;
            } else if (cmp < 0) {
                sb.append(" esq");
                i = i.esq;
            } else {
                sb.append(" dir");
                i = i.dir;
            }
        }
        return new String[]{ String.valueOf(resp), sb.toString() };
    }

    // main
    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        ArvoreArvore arvore = new ArvoreArvore();
        
        //buscar de jogos por ID na hash
        HashMap<Integer, Game> mapa = new HashMap<>();
        
        // Leitura do arquivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader("/tmp/games.csv"))) {
            br.readLine(); // Pular cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                Game g = new Game();
                g.ler(linha);
                mapa.put(g.getId(), g);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        // 1. Inserção (lê IDs)
        while (sc.hasNext()) {
            String s = sc.nextLine().trim();
            if (s.equals("FIM")) break;
            try {
                int id = Integer.parseInt(s);
                Game g = mapa.get(id);
                if (g != null) arvore.inserir(g);
            } catch (Exception e) {}
        }

        // 2. Pesquisa (lê Nomes)
        while (sc.hasNext()) {
            String nome = sc.nextLine().trim();
            if (nome.equals("FIM")) break;
            arvore.pesquisar(nome);
        }
        sc.close();

        long fim = System.currentTimeMillis();

        // Arquivo de Log
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("866800_arvoreBinaria.txt"))) {
            bw.write("866800\t" + (fim - inicio) + "\t" + arvore.comparacoes);
        } catch (IOException e) {}
    }
}