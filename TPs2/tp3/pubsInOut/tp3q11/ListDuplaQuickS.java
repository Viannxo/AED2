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
        StringBuilder resp = new StringBuilder();
        for (int i = 0; i < faixa; i++) 
            resp.append("$");
        return resp.toString();
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
        StringBuilder res = new StringBuilder("[");
        for (int i = 0; i < types.length; i++) {
            res.append(types[i]);
            if (i < types.length - 1) res.append(",");
        }
        return res.append("]").toString();
    }
}

// --- Classe Principal Restaurante ---

class Restaurante {
    private int id;
    private String nome, cidade;
    private int capacidade;
    private double avaliacao;
    private TiposCozinha tiposCozinha;
    private int faixaPreco;
    private Data dataAbertura;
    private Hora abertura, fechamento;
    private boolean aberto;

    public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao,
                       TiposCozinha tiposCozinha, int faixaPreco, Data dataAbertura,
                       Hora abertura, Hora fechamento, boolean aberto) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
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
    public double getAvaliacao() { return avaliacao; }

    // Critério de ordenação: Avaliação Crescente + Desempate por Nome Alfabético
    public int comparar(Restaurante outro) {
        if (this.avaliacao < outro.avaliacao) return -1;
        if (this.avaliacao > outro.avaliacao) return 1;
        return this.nome.compareTo(outro.nome);
    }

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String nome = sc.next();
        String cidade = sc.next();
        int capacidad = sc.nextInt();
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
        return new Restaurante(id, nome, cidade, capacidad, avalia, tpc, preco, dAbrir, hAbrir, hFechar, func);
    }

    public String formatar() {
        Price preco = new Price(faixaPreco);
        TiposCozinha tcz = tiposCozinha;
        return String.format(Locale.US, "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
            id, nome, cidade, capacidade, avaliacao,
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
            // Tratamento silencioso de exceção
        }
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < n; i++) {
            if (rest[i].getId() == id) return rest[i];
        }
        return null;
    }
}

// --- Implementação da Lista Dupla Flexível ---

class CelulaDupla {
    public Restaurante elemento;
    public CelulaDupla ant, prox;

    public CelulaDupla() {
        this(null);
    }

    public CelulaDupla(Restaurante elemento) {
        this.elemento = elemento;
        this.ant = null;
        this.prox = null;
    }
}

class ListaDuplaFlexivel {
    private CelulaDupla primeiro, ultimo;
    private int tamanho;

    public ListaDuplaFlexivel() {
        primeiro = new CelulaDupla(); // Sentinela
        ultimo = primeiro;
        tamanho = 0;
    }

    public void inserirFim(Restaurante r) {
        CelulaDupla nova = new CelulaDupla(r);
        ultimo.prox = nova;
        nova.ant = ultimo;
        ultimo = nova;
        tamanho++;
    }

    public void mostrar() {
        for (CelulaDupla i = primeiro.prox; i != null; i = i.prox) {
            System.out.println(i.elemento.formatar());
        }
    }

    // Partição do Quicksort adaptada para Ponteiros de Célula Dupla
    private CelulaDupla partition(CelulaDupla esq, CelulaDupla dir, int[] comp, int[] mov) {
        Restaurante pivot = dir.elemento;
        CelulaDupla i = esq.ant;

        for (CelulaDupla j = esq; j != dir; j = j.prox) {
            comp[0]++;
            if (j.elemento.comparar(pivot) < 0) {
                i = (i == esq.ant) ? esq : i.prox;
                Restaurante temp = i.elemento;
                i.elemento = j.elemento;
                j.elemento = temp;
                mov[0] += 3;
            }
        }
        i = (i == esq.ant) ? esq : i.prox;
        Restaurante temp = i.elemento;
        i.elemento = dir.elemento;
        dir.elemento = temp;
        mov[0] += 3;
        return i;
    }

    private void quicksort(CelulaDupla esq, CelulaDupla dir, int[] comp, int[] mov) {
        if (esq != null && dir != null && esq != dir && dir.prox != esq && dir != primeiro) {
            CelulaDupla p = partition(esq, dir, comp, mov);
            quicksort(esq, p.ant, comp, mov);
            quicksort(p.prox, dir, comp, mov);
        }
    }

    public void OrdQuickListaDupla(int[] comp, int[] mov) {
        if (tamanho > 1) {
            quicksort(primeiro.prox, ultimo, comp, mov);
        }
    }
}

// --- Classe Principal / Driver Executável ---

public class ListDuplaQuickS {
    
    public static void cLog(int comparacoes, int movimentacoes, double tempo_ms) {
        try {
            FileWriter fw = new FileWriter("matricula_quicksort_flexivel.txt");
            PrintWriter pw = new PrintWriter(fw);
            // Substitua pelo número de matrícula se aplicável
            pw.printf(Locale.US, "844387\t%d\t%d\t%.4f\n", comparacoes, movimentacoes, tempo_ms);
            pw.close();
        } catch (IOException e) {
            // Ignora erros de escrita do arquivo de log
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ColecaoRestaurantes colecao = new ColecaoRestaurantes();
        colecao.lerCsv("/tmp/restaurantes.csv");

        ListaDuplaFlexivel lista = new ListaDuplaFlexivel();

        while (in.hasNext()) {
            String entrada = in.next();
            if (entrada.equals("FIM") || entrada.equals("-1")) {
                break;
            }
            int idBusca = Integer.parseInt(entrada);
            Restaurante r = colecao.buscarPorId(idBusca);
            if (r != null) {
                lista.inserirFim(r);
            }
        }

        int[] comp = {0};
        int[] mov = {0};

        long startTime = System.nanoTime();
        lista.OrdQuickListaDupla(comp, mov);
        long endTime = System.nanoTime();

        double tempo_ms = (endTime - startTime) / 1e6;

        lista.mostrar();
        cLog(comp[0], mov[0], tempo_ms);

        in.close();
    }
}