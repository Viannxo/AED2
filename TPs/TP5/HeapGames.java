import java.io.*;
import java.util.*;
import java.text.*;

public class HeapGames {

    // =========================================================================
    // CLASSE GAME (inalterada)
    // =========================================================================
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

        // Getters e Setters
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

        private String[] parseArrayField(String text) {
            if (text == null || text.isEmpty()) return new String[0];
            text = text.replaceAll("\\[|\\]", "").replace("'", "").trim();
            String[] parts = text.split(",");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
                if (parts[i].equalsIgnoreCase("Shoot Em Up")) parts[i] = "Shoot 'Em Up";
                else if (parts[i].equalsIgnoreCase("Beat Em Up")) parts[i] = "Beat 'em up";
                else if(parts[i].equalsIgnoreCase("1990s")) parts[i] = "1990's";
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

        public void print() {
            System.out.print("=> " + id + " ## " + name + " ## " + releaseDate + " ## " +
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

    // =========================================================================
    // MÉTODOS AUXILIARES
    // =========================================================================
    public static boolean Parada(String line) {
        return (line.length() == 3 &&
                line.charAt(0) == 'F' &&
                line.charAt(1) == 'I' &&
                line.charAt(2) == 'M');
    }

    public static List<Game> readFromCSV(String filePath) {
        List<Game> games = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // pula cabeçalho
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = splitCSVLine(line);
                if (fields.length < 14) continue;

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
            if (c == '\"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field.setLength(0);
            } else field.append(c);
        }
        result.add(field.toString());
        return result.toArray(new String[0]);
    }

    public static Game findById(List<Game> list, int id) {
        for (Game g : list) {
            if (g.getId() == id) return g;
        }
        return null;
    }

    // =========================================================================
    // HEAPSORT
    // =========================================================================
    public static class HeapSortStats {
        long comparacoes = 0;
        long movimentacoes = 0;
    }

    public static void heapSort(Game[] arr, HeapSortStats stats) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i, stats);

        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i, stats);
            heapify(arr, i, 0, stats);
        }
    }

    private static void heapify(Game[] arr, int n, int i, HeapSortStats stats) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compareGames(arr[left], arr[largest], stats) > 0)
            largest = left;

        if (right < n && compareGames(arr[right], arr[largest], stats) > 0)
            largest = right;

        if (largest != i) {
            swap(arr, i, largest, stats);
            heapify(arr, n, largest, stats);
        }
    }

    private static int compareGames(Game a, Game b, HeapSortStats stats) {
        stats.comparacoes++;
        if (a.getEstimatedOwners() != b.getEstimatedOwners())
            return Integer.compare(a.getEstimatedOwners(), b.getEstimatedOwners());
        else
            return Integer.compare(a.getId(), b.getId());
    }

    private static void swap(Game[] arr, int i, int j, HeapSortStats stats) {
        Game temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        stats.movimentacoes += 3;
    }

    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String csvPath = "/tmp/games.csv";
        List<Game> gamesList = readFromCSV(csvPath);

        List<Game> selectedGames = new ArrayList<>();
        String line;

        while (sc.hasNextLine() && !(line = sc.nextLine()).equalsIgnoreCase("FIM")) {
            try {
                int id = Integer.parseInt(line.trim());
                Game g = findById(gamesList, id);
                if (g != null) selectedGames.add(g);
            } catch (NumberFormatException e) {}
        }

        Game[] gamesArray = selectedGames.toArray(new Game[0]);
        HeapSortStats stats = new HeapSortStats();

        long startTime = System.currentTimeMillis();
        heapSort(gamesArray, stats);
        long endTime = System.currentTimeMillis();

        for (Game game : gamesArray) game.print();

        System.err.println("Tempo de execução: " + (endTime - startTime) + "ms");
        System.err.println("Comparações: " + stats.comparacoes);
        System.err.println("Movimentações: " + stats.movimentacoes);

        sc.close();
    }
}
