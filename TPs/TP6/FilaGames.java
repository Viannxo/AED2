import java.io.*;
import java.util.*;
import java.text.*;

public class FilaGames {

    // celula
    private static class Celula {
        public Game elemento;
        public Celula prox;

        public Celula(Game elemento) {
            this.elemento = elemento;
            this.prox = null;
        }

        public Celula() {
            this(null);
        }
    }

    private Celula primeiro, ultimo;
    private int tamanho;
    private static final int MAX_SIZE = 82;

    public FilaGames() {
        primeiro = new Celula();
        ultimo = primeiro;
        tamanho = 0;
    }

    public Game remover() throws Exception {
        if (primeiro == ultimo)
            throw new Exception("Erro ao remover: fila vazia!");
        Celula tmp = primeiro;
        primeiro = primeiro.prox;
        Game resp = primeiro.elemento;
        tmp.prox = null;
        tamanho--;
        System.out.println("(R) " + resp.getName());

        return resp;
    }

    public void inserir(Game g) throws Exception {
        if (tamanho == MAX_SIZE)
            remover();
        ultimo.prox = new Celula(g);
        ultimo = ultimo.prox;
        tamanho++;
    }

    public void imprimir() {
        int contador = 0;
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            //System.out.println(contadorSaida++);
            System.out.print("[" + contador++ +"] => ");
            i.elemento.print();
        }
    }

    // game
    private static class Game {
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

        public Game() {
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(String idStr) {
            try {
                this.id = Integer.parseInt(idStr.trim());
            } catch (Exception e) {
                this.id = -1;
            }
        }

        public void setName(String name) {
            this.name = name != null ? name.trim() : "";
        }

        public void setReleaseDate(String s) {
            this.releaseDate = normalizeReleaseDate(s);
        }

        public void setEstimatedOwners(String str) {
            if (str == null || str.isEmpty())
                this.estimatedOwners = 0;
            else
                this.estimatedOwners = Integer.parseInt(str.replaceAll("[^0-9]", ""));
        }

        public void setPrice(String str) {
            if (str == null || str.isEmpty() || str.equalsIgnoreCase("Free to Play"))
                this.price = 0.0f;
            else
                this.price = Float.parseFloat(str.replace(",", "."));
        }

        public void setSupportedLanguages(String str) {
            this.supportedLanguages = parseArrayField(str);
        }

        public void setMetacriticScore(String str) {
            if (str == null || str.isEmpty())
                this.metacriticScore = -1;
            else {
                try {
                    this.metacriticScore = Integer.parseInt(str.trim());
                } catch (Exception e) {
                    this.metacriticScore = -1;
                }
            }
        }

        public void setUserScore(String str) {
            if (str == null || str.isEmpty() || str.equalsIgnoreCase("tbd"))
                this.userScore = -1.0f;
            else
                this.userScore = Float.parseFloat(str.replace(",", "."));
        }

        public void setAchievements(String str) {
            if (str == null || str.isEmpty())
                this.achievements = 0;
            else {
                try {
                    this.achievements = Integer.parseInt(str.trim());
                } catch (Exception e) {
                    this.achievements = 0;
                }
            }
        }

        public void setPublishers(String str) {
            this.publishers = splitByComma(str);
        }

        public void setDevelopers(String str) {
            this.developers = splitByComma(str);
        }

        public void setCategories(String str) {
            this.categories = parseArrayField(str);
        }

        public void setGenres(String str) {
            this.genres = parseArrayField(str);
        }

        public void setTags(String str) {
            this.tags = parseArrayField(str);
        }

        // metodos aux
        private String[] parseArrayField(String text) {
            if (text == null || text.isEmpty())
                return new String[0];
            text = text.replaceAll("\\[|\\]", "").replace("'", "").trim();
            String[] parts = text.split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
                if (parts[i].equalsIgnoreCase("Shoot Em Up"))
                    parts[i] = "Shoot 'Em Up";
                else if (parts[i].equalsIgnoreCase("Beat Em Up"))
                    parts[i] = "Beat 'em up";
                else if (parts[i].equalsIgnoreCase("1990s"))
                    parts[i] = "1990's";
            }
            return parts;
        }

        private String[] splitByComma(String text) {
            if (text == null || text.isEmpty())
                return new String[0];
            String[] parts = text.split(",");
            for (int i = 0; i < parts.length; i++)
                parts[i] = parts[i].trim();
            return parts;
        }

        private static String normalizeReleaseDate(String s) {
            if (s == null || s.trim().isEmpty())
                return "01/01/0001";
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
                    if (s.matches("^\\d{4}$"))
                        return "01/01/" + s;
                    return "01/01/2000";
                }
            }
        }

        private String formatPrice(float value) {
            String formatted = String.format(Locale.US, "%.2f", value);
            if (formatted.endsWith(".00"))
                return "0.0";
            if (formatted.charAt(formatted.length() - 1) == '0')
                return String.format(Locale.US, "%.1f", value);
            return formatted;
        }

        public void print() {
            System.out.print(id + " ## " + name + " ## " + releaseDate + " ## " +
                    estimatedOwners + " ## " + formatPrice(price) + " ## " +
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
    }

    // leitura csv
    private static String[] splitCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"')
                inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field.setLength(0);
            } else
                field.append(c);
        }
        result.add(field.toString());
        return result.toArray(new String[0]);
    }

    private static List<Game> readFromCSV(String csvPath) throws Exception {
        List<Game> games = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); // cabeçalho
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = splitCSVLine(line);
                if (fields.length < 14)
                    continue;
                Game g = new Game();
                g.setId(fields[0]);
                g.setName(fields[1]);
                g.setReleaseDate(fields[2]);
                g.setEstimatedOwners(fields[3]);
                g.setPrice(fields[4]);
                g.setSupportedLanguages(fields[5]);
                g.setMetacriticScore(fields[6]);
                g.setUserScore(fields[7]);
                g.setAchievements(fields[8]);
                g.setPublishers(fields[9]);
                g.setDevelopers(fields[10]);
                g.setCategories(fields[11]);
                g.setGenres(fields[12]);
                g.setTags(fields[13]);
                games.add(g);
            }
        }
        return games;
    }

    public static Game findById(List<Game> games, int id) {
        for (Game g : games)
            if (g.getId() == id)
                return g;
        return null;
    }

    // parada
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    // main
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String csvPath = "/tmp/games.csv";
        List<Game> games = readFromCSV(csvPath);
        FilaGames fila = new FilaGames();

        // Leitura inicial até "FIM"
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (Parada(line))
                break;
            try {
                int id = Integer.parseInt(line);
                Game g = findById(games, id);
                if (g != null)
                    fila.inserir(g);
            } catch (NumberFormatException ignored) {
            }
        }

        // Leitura dos comandos
        int n = 0;
        if (sc.hasNextLine()) {
            try {
                n = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ignored) {
            }
        }

        for (int i = 0; i < n && sc.hasNextLine(); i++) {
            String comando = sc.nextLine().trim();
            if (comando.startsWith("I")) {
                int id = Integer.parseInt(comando.substring(1).trim());
                Game g = findById(games, id);
                if (g != null)
                    fila.inserir(g);
            } else if (comando.trim().equals("R")) {
                try {
                    fila.remover();
                } catch (Exception ignored) {
                }
            }
        }

        // print fila
        fila.imprimir();
        sc.close();
    }
}
