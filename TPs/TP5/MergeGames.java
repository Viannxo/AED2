import java.io.*;
import java.util.*;
import java.text.*;

public class MergeGames {

    // classe game
    public static class Game {
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

        //getters e setters
        public int getId() { return id; }
        public void setId(String idStr) {
            try { this.id = Integer.parseInt(idStr.trim()); } catch (Exception e) { this.id = -1; }
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
                try { this.metacriticScore = Integer.parseInt(str.trim()); } catch (Exception e) { this.metacriticScore = -1; }
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
                try { this.achievements = Integer.parseInt(str.trim()); } catch (Exception e) { this.achievements = 0; }
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

        //metodos aux
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
            if (s == null || s.trim().isEmpty()) return "01/01/0001";
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

        private String formatPrice(float value) {
            String formatted = String.format(Locale.US, "%.2f", value);
            if (formatted.endsWith(".00")) return "0.0";
            if (formatted.charAt(formatted.length() - 1) == '0')
                return String.format(Locale.US, "%.1f", value);
            return formatted;
        }

        private String formatArray(String[] array) {
            return Arrays.toString(array);
        }

        public void print() {
            System.out.print("=> " + id + " ## " + name + " ## " + releaseDate + " ## " +
                    estimatedOwners + " ## " + formatPrice(price) + " ## " +
                    formatArray(supportedLanguages) + " ## " +
                    metacriticScore + " ## " +
                    String.format(Locale.US, "%.1f", userScore) + " ## " +
                    achievements + " ## " +
                    formatArray(publishers) + " ## " +
                    formatArray(developers) + " ## " +
                    formatArray(categories) + " ## " +
                    formatArray(genres) + " ## " +
                    formatArray(tags) + " ##");
            System.out.println();
        }
    }

    //Merge sort
    public static void mergeSort(Game[] array, int esquerda, int direita) {
        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;
            mergeSort(array, esquerda, meio);
            mergeSort(array, meio + 1, direita);
            merge(array, esquerda, meio, direita);
        }
    }

    private static void merge(Game[] array, int esquerda, int meio, int direita) {
        int tamanhoEsq = meio - esquerda + 1;
        int tamanhoDir = direita - meio;

        Game[] esquerdaArr = new Game[tamanhoEsq];
        Game[] direitaArr = new Game[tamanhoDir];

        for (int i = 0; i < tamanhoEsq; i++) esquerdaArr[i] = array[esquerda + i];
        for (int j = 0; j < tamanhoDir; j++) direitaArr[j] = array[meio + 1 + j];

        int i = 0, j = 0, k = esquerda;

        // comparação por preço e id
        while (i < tamanhoEsq && j < tamanhoDir) {
            if (compare(esquerdaArr[i], direitaArr[j]) <= 0) {
                array[k] = esquerdaArr[i];
                i++;
            } else {
                array[k] = direitaArr[j];
                j++;
            }
            k++;
        }

        while (i < tamanhoEsq) {
            array[k] = esquerdaArr[i];
            i++;
            k++;
        }

        while (j < tamanhoDir) {
            array[k] = direitaArr[j];
            j++;
            k++;
        }
    }

    private static int compare(Game a, Game b) {
        if (a.getPrice() < b.getPrice()) return -1;
        if (a.getPrice() > b.getPrice()) return 1;
        return Integer.compare(a.getId(), b.getId());
    }

    //leitura e parada
    public static boolean Parada(String line) {
        return line.equals("FIM");
    }

    public static List<Game> readFromCSV(String filePath) {
        List<Game> games = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = splitCSVLine(line);
                if (f.length < 14) continue;
                Game g = new Game();
                g.setId(f[0]);
                g.setName(f[1]);
                g.setReleaseDate(f[2]);
                g.setEstimatedOwners(f[3]);
                g.setPrice(f[4]);
                g.setSupportedLanguages(f[5]);
                g.setMetacriticScore(f[6]);
                g.setUserScore(f[7]);
                g.setAchievements(f[8]);
                g.setPublishers(f[9]);
                g.setDevelopers(f[10]);
                g.setCategories(f[11]);
                g.setGenres(f[12]);
                g.setTags(f[13]);
                games.add(g);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler CSV: " + e.getMessage());
        }
        return games;
    }

    private static String[] splitCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field.setLength(0);
            } else field.append(c);
        }
        result.add(field.toString());
        return result.toArray(new String[0]);
    }

    //main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String csvPath = "/tmp/games.csv";
        List<Game> gamesList = readFromCSV(csvPath);
        List<Game> selected = new ArrayList<>();

        // lê IDs até FIM
        while (true) {
            String line = sc.nextLine().trim();
            if (Parada(line)) break;
            try {
                int id = Integer.parseInt(line);
                for (Game g : gamesList) {
                    if (g.getId() == id) {
                        selected.add(g);
                        break;
                    }
                }
            } catch (Exception e) {}
        }

        // converte lista para array
        Game[] array = selected.toArray(new Game[0]);

        // ordena por price (desempate por id)
        mergeSort(array, 0, array.length - 1);

        System.out.println("| 5 pre\u00E7os mais caros |");
        int count = 0;
        for (int i = array.length - 1; i >= 0 && count < 5; i--) {
            array[i].print();
            count++;
        }

        System.out.println("\n| 5 pre\u00E7os mais baratos |");
        for (int i = 0; i < array.length && i < 5; i++) {
            array[i].print();
        }

        sc.close();
    }
}
