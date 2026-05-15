import java.io.*;
import java.util.*;

class Data {

    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAno() {
        return this.ano;
    }

    public int getMes() {
        return this.mes;
    }

    public int getDia() {
        return this.dia;
    }

    public void setAno(int ano) {
        this.ano = ano;
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
            resp += "$";
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
        
        // Limpeza manual da string sem utilizar o método .replace()
        String tiposLimpo = "";
        for (int i = 0; i < tipos.length(); i++) {
            char c = tipos.charAt(i);
            // Ignora colchetes e aspas
            if (c != '[' && c != ']' && c != '"') {
                tiposLimpo += c;
            }
        }
        
        Scanner sc = new Scanner(tiposLimpo);
        sc.useDelimiter(";");
        int count = 0;
        for (int i = 0; i < tiposLimpo.length(); i++) {
            if (tiposLimpo.charAt(i) == ';') {
                count++;
            }
        }

        String[] tps = {};
        if (sc.hasNext()) {
            tps = new String[count + 1];

            for (int i = 0; i < count + 1; i++) {
                // O .trim() remove espaços vazios nas extremidades, se existirem
                tps[i] = sc.next().trim();
            }
        }
        return new TiposCozinha(tps);
    }

    public String formatar() {

        String res = "[";
        for (int i = 0; i < types.length; i++) {
            res += types[i];
            if (i < types.length - 1) {
                res += ",";
            }
        }
        return res + "]";
    }
}

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

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

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
        Data dAbrir = Data.parseData(sc.next());
        boolean func = sc.nextBoolean();
        sc.close();
        scH.close();
        return new Restaurante(id, nome, cidade, capacidade, avalia, tpc, preco, dAbrir, hAbrir, hFechar, func);
    }

    public String formatar() {
        Price preco = new Price(faixaPreco);
        return String.format(Locale.US, "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
                id, nome, cidade, capacidade, avaliacao, tiposCozinha.formatar(), preco.formatar(),
                abertura.formatar(), fechamento.formatar(), dataAbertura.formatar(), aberto);
    }
}

class ColecaoRestaurantes {

    private Restaurante[] cRest;
    private int n;

    public ColecaoRestaurantes() {
        cRest = new Restaurante[1000];
        n = 0;
    }

    public void lerCsv(String path) {
        try {
            Scanner fsc = new Scanner(new File(path));
            if (fsc.hasNextLine()) {
                fsc.nextLine();
            }
            while (fsc.hasNextLine() && n < cRest.length) {
                cRest[n++] = Restaurante.parseRestaurante(fsc.nextLine());
            }
            fsc.close();
        } catch (FileNotFoundException e) {
            // Ignora o erro para manter a compatibilidade
        }
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < n; i++) {
            if (cRest[i].getId() == id) {
                return cRest[i];
            }
        }
        return null;
    }

    public void adicionar(Restaurante r) {
        if (n < cRest.length) {
            cRest[n++] = r;
        }
    }

    public int getN() {
        return n;
    }

    public Restaurante get(int i) {
        return cRest[i];
    }

    public void set(int i, Restaurante r) {
        cRest[i] = r;
    }
}

public class OrdQuickParcial {

    static double comparacoes = 0;
    static double movimentacoes = 0;

    static int comparar(Restaurante a, Restaurante b) {
        if (a.getAvaliacao() < b.getAvaliacao()) return -1;
        if (a.getAvaliacao() > b.getAvaliacao()) return 1;
        return a.getNome().compareTo(b.getNome());
    }

    static void swap(ColecaoRestaurantes col, int i, int j) {
        Restaurante tmp = col.get(i);
        col.set(i, col.get(j));
        col.set(j, tmp);
        movimentacoes += 3;
    }

    static void quickSortParcial(ColecaoRestaurantes col, int esq, int dir, int k) {
        int i = esq, j = dir;
        Restaurante pivo = col.get((esq + dir) / 2);

        while (i <= j) {
            while (true) {
                comparacoes++;
                if (comparar(col.get(i), pivo) < 0) {
                    i++;
                } else {
                    break;
                }
            }
            while (true) {
                comparacoes++;
                if (comparar(col.get(j), pivo) > 0) {
                    j--;
                } else {
                    break;
                }
            }
            if (i <= j) {
                swap(col, i, j);
                i++;
                j--;
            }
        }

        if (esq < j) {
            quickSortParcial(col, esq, j, k);
        }
        if (i < k && i < dir) {
            quickSortParcial(col, i, dir, k);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes colecao = new ColecaoRestaurantes();
        
        colecao.lerCsv("/tmp/restaurantes.csv");

        ColecaoRestaurantes selecionados = new ColecaoRestaurantes();

        int idBusca = sc.nextInt();
        while (idBusca != -1) {
            Restaurante r = colecao.buscarPorId(idBusca);
            if (r != null) {
                selecionados.adicionar(r);
            }
            idBusca = sc.nextInt();
        }
        sc.close();

        long inicio = System.nanoTime();
        quickSortParcial(selecionados, 0, selecionados.getN() - 1, 10);
        long fim = System.nanoTime();
        double tempo = (fim - inicio) / 1_000_000.0;

        for (int i = 0; i < selecionados.getN(); i++) {
            System.out.println(selecionados.get(i).formatar());
        }

        PrintWriter log = new PrintWriter(new FileWriter("844387_quicksort_parcial.txt"));
        log.printf(Locale.US, "844387\t%.0f\t%.0f\t%.2f\n", comparacoes, movimentacoes, tempo);
        log.close();
    }
}