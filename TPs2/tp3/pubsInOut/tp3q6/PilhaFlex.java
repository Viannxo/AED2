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

        // Limpeza manual da string SEM utilizar o método .replace()
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
                // Apenas recebe o token. O CSV do Verde não tem espaços ao redor do ';'
                // Logo, o .trim() não é necessário.
                tps[i] = sc.next();
            }
        }
        sc.close();
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
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private TiposCozinha tiposCozinha;
    private int faixaPreco;
    private Data dataAbertura;
    private Hora abertura;
    private Hora fechamento;
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

    public static Restaurante parseRestaurante(String s) {
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");
        int id = sc.nextInt();
        String nome = sc.next();
        String cidade = sc.next();
        int capacidade = sc.nextInt();
        double avalia = Double.parseDouble(sc.next());
        TiposCozinha tpc = TiposCozinha.create(sc.next());
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

/* ============================================================
 * CELULA DA PILHA ENCADEADA
 * ============================================================ */
class Celula {
    
    Restaurante dado;
    Celula prox;

    Celula(Restaurante r) {
        this.dado = r;
        this.prox = null;
    }
}

/* ============================================================
 * PILHA SIMPLESMENTE ENCADEADA SEM CABECA
 * ============================================================ */
class PilhaEncadeada {
    
    private Celula topo;

    PilhaEncadeada() {
        this.topo = null;
    }

    public void push(Restaurante r) {
        Celula tmp = new Celula(r);
        tmp.prox = topo;
        topo = tmp;
    }

    public Restaurante pop() {
        if (topo == null) {
            return null;
        }
        Restaurante r = topo.dado;
        Celula tmp = topo;
        topo = topo.prox;
        tmp.prox = null;
        return r;
    }

    public Celula getTopo() {
        return topo;
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
}

public class PilhaFlex {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes col = new ColecaoRestaurantes();
        
        col.lerCsv("/tmp/restaurantes.csv");

        PilhaEncadeada pilha = new PilhaEncadeada();

        /* Fase 1: empilha IDs ate -1 */
        while (sc.hasNext()) {
            String linha = sc.next(); // Retorna a string limpa de quebras de linha e espaços
            
            // Verifica a condição de parada utilizando compareTo()
            if (linha.compareTo("-1") == 0) {
                break;
            }
            
            Restaurante r = col.buscarPorId(Integer.parseInt(linha));
            if (r != null) {
                pilha.push(r);
            }
        }

        /* Fase 2: n operacoes I (push) e R (pop) */
        int qtd = sc.nextInt();
        
        for (int op = 0; op < qtd; op++) {
            String comando = sc.next();
            
            // Verifica o comando utilizando compareTo()
            if (comando.compareTo("I") == 0) {
                int idBusca = sc.nextInt();
                Restaurante r = col.buscarPorId(idBusca);
                if (r != null) {
                    pilha.push(r);
                }
            } else if (comando.compareTo("R") == 0) {
                Restaurante r = pilha.pop();
                if (r != null) {
                    System.out.println("(R)" + r.getNome());
                }
            }
        }
        sc.close();

        /* Impressao final: TOPO ao FUNDO (percorre a cadeia) */
        for (Celula i = pilha.getTopo(); i != null; i = i.prox) {
            System.out.println(i.dado.formatar());
        }
    }
}