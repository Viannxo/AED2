import java.io.*;
import java.util.*;
import java.text.*;

// ========================================================
// CLASSE GAME (Dados e Parsing CSV)
// ========================================================
class Game implements Comparable<Game> {
    private int id;
    private String name;
    private String releaseDate;
    private int estimatedOwners;
    private float price;
    private String[] supportedLanguages;
    private int metacriticScore;
    private float userScore;
    private int achievements;
    private String[] publishers;
    private String[] developers;
    private String[] categories;
    private String[] genres;
    private String[] tags;

    public Game() {}

    public int getId() { return id; }
    public String getName() { return name; }
    public int getEstimatedOwners() { return estimatedOwners; }

    public int compareTo(Game outro) {
        return this.name.compareTo(outro.name);
    }

    public void ler(String linha) {

        String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        try { this.id = Integer.parseInt(campos[0]); } 
        catch (Exception e) { this.id = -1; }

        this.name = campos[1];
        this.releaseDate = normalizeReleaseDate(campos[2]);

        // ==========================================================
        // CORREÇÃO FUNDAMENTAL: PEGAR SOMENTE O PRIMEIRO NÚMERO
        // ==========================================================
        try {
            String raw = campos[3].replaceAll("\"", "");
            raw = raw.split("[^0-9]+")[0];
            this.estimatedOwners = Integer.parseInt(raw);
        } catch (Exception e) {
            this.estimatedOwners = 0;
        }

        try { this.price = Float.parseFloat(campos[4].replace(",", ".")); } 
        catch (Exception e) { this.price = 0.0f; }

        this.supportedLanguages = parseArrayField(campos[5]);

        try { this.metacriticScore = Integer.parseInt(campos[6]); } 
        catch (Exception e) { this.metacriticScore = -1; }

        try { this.userScore = Float.parseFloat(campos[7].replace(",", ".")); } 
        catch (Exception e) { this.userScore = -1.0f; }

        try { this.achievements = Integer.parseInt(campos[8]); } 
        catch (Exception e) { this.achievements = 0; }

        this.publishers = parseArrayField(campos[9]);
        this.developers = parseArrayField(campos[10]);
        this.categories = parseArrayField(campos[11]);
        this.genres = parseArrayField(campos[12]);
        this.tags = parseArrayField(campos.length > 13 ? campos[13] : "");
    }

    private String normalizeReleaseDate(String s) {
        if (s == null || s.trim().isEmpty()) return "01/01/2000";
        s = s.trim().replace("\"", "");

        SimpleDateFormat fmtOriginal = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fmtMonthYear = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

        try {
            Date date = fmtOriginal.parse(s);
            return fmtOut.format(date);
        } catch (ParseException e) {
            try {
                Date date = fmtMonthYear.parse(s);
                return "01/" + new SimpleDateFormat("MM/yyyy").format(date);
            } catch (ParseException e2) {
                return "01/01/2000";
            }
        }
    }

    private String[] parseArrayField(String text) {
        if (text == null || text.isEmpty()) return new String[0];
        text = text.replaceAll("\\[|\\]|'|\"", "").trim();
        String[] parts = text.split(", ");
        return parts.length == 1 && parts[0].isEmpty() ? new String[0] : parts;
    }
}

// ========================================================
// NÓ DA PRIMEIRA ÁRVORE
// ========================================================
class No {
    public int elemento;
    public No esq, dir;
    public No2 outro;

    No(int elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
        this.outro = null;
    }
}

// ========================================================
// NÓ DA SEGUNDA ÁRVORE
// ========================================================
class No2 {
    public Game elemento;
    public No2 esq, dir;

    No2(Game elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
    }
}

// ========================================================
// ÁRVORE DE ÁRVORES
// ========================================================
public class ArvoreArvore {
    private No raiz;
    public int comparacoes = 0;

    public ArvoreArvore() {
        raiz = null;
        int[] fixos = {7,3,11,1,5,9,13,0,2,4,6,8,10,12,14};
        for (int x : fixos) {
            try { inserir(x); } catch (Exception e) {}
        }
    }

    // primeira árvore
    private No inserir(int x, No i) throws Exception {
        if (i == null) return new No(x);
        else if (x < i.elemento) i.esq = inserir(x, i.esq);
        else if (x > i.elemento) i.dir = inserir(x, i.dir);
        else throw new Exception("chave duplicada");
        return i;
    }

    public void inserir(int x) throws Exception {
        raiz = inserir(x, raiz);
    }

    // segunda árvore
    private No inserir(Game g, int chave1, No i) throws Exception {
        if (i == null) throw new Exception("bucket inexistente");
        if (chave1 < i.elemento) i.esq = inserir(g, chave1, i.esq);
        else if (chave1 > i.elemento) i.dir = inserir(g, chave1, i.dir);
        else i.outro = inserirSegundaArvore(g, i.outro);
        return i;
    }

    public void inserir(Game g) throws Exception {
        int bucket = g.getEstimatedOwners() % 15;
        raiz = inserir(g, bucket, raiz);
    }

    private No2 inserirSegundaArvore(Game g, No2 i) throws Exception {
        if (i == null) return new No2(g);

        int cmp = g.getName().compareTo(i.elemento.getName());
        if (cmp < 0) i.esq = inserirSegundaArvore(g, i.esq);
        else if (cmp > 0) i.dir = inserirSegundaArvore(g, i.dir);
        else throw new Exception("nome duplicado");
        return i;
    }

    // pesquisa
    public void pesquisar(String nome) {
        System.out.print("=> " + nome);
        if (!pesquisarPrimeiraArvore(raiz, nome, "")) {
            System.out.println(": =>raiz NAO");
        }
    }

    private boolean pesquisarPrimeiraArvore(No i, String nome, String path1) {
        if (i == null) return false;

        if (pesquisarPrimeiraArvore(i.esq, nome, extend(path1, "ESQ"))) return true;

        String[] res = pesquisarSegundaArvore(i.outro, nome);
        boolean found = Boolean.parseBoolean(res[0]);
        String path2 = res[1];

        if (found) {
            // formatação igual a saída esperada
            System.out.print(": =>raiz");
            if (!path1.isBlank()) System.out.print("  " + path1.trim());
            if (!path2.isBlank()) System.out.print(" " + path2.trim());
            System.out.println(" SIM");
            return true;
        }

        if (pesquisarPrimeiraArvore(i.dir, nome, extend(path1, "DIR"))) return true;

        return false;
    }

    private String extend(String path, String step) {
        if (path.isEmpty()) return step;
        return path + "  " + step;
    }

    private String[] pesquisarSegundaArvore(No2 i, String nome) {
        StringBuilder path = new StringBuilder();
        boolean ok = false;

        while (i != null) {
            int cmp = nome.compareTo(i.elemento.getName());
            comparacoes++;

            if (cmp == 0) { ok = true; break; }
            else if (cmp < 0) {
                path.append(path.length()==0 ? "esq" : " esq");
                i = i.esq;
            } else {
                path.append(path.length()==0 ? "dir" : " dir");
                i = i.dir;
            }
        }

        return new String[]{String.valueOf(ok), path.toString()};
    }

    // main
    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();

        HashMap<Integer, Game> tabela = new HashMap<>();
        String arquivo = "/tmp/games.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            br.readLine();
            String l;
            while ((l = br.readLine()) != null) {
                try {
                    Game g = new Game();
                    g.ler(l);
                    tabela.put(g.getId(), g);
                } catch (Exception e) {}
            }
        } catch (IOException e) {}

        Scanner sc = new Scanner(System.in);
        ArvoreArvore arv = new ArvoreArvore();

        while (true) {
            String s = sc.nextLine().trim();
            if (s.equals("FIM")) break;
            try {
                int id = Integer.parseInt(s);
                Game g = tabela.get(id);
                if (g != null)
                    try { arv.inserir(g); } catch (Exception e) {}
            } catch (Exception e) {}
        }

        while (true) {
            String nome = sc.nextLine().trim();
            if (nome.equals("FIM")) break;
            arv.pesquisar(nome);
        }

        long fim = System.currentTimeMillis();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("866800_arvoreBinaria.txt"))) {
            bw.write("866800\t" + (fim - inicio) + "ms\tComparacoes: " + arv.comparacoes);
        } catch (IOException e) {}
    }
}
