import java.io.*;
import java.util.*;

// --- Classes Auxiliares de Formatação ---

class Data {
    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) { 
        this.ano = ano;
        this.mes = mes;
        this.dia = dia; 
    }

    public int getAno() { return this.ano; }  
    public int getMes() { return this.mes; }  
    public int getDia() { return this.dia; }  

    public static Data parseData(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter("-");
        int ano = sc.nextInt();
        int mes = sc.nextInt();
        int dia = sc.nextInt();
        sc.close();
        return new Data(ano, mes, dia);
    }

    public String formatar() { 
        return String.format("%02d/%02d/%04d", dia, mes, ano); 
    }
}

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) { 
        this.hora = hora; 
        this.minuto = minuto; 
    }

    public static Hora parseHora(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(":");
        int h = sc.nextInt();
        int m = sc.nextInt();
        sc.close();
        return new Hora(h, m);
    }

    public String formatar() { 
        return String.format("%02d:%02d", hora, minuto); 
    }
}

class Price {
    private int faixa;
    public Price(int faixa) { 
        this.faixa = faixa; 
    }

    public String formatar() {
        String resp = "";
        for (int i = 0; i < faixa; i++) {
            resp = resp + "$";
        }
        return resp;
    }
}

class TiposCozinha {
    private String[] types;

    public TiposCozinha(String[] types) { 
        this.types = types; 
    }

    public static TiposCozinha create(String tipos) {
        Scanner sc = new Scanner(tipos);
        sc.useDelimiter(";");
        int count = 0;
        for (int i = 0; i < tipos.length(); i++) {
            if (tipos.charAt(i) == ';') {
                count++;    
            }
        }
        
        String[] tps = {};
        if (sc.hasNext()) {
            tps = new String[count + 1];
            for (int i = 0; i < count + 1; i++) {
                tps[i] = sc.next();
            }
        }
        sc.close();
        return new TiposCozinha(tps);
    }

    public String formatar() {
        String res = "[";
        for (int i = 0; i < types.length; i++) {
            res = res + types[i];
            if (i < types.length - 1) {
                res = res + ",";
            }
        }
        res = res + "]";
        return res;
    }
}

// --- Classe Restaurante ---

class Restaurante {
    private int id;
    private String nome, city;
    private int capacidade;
    private double avaliacao;
    private TiposCozinha tiposCozinha;
    private int faixaPreco;
    private Data dataAbertura;
    private Hora abertura, fechamento;
    private boolean aberto;

    public Restaurante(int id, String nome, String city, int capacidade, double avaliacao,
                       TiposCozinha tiposCozinha, int faixaPreco, Data dataAbertura,
                       Hora abertura, Hora fechamento, boolean aberto) {
        this.id = id;
        this.nome = nome;
        this.city = city;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixaPreco = faixaPreco;
        this.dataAbertura = dataAbertura;
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.aberto = aberto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String nome = sc.next();
        String city = sc.next();
        int capacidade = sc.nextInt();
        double avalia = Double.parseDouble(sc.next());
        String cozinhas = sc.next(); 
        TiposCozinha tpc = TiposCozinha.create(cozinhas); 

        int preco = sc.next().length();
        String horas = sc.next();

        Scanner scH = new Scanner(horas);
        scH.useDelimiter("-");

        Hora hAbrir = Hora.parseHora(scH.next());
        Hora hFechar = Hora.parseHora(scH.next());
        Data dAbrir = Data.parseData(sc.next());
        boolean func = sc.nextBoolean();
        
        scH.close();
        sc.close();
        return new Restaurante(id, nome, city, capacidade, avalia, tpc, preco, dAbrir, hAbrir, hFechar, func);
    }

    public String formatar() {
        Price preco = new Price(faixaPreco);
        TiposCozinha tcz = tiposCozinha;
        return String.format(Locale.US, "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
            id, nome, city, capacidade, avaliacao,
            tcz.formatar(), preco.formatar(), 
            abertura.formatar(), fechamento.formatar(),
            dataAbertura.formatar(), aberto);
    }
}

// --- Coleção e Leitura de CSV ---

class ColecaoRestaurantes {
    private Restaurante[] rest;
    private int n;

    public ColecaoRestaurantes() {
        rest = new Restaurante[1000];
        n = 0;
    }

    public void lerCsv(String path) {
        try {
            Scanner fsc = new Scanner(new File(path));
            if (fsc.hasNextLine()) fsc.nextLine(); // Pula cabeçalho
            while (fsc.hasNextLine() && n < rest.length) {
                rest[n++] = Restaurante.parseRestaurante(fsc.nextLine());
            }
            fsc.close();
        } catch (FileNotFoundException e) {
            // Tratamento silencioso
        }
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < n; i++) {
            if (rest[i].getId() == id) return rest[i];
        }
        return null;
    }
}

// --- Implementação do Nó e da Árvore Binária ---

class No {
    public Restaurante elemento;
    public No esq, dir;

    public No(Restaurante elemento) {
        this.elemento = elemento;
        this.esq = null;
        this.dir = null;
    }
}

class ArvoreBinaria {
    private No raiz;

    public ArvoreBinaria() {
        raiz = null;
    }

    public void inserir(Restaurante r) {
        raiz = inserir(raiz, r);
    }

    private No inserir(No i, Restaurante r) {
        if (i == null) {
            return new No(r);
        }
        
        int comp = r.getNome().compareTo(i.elemento.getNome());
        if (comp < 0) {
            i.esq = inserir(i.esq, r);
        } else if (comp > 0) {
            i.dir = inserir(i.dir, r);
        }
        return i;
    }

    public boolean pesquisar(String nome, int[] comp) {
        System.out.print("raiz");
        return pesquisar(raiz, nome, comp);
    }

    private boolean pesquisar(No i, String nome, int[] comp) {
        if (i == null) {
            System.out.println(" NAO");
            return false;
        }
        
        comp[0]++; 
        int c = nome.compareTo(i.elemento.getNome());
        
        if (c == 0) {
            System.out.println(" SIM");
            return true;
        } else if (c < 0) {
            System.out.print(" esq");
            return pesquisar(i.esq, nome, comp);
        } else {
            System.out.print(" dir");
            return pesquisar(i.dir, nome, comp);
        }
    }

    public void caminharEmOrdem() {
        caminharEmOrdem(raiz);
    }

    private void caminharEmOrdem(No i) {
        if (i != null) {
            caminharEmOrdem(i.esq);
            System.out.println(i.elemento.formatar());
            caminharEmOrdem(i.dir);
        }
    }
}

// --- Classe Principal / Driver Executável ---

public class ArvoreBi{
    
    public static void criarLog(int comparacoes, double tempo_ms) {
        try {
            FileWriter fw = new FileWriter("matricula_arvore_binaria.txt");
            PrintWriter pw = new PrintWriter(fw);
            pw.printf(Locale.US, "800000\t%d\t%.4f\n", comparacoes, tempo_ms);
            pw.close();
        } catch (IOException e) {
            // Ignora silenciosamente
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ColecaoRestaurantes colecao = new ColecaoRestaurantes();
        colecao.lerCsv("/tmp/restaurantes.csv");

        ArvoreBinaria arvore = new ArvoreBinaria();

        // 1ª Parte: Leitura e inserção por IDs até encontrar a flag de parada
        while (in.hasNext()) {
            String entrada = in.next();
            if (entrada.compareTo("FIM") == 0 || entrada.compareTo("-1") == 0) {
                break;
            }
            int idBusca = Integer.parseInt(entrada);
            Restaurante r = colecao.buscarPorId(idBusca);
            if (r != null) {
                arvore.inserir(r);
            }
        }

        if (in.hasNextLine()) {
            in.nextLine();
        }

        int[] totalComp = {0};
        long startTime = System.nanoTime();

        // 2ª Parte: Pesquisa por Nomes completos usando compareTo
        while (in.hasNextLine()) {
            String nomeBusca = in.nextLine().trim();
            if (nomeBusca.compareTo("FIM") == 0 || nomeBusca.isEmpty()) {
                break;
            }
            arvore.pesquisar(nomeBusca, totalComp);
        }

        long endTime = System.nanoTime();
        double tempo_ms = (endTime - startTime) / 1e6;

        // 3ª Parte: Impressão dos elementos ordenados (Caminhamento em Ordem)
        arvore.caminharEmOrdem();

        criarLog(totalComp[0], tempo_ms);
        in.close();
    }
}