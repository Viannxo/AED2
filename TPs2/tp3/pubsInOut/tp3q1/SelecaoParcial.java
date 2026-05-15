import java.io.*;
import java.util.*;

class Data {

    private int ano, mes, dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public static Data parseData(String s) {
        String p0 = "", p1 = "", p2 = "";
        int parte = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-') {
                parte++;
            } else {
                if (parte == 0) p0 += c;
                else if (parte == 1) p1 += c;
                else p2 += c;
            }
        }
        return new Data(Integer.parseInt(p0), Integer.parseInt(p1), Integer.parseInt(p2));
    }

    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}

class Hora {

    private int hora, minuto;

    public Hora(int h, int m) {
        this.hora = h;
        this.minuto = m;
    }

    public static Hora parseHora(String s) {
        String p0 = "", p1 = "";
        int parte = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ':') {
                parte++;
            } else {
                if (parte == 0) p0 += c;
                else p1 += c;
            }
        }
        return new Hora(Integer.parseInt(p0), Integer.parseInt(p1));
    }

    public String formatar() {
        return String.format("%02d:%02d", hora, minuto);
    }
}

class TiposCozinha {

    private String[] types;

    public TiposCozinha(String[] t) {
        this.types = t;
    }

    public static TiposCozinha create(String tipos) {
        String[] t = new String[20]; // Assumindo no máximo 20 tipos
        int count = 0;
        String atual = "";
        
        for (int i = 0; i < tipos.length(); i++) {
            char c = tipos.charAt(i);
            if (c == '[' || c == ']') {
                continue; // Ignora os colchetes
            } else if (c == ';') {
                t[count++] = atual;
                atual = "";
            } else {
                atual += c;
            }
        }
        if (atual.length() > 0) {
            t[count++] = atual;
        }
        
        // Copia para um array com o tamanho exato
        String[] exatos = new String[count];
        for (int i = 0; i < count; i++) {
            exatos[i] = t[i];
        }
        return new TiposCozinha(exatos);
    }

    public String formatar() {
        String res = "[";
        for (int i = 0; i < types.length; i++) {
            res += types[i];
            if (i < types.length - 1) {
                res += ",";
            }
        }
        res += "]";
        return res;
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

    public Restaurante(int id, String nome, String cidade, int cap, double aval,
            TiposCozinha tc, int fp, Data da, Hora ab, Hora fe, boolean ab2) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = cap;
        this.avaliacao = aval;
        this.tiposCozinha = tc;
        this.faixaPreco = fp;
        this.dataAbertura = da;
        this.abertura = ab;
        this.fechamento = fe;
        this.aberto = ab2;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static Restaurante parseRestaurante(String s) {
        String[] p = new String[10];
        for (int i = 0; i < 10; i++) p[i] = "";
        int parte = 0;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ',') {
                parte++;
            } else {
                p[parte] += c;
            }
        }

        int id = Integer.parseInt(p[0]);
        String nome = p[1];
        String cidade = p[2];
        int cap = Integer.parseInt(p[3]);
        double aval = Double.parseDouble(p[4]);
        TiposCozinha tpc = TiposCozinha.create(p[5]);
        int preco = p[6].length();

        String hStr = p[7];
        String h0 = "", h1 = "";
        int hParte = 0;
        for (int i = 0; i < hStr.length(); i++) {
            char c = hStr.charAt(i);
            if (c == '-') {
                hParte++;
            } else {
                if (hParte == 0) h0 += c;
                else h1 += c;
            }
        }

        Hora hA = Hora.parseHora(h0);
        Hora hF = Hora.parseHora(h1);
        Data dA = Data.parseData(p[8]);
        boolean func = Boolean.parseBoolean(p[9]);
        
        return new Restaurante(id, nome, cidade, cap, aval, tpc, preco, dA, hA, hF, func);
    }

    public String formatar() {
        String cifroes = "";
        for (int i = 0; i < faixaPreco; i++) {
            cifroes += "$";
        }
        return String.format(Locale.US, "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %b]",
                id, nome, cidade, capacidade, avaliacao, tiposCozinha.formatar(),
                cifroes, abertura.formatar(), fechamento.formatar(),
                dataAbertura.formatar(), aberto);
    }
}

class ColecaoRestaurantes {

    private Restaurante[] cRest;
    private int n;

    public ColecaoRestaurantes() {
        cRest = new Restaurante[10000]; 
        n = 0;
    }

    public void lerCsv(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Pular cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                adicionar(Restaurante.parseRestaurante(linha));
            }
        } catch (IOException e) {
            /* Silencioso */ 
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

public class SelecaoParcial {

    static int comparacoes = 0;
    static int movimentacoes = 0;

    public static void selecaoParcial(ColecaoRestaurantes col, int k) {
        int n = col.getN();
        // O limite do loop externo define quantos elementos serão fixados no início 
        for (int i = 0; i < k && i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                comparacoes++;
                int cmp = col.get(j).getNome().compareTo(col.get(min).getNome());
                
                // Critério primário: Nome 
                if (cmp < 0) {
                    min = j;
                } 
                // Critério de desempate: Id
                else if (cmp == 0) {
                    comparacoes++;
                    if (col.get(j).getId() < col.get(min).getId()) {
                        min = j;
                    }
                }
            }
            // Realiza a troca
            if (min != i) {
                Restaurante temp = col.get(i);
                col.set(i, col.get(min));
                col.set(min, temp);
                movimentacoes += 3;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ColecaoRestaurantes base = new ColecaoRestaurantes();
        base.lerCsv("/tmp/restaurantes.csv");

        ColecaoRestaurantes selecionados = new ColecaoRestaurantes();
        int idBusca = in.nextInt();
        while (idBusca != -1) {
            Restaurante r = base.buscarPorId(idBusca);
            if (r != null) {
                selecionados.adicionar(r);
            }
            idBusca = in.nextInt();
        }

        long inicio = System.currentTimeMillis();
        // Aplica a seleção parcial (apenas os 10 primeiros ficarão completamente ordenados)
        selecaoParcial(selecionados, 10);
        long fim = System.currentTimeMillis();

        // Alteração feita aqui: agora o loop vai até selecionados.getN() para imprimir todos
        for (int i = 0; i < selecionados.getN(); i++) {
            System.out.println(selecionados.get(i).formatar());
        }

        try (PrintWriter log = new PrintWriter(new FileWriter("844387_selecao_parcial.txt"))) {
            log.printf(Locale.US, "844387\t%d\t%d\t%dms", comparacoes, movimentacoes, (fim - inicio));
        } catch (IOException e) {
        }
        in.close();
    }
}