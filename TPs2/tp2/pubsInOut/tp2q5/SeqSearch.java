import java.io.*;
import java.util.*;

// --- Classes Auxiliares ---

class Data {
    private int ano, mes, dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

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
    private int hora, minuto;

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
        for (int i = 0; i < faixa; i++)
            resp += "$";
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
        for (int i = 0; i < tipos.length(); i++)
            if (tipos.charAt(i) == ';') count++;

        String[] tps = {};
        if (sc.hasNext()) {
            tps = new String[count + 1];
            for (int i = 0; i < count + 1; i++)
                tps[i] = sc.next();
        }
        sc.close();
        return new TiposCozinha(tps);
    }

    public String formatar() {
        String res = "[";
        for (int i = 0; i < types.length; i++) {
            res += types[i];
            if (i < types.length - 1) res += ",";
        }
        return res + "]";
    }
}

// --- Restaurante ---

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
    public String getCidade() { return cidade; }

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        int id = sc.nextInt();
        String nome = sc.next();
        String cidade = sc.next();
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
        scH.close();

        Data dAbrir = Data.parseData(sc.next());
        boolean func = sc.nextBoolean();
        sc.close();

        return new Restaurante(id, nome, cidade, capacidade, avalia, tpc, preco, dAbrir, hAbrir, hFechar, func);
    }

    public String formatar() {
        Price preco = new Price(faixaPreco);
        return String.format(Locale.US,
            "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
            id, nome, cidade, capacidade, avaliacao,
            tiposCozinha.formatar(), preco.formatar(),
            abertura.formatar(), fechamento.formatar(),
            dataAbertura.formatar(), aberto);
    }
}

// --- Coleção ---

class ColecaoRestaurantes {
    private Restaurante[] cRest;
    private int n;

    // Contadores para o log
    public long comparacoes = 0;
    public long movimentacoes = 0;

    public ColecaoRestaurantes() {
        cRest = new Restaurante[1000];
        n = 0;
    }

    public void lerCsv(String path) {
        try {
            Scanner fsc = new Scanner(new File(path));
            if (fsc.hasNextLine()) fsc.nextLine(); // pula cabeçalho
            while (fsc.hasNextLine() && n < cRest.length)
                cRest[n++] = Restaurante.parseRestaurante(fsc.nextLine());
            fsc.close();
        } catch (FileNotFoundException e) {
            // sem print
        }
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < n; i++)
            if (cRest[i].getId() == id) return cRest[i];
        return null;
    }

    public void adicionar(Restaurante r) {
        if (n < cRest.length) cRest[n++] = r;
    }

    // Insertion Sort — chave: nome (alfabético crescente)
    public void insertionSort() {
        comparacoes   = 0;
        movimentacoes = 0;

        for (int i = 1; i < n; i++) {
            Restaurante chave = cRest[i];
            movimentacoes++; // leitura da chave
            int j = i - 1;

            while (j >= 0) {
                comparacoes++;
                if (cRest[j].getNome().compareTo(chave.getNome()) > 0) {
                    cRest[j + 1] = cRest[j];
                    movimentacoes++;
                    j--;
                } else {
                    break;
                }
            }

            cRest[j + 1] = chave;
            movimentacoes++; // escrita da chave na posição final
        }
    }

    // Pesquisa Sequencial — busca por nome exato
    public int pesquisaSequencial(String nome) {
        for (int i = 0; i < n; i++)
            if (cRest[i].getNome().equals(nome)) return i;
        return -1;
    }
}

// --- Main ---

public class SeqSearch {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        ColecaoRestaurantes colecao  = new ColecaoRestaurantes();
        ColecaoRestaurantes colecao2 = new ColecaoRestaurantes();

        colecao.lerCsv("/tmp/restaurantes.csv");

        // Fase 1: leitura de IDs até -1
        int idBusca = in.nextInt();
        in.nextLine(); // consome newline
        while (idBusca != -1) {
            Restaurante r = colecao.buscarPorId(idBusca);
            if (r != null) colecao2.adicionar(r);
            idBusca = in.nextInt();
            in.nextLine();
        }

        // Ordenar por cidade via Insertion Sort
        long inicio = System.currentTimeMillis();
        colecao2.insertionSort();
        long fim = System.currentTimeMillis();
        double tempo = (double)(fim - inicio);

        // Fase 2: pesquisa sequencial por nome até FIM
        while (in.hasNextLine()) {
            String entrada = in.nextLine().trim();
            if (entrada.equals("FIM")) break;

            int idx = colecao2.pesquisaSequencial(entrada);
            if (idx >= 0)
                System.out.println("SIM");
            else
                System.out.println("NAO");
        }

        // Log
        try (PrintWriter log = new PrintWriter(new FileWriter("844387_insercao.txt"))) {
            log.printf("844387\t%d\t%d\t%.2f",
                colecao2.comparacoes, colecao2.movimentacoes, tempo);
        }

        in.close();
    }
}
