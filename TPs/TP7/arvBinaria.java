import java.io.*;
import java.util.*;
import java.text.*;

public class arvBinaria {

    //clase games
    public static class Game implements Comparable<Game> {
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

        // getters e setters
        public int getId() { return id; }
        public void setId(String idStr) { 
            try { this.id = Integer.parseInt(idStr.trim()); } 
            catch (Exception e) { this.id = -1; } 
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name != null ? name.trim() : ""; }

        public String getReleaseDate() { return releaseDate; }
        public void setReleaseDate(String s) { this.releaseDate = normalizeReleaseDate(s); }

        public int getEstimatedOwners() { return estimatedOwners; }
        public void setEstimatedOwners(String str) {
            if (str == null || str.isEmpty()) this.estimatedOwners = 0;
            else this.estimatedOwners = Integer.parseInt(str.replaceAll("[^0-9]", ""));
        }

        public float getPrice() { return price; }
        public void setPrice(String str) {
            if (str == null || str.isEmpty() || str.equalsIgnoreCase("Free to Play")) this.price = 0.0f;
            else this.price = Float.parseFloat(str.replace(",", "."));
        }

        public String[] getSupportedLanguages() { return supportedLanguages; }
        public void setSupportedLanguages(String str) { this.supportedLanguages = parseArrayField(str); }

        public int getMetacriticScore() { return metacriticScore; }
        public void setMetacriticScore(String str) {
            if (str == null || str.isEmpty()) this.metacriticScore = -1;
            else {
                try { this.metacriticScore = Integer.parseInt(str.trim()); } 
                catch (Exception e) { this.metacriticScore = -1; }
            }
        }

        public float getUserScore() { return userScore; }
        public void setUserScore(String str) {
            if (str == null || str.isEmpty() || str.equalsIgnoreCase("tbd")) this.userScore = -1.0f;
            else this.userScore = Float.parseFloat(str.replace(",", "."));
        }

        public int getAchievements() { return achievements; }
        public void setAchievements(String str) {
            if (str == null || str.isEmpty()) this.achievements = 0;
            else {
                try { this.achievements = Integer.parseInt(str.trim()); } 
                catch (Exception e) { this.achievements = 0; }
            }
        }

        public String[] getPublishers() { return publishers; }
        public void setPublishers(String str) { this.publishers = splitByComma(str); }

        public String[] getDevelopers() { return developers; }
        public void setDevelopers(String str) { this.developers = splitByComma(str); }

        public String[] getCategories() { return categories; }
        public void setCategories(String str) { this.categories = parseArrayField(str); }

        public String[] getGenres() { return genres; }
        public void setGenres(String str) { this.genres = parseArrayField(str); }

        public String[] getTags() { return tags; }
        public void setTags(String str) { this.tags = parseArrayField(str); }

        // funcoes aux
        private String[] parseArrayField(String text) {
            if (text == null || text.isEmpty()) return new String[0];
            text = text.replaceAll("\\[|\\]", "").replace("'", "").trim();
            String[] parts = text.split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
                if (parts[i].equalsIgnoreCase("Shoot Em Up")) parts[i] = "Shoot 'Em Up";
                else if (parts[i].equalsIgnoreCase("Beat Em Up")) parts[i] = "Beat 'em up";
                else if (parts[i].equalsIgnoreCase("1990s")) parts[i] = "1990's";
            }
            return parts;
        }

        private String[] splitByComma(String text) {
            if (text == null || text.isEmpty()) return new String[0];
            String[] parts = text.split(",");
            for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
            return parts;
        }

        private static String normalizeReleaseDate(String s) {
            if (s == null || s.trim().isEmpty()) return "01/01/2000";
            s = s.trim().replace("\"", "");
            SimpleDateFormat csvFullFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            SimpleDateFormat csvMonthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = csvFullFormat.parse(s);
                return targetFormat.format(date);
            } catch (ParseException e) {
                try {
                    Date date = csvMonthYearFormat.parse(s);
                    return "01/" + new SimpleDateFormat("MM/yyyy").format(date);
                } catch (ParseException e2) {
                    if (s.matches("^\\d{4}$")) return "01/01/" + s;
                    return "01/01/2000";
                }
            }
        }

        public void print() {
            System.out.print("=> " + id + " ## " + name + " ## " + releaseDate + " ## " +
                    estimatedOwners + " ## " + String.format(Locale.US, "%.1f", price) + " ## " +
                    Arrays.toString(supportedLanguages) + " ## " +
                    metacriticScore + " ## " +
                    String.format(Locale.US, "%.1f", userScore) + " ## " +
                    achievements + " ## " +
                    Arrays.toString(publishers) + " ## " +
                    Arrays.toString(developers) + " ## " +
                    Arrays.toString(categories) + " ## " +
                    Arrays.toString(genres) + " ## " +
                    Arrays.toString(tags) + " ##");
            System.out.println();
        }

        //comparador
        public int compareTo(Game outro) {
            return this.name.compareTo(outro.name);
        }
    }

    //parada
    public static boolean Parada(String line) {
        return "FIM".equals(line);
    }

    //pesquisa binaria
    public static boolean buscaBinaria(List<Game> vetor, String nomeProcurado) {
        int esq = 0, dir = vetor.size() - 1;
        while (esq <= dir) {
            int meio = esq + (dir - esq) / 2;
            int cmp = nomeProcurado.compareTo(vetor.get(meio).getName());
            if (cmp == 0) return true;
            else if (cmp < 0) dir = meio - 1;
            else esq = meio + 1;
        }
        return false;
    }

    //Arvore
    //nó
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

        public ArvoreBinaria() {
            raiz = null;
        }

        // inserir por nome (evitar duplicados)
        public void inserir(Game g) {
            raiz = inserir(g, raiz);
        }

        private No inserir(Game g, No i) {
            if (i == null) return new No(g);

            int cmp = g.getName().compareTo(i.elemento.getName());

            if (cmp < 0) i.esq = inserir(g, i.esq);
            else if (cmp > 0) i.dir = inserir(g, i.dir);
            // se for igual, NÃO insere (regra do enunciado)
            return i;
        }

        // PESQUISA COM IMPRESSÃO DO CAMINHO
        public boolean pesquisar(String nome) {
            System.out.print("raiz ");
            return pesquisar(nome, raiz);
        }

        private boolean pesquisar(String nome, No i) {
            if (i == null) return false;

            int cmp = nome.compareTo(i.elemento.getName());

            if (cmp == 0) return true;

            if (cmp < 0) {
                System.out.print("esq ");
                return pesquisar(nome, i.esq);

            } else {
                System.out.print("dir ");
                return pesquisar(nome, i.dir);
            }
        }
    }

    // main
        public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        List<Game> listaGames = new ArrayList<>();
        List<String> nomesParaPesquisar = new ArrayList<>();
        int contaFIM = 0;

        while (sc.hasNextLine()) {

            String linha = sc.nextLine().trim();

            if (linha.equals("FIM")) {
                contaFIM++;
                if (contaFIM == 2) break;
                continue;
            }

            // PRIMEIRO BLOCO (games)
            if (contaFIM == 0) {

                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (campos.length < 2) continue;

                Game g = new Game();
                g.setId(campos[0]);
                g.setName(campos[1]);

                listaGames.add(g);
            }

            // SEGUNDO BLOCO (nomes para pesquisar)
            else if (contaFIM == 1) {
                nomesParaPesquisar.add(linha);
            }
        }

        sc.close();

        // ====== MONTAR ÁRVORE BINÁRIA USANDO NAME COMO CHAVE ======
        ArvoreBinaria arv = new ArvoreBinaria();

        for (Game g : listaGames) {
            arv.inserir(g);
        }

        // ====== PESQUISAR NOMES ======
        for (String nome : nomesParaPesquisar) {
            boolean resp = arv.pesquisar(nome);
            System.out.println(resp ? "SIM" : "NAO");
        }
    }
}
