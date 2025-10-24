import java.io.*;
import java.util.*;
import java.text.*;

public class pBinariaGames {

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

    // main
    public static void main(String[] args) throws IOException {
    Scanner sc = new Scanner(System.in);

    List<Game> vetor = new ArrayList<>();
    List<String> nomesParaPesquisar = new ArrayList<>();
    int contadorFIM = 0; // contador de "FIM"

    while (sc.hasNextLine()) {
        String linha = sc.nextLine().trim();
        if (linha.equals("FIM")) {
            contadorFIM++;
            if (contadorFIM == 2) break; // se forem dois FIM, termina a leitura
            continue; // caso seja o primeiro FIM, passa para a próxima leitura
        }

        if (contadorFIM == 0) {
            // PRIMEIRA PARTE: leitura do CSV ou dados dos games
            String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if (campos.length < 2) continue;

            Game g = new Game();
            g.setId(campos[0]);
            g.setName(campos[1]);
            vetor.add(g);

        } else if (contadorFIM == 1) {
            // SEGUNDA PARTE: nomes para pesquisa
            nomesParaPesquisar.add(linha);
        }
    }

    sc.close();

    // Ordenar vetor pelo nome
    Collections.sort(vetor);

    // Busca binária
    for (String nome : nomesParaPesquisar) {
        System.out.println(buscaBinaria(vetor, nome) ? " SIM" : " NAO");
    }
}
}
