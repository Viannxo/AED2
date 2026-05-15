
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
 * CELULA DUPLA DA LISTA ENCADEADA
 * ============================================================ */
class CelulaDupla {

    Restaurante dado;
    CelulaDupla prox;
    CelulaDupla ant;

    CelulaDupla(Restaurante r) {
        this.dado = r;
        this.prox = null;
        this.ant = null;
    }
}

/* ============================================================
 * LISTA DUPLAMENTE ENCADEADA COM CABECA
 * ============================================================ */
class ListaDupla {

    private CelulaDupla primeiro; // Cabeça da lista
    private CelulaDupla ultimo;
    private int tamanho;

    public ListaDupla() {
        primeiro = new CelulaDupla(null);
        ultimo = primeiro;
        tamanho = 0;
    }

    public void inserirInicio(Restaurante r) {
        CelulaDupla tmp = new CelulaDupla(r);
        tmp.ant = primeiro;
        tmp.prox = primeiro.prox;
        primeiro.prox = tmp;

        if (primeiro == ultimo) {
            ultimo = tmp; // Se estava vazia, o ultimo é o inserido
        } else {
            tmp.prox.ant = tmp; // Ajusta o anterior do antigo primeiro elemento válido
        }
        tamanho++;
    }

    public void inserirFim(Restaurante r) {
        CelulaDupla tmp = new CelulaDupla(r);
        tmp.ant = ultimo;
        ultimo.prox = tmp;
        ultimo = tmp;
        tamanho++;
    }

    public void inserir(Restaurante r, int pos) {
        if (pos <= 0) {
            inserirInicio(r);
            return;
        }
        if (pos >= tamanho) {
            inserirFim(r);
            return;
        }

        // Caminha até a célula ANTERIOR à posição desejada
        CelulaDupla i = primeiro;
        for (int j = 0; j < pos; j++) {
            i = i.prox;
        }

        CelulaDupla tmp = new CelulaDupla(r);
        tmp.ant = i;
        tmp.prox = i.prox;
        tmp.ant.prox = tmp;
        tmp.prox.ant = tmp;
        tamanho++;
    }

    public Restaurante removerInicio() {
        if (primeiro == ultimo) {
            return null;
        }

        CelulaDupla tmp = primeiro.prox;
        Restaurante r = tmp.dado;
        primeiro.prox = tmp.prox;

        if (tmp.prox != null) {
            tmp.prox.ant = primeiro;
        } else {
            ultimo = primeiro; // A lista ficou vazia
        }

        tmp.prox = tmp.ant = null; // Isola o nodo removido
        tamanho--;
        return r;
    }

    public Restaurante removerFim() {
        if (primeiro == ultimo) {
            return null;
        }

        CelulaDupla tmp = ultimo;
        Restaurante r = tmp.dado;
        ultimo = ultimo.ant;
        ultimo.prox = null;
        tmp.ant = null; // Isola o nodo removido

        tamanho--;
        return r;
    }

    public Restaurante remover(int pos) {
        if (primeiro == ultimo) {
            return null;
        }
        if (pos <= 0) {
            return removerInicio();
        }
        if (pos >= tamanho - 1) {
            return removerFim();
        }

        // Caminha até a célula que será removida
        CelulaDupla i = primeiro.prox;
        for (int j = 0; j < pos; j++) {
            i = i.prox;
        }

        i.ant.prox = i.prox;
        i.prox.ant = i.ant;
        Restaurante r = i.dado;

        i.prox = i.ant = null; // Isola o nodo removido
        tamanho--;
        return r;
    }

    public CelulaDupla getPrimeiro() {
        return primeiro;
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

public class ListaDuplaFlex {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ColecaoRestaurantes col = new ColecaoRestaurantes();

        col.lerCsv("/tmp/restaurantes.csv");

        ListaDupla lista = new ListaDupla();

        /* Fase 1: inserirFim dos IDs ate -1 */
        while (sc.hasNext()) {
            String linha = sc.next(); // Retorna o token limpo (ignora espaços)

            // Verifica a condição de parada utilizando compareTo()
            if (linha.compareTo("-1") == 0) {
                break;
            }

            Restaurante r = col.buscarPorId(Integer.parseInt(linha));
            if (r != null) {
                lista.inserirFim(r);
            }
        }

        /* Fase 2: n operacoes */
        int qtd = sc.nextInt();

        for (int op = 0; op < qtd; op++) {
            String comando = sc.next(); // Pega a palavra do comando

            switch (comando) {
                case "II": {
                    int id = sc.nextInt();
                    Restaurante r = col.buscarPorId(id);
                    if (r != null) {
                        lista.inserirInicio(r);
                    }
                    break;
                }
                case "IF": {
                    int id = sc.nextInt();
                    Restaurante r = col.buscarPorId(id);
                    if (r != null) {
                        lista.inserirFim(r);
                    }
                    break;
                }
                case "I*": {
                    int pos = sc.nextInt();
                    int id = sc.nextInt();
                    Restaurante r = col.buscarPorId(id);
                    if (r != null) {
                        lista.inserir(r, pos);
                    }
                    break;
                }
                case "RI": {
                    Restaurante r = lista.removerInicio();
                    if (r != null) {
                        System.out.println("(R)" + r.getNome());
                    }
                    break;
                }
                case "RF": {
                    Restaurante r = lista.removerFim();
                    if (r != null) {
                        System.out.println("(R)" + r.getNome());
                    }
                    break;
                }
                case "R*": {
                    int pos = sc.nextInt();
                    Restaurante r = lista.remover(pos);
                    if (r != null) {
                        System.out.println("(R)" + r.getNome());
                    }
                    break;
                }
            }
        }
        sc.close();

        for (CelulaDupla i = lista.getPrimeiro().prox; i != null; i = i.prox) {
            System.out.println(i.dado.formatar());
        }
    }
}
