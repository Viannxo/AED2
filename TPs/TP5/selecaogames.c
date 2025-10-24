#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAX_STR 1024 
#define MAX_LIST 50
#define MAX_LINE 8192
#define MAX_GAMES 5000
#define MAX_FIELDS 64

typedef struct {
    int id;
    char name[MAX_STR];
    char releaseDate[11]; // dd/mm/yyyy
    int estimatedOwners;
    float price;
    char supportedLanguages[MAX_LIST][MAX_STR];
    int numSupportedLanguages;
    int metacriticScore;
    float userScore;
    int achievements;
    char publishers[MAX_LIST][MAX_STR];
    int numPublishers;
    char developers[MAX_LIST][MAX_STR];
    int numDevelopers;
    char categories[MAX_LIST][MAX_STR];
    int numCategories;
    char genres[MAX_LIST][MAX_STR];
    int numGenres;
    char tags[MAX_LIST][MAX_STR];
    int numTags;
} Game;

//funcoes aux

static char *trim(char *s) {
    if (!s) return s;
    while (isspace((unsigned char)*s)) s++;
    if (*s == 0) return s;
    char *end = s + strlen(s) - 1;
    while (end > s && isspace((unsigned char)*end)) *end-- = '\0';
    return s;
}

//remove [ ] e '' e divide por vírgulas
static void parse_array_field_into(char *s, char dest[][MAX_STR], int *count, int max_elems) {
    *count = 0;
    if (!s || strlen(s) == 0) return;
    
    // 1. Limpa (remove [, ], ') + copia
    char cleaned_s[MAX_STR];
    int k = 0;
    for (int i = 0; s[i] && k < MAX_STR-1; i++) {
        // Remove colchetes e aspas simples
        if (s[i] == '[' || s[i] == ']' || s[i] == '\'') continue; 
        cleaned_s[k++] = s[i];
    }
    cleaned_s[k] = '\0';
    
    // 2. Tokeniza o campo limpo
    char *token = strtok(cleaned_s, ",");
    while (token && *count < max_elems) {
        char t[MAX_STR];
        strncpy(t, token, MAX_STR-1);
        t[MAX_STR-1] = '\0';
        trim(t); // Aplica o trim NO ITEM INDIVIDUAL
        
        // Aplica substituições especiais
        if (strcasecmp(t, "Shoot Em Up") == 0) strcpy(t, "Shoot 'Em Up");
        else if (strcasecmp(t, "Beat Em Up") == 0) strcpy(t, "Beat 'em up");
        else if (strcasecmp(t, "1990s") == 0) strcpy(t, "1990's");
        
        strncpy(dest[*count], t, MAX_STR-1);
        dest[*count][MAX_STR-1] = '\0';
        (*count)++;
        token = strtok(NULL, ",");
    }
}

/* Divide campos separados por vírgula */
static void split_by_comma_into(char *s, char dest[][MAX_STR], int *count, int max_elems) {
    *count = 0;
    if (!s || strlen(s) == 0) return;
    char tmp[MAX_STR];
    strncpy(tmp, s, MAX_STR-1);
    tmp[MAX_STR-1] = '\0';
    char *token = strtok(tmp, ",");
    while (token && *count < max_elems) {
        trim(token);
        strncpy(dest[*count], token, MAX_STR-1);
        dest[*count][MAX_STR-1] = '\0';
        (*count)++;
        token = strtok(NULL, ",");
    }
}

/* Converte preço para float */
static float parse_price(const char *s) {
    if (!s) return 0.0f;
    char tmp[MAX_STR];
    strncpy(tmp, s, MAX_STR-1);
    tmp[MAX_STR-1] = '\0';
    trim(tmp);
    if (strcasecmp(tmp, "Free to Play") == 0 || strlen(tmp) == 0) return 0.0f;
    for (char *p = tmp; *p; ++p) if (*p == ',') *p = '.';
    char filtered[MAX_STR]; int j = 0;
    for (int i = 0; tmp[i] && j < MAX_STR-1; ++i)
        if (isdigit((unsigned char)tmp[i]) || tmp[i] == '.' || tmp[i] == '-')
            filtered[j++] = tmp[i];
    filtered[j] = '\0';
    if (strlen(filtered) == 0) return 0.0f;
    return strtof(filtered, NULL);
}

/* Extrai dígitos do campo de proprietários */
static int parse_estimated_owners(const char *s) {
    if (!s) return 0;
    char tmp[MAX_STR]; int j = 0;
    for (int i = 0; s[i] && j < MAX_STR-1; ++i)
        if (isdigit((unsigned char)s[i])) tmp[j++] = s[i];
    tmp[j] = '\0';
    return j ? atoi(tmp) : 0;
}

/* Converte score (tbd -> -1.0) */
static float parse_user_score(const char *s) {
    if (!s) return -1.0f;
    char tmp[MAX_STR];
    strncpy(tmp, s, MAX_STR-1);
    tmp[MAX_STR-1] = '\0';
    trim(tmp);
    if (strcasecmp(tmp, "tbd") == 0 || strlen(tmp) == 0) return -1.0f;
    for (char *p = tmp; *p; ++p) if (*p == ',') *p = '.';
    return strtof(tmp, NULL);
}

// Mês abreviado -> número 
static int month_str_to_int(const char *m) {
    if (!m) return -1;
    const char *months = "JanFebMarAprMayJunJulAugSepOctNovDec";
    for (int i = 0; i < 12; i++)
        if (strncasecmp(m, months + i*3, 3) == 0) return i + 1;
    return -1;
}

//Normaliza datas (vários formatos -> dd/mm/yyyy) 
static void normalize_release_date(char *src, char out[11]) {
    if (!src || strlen(src) == 0) { strcpy(out, "01/01/0001"); return; }
    char s[MAX_STR];
    strncpy(s, src, MAX_STR-1);
    s[MAX_STR-1] = '\0';
    trim(s);
    if (s[0] == '"' && s[strlen(s)-1] == '"') {
        memmove(s, s+1, strlen(s)-2);
        s[strlen(s)-2] = '\0';
        trim(s);
    }
    int month=-1, day=-1, year=-1; char mon[16];
    if (sscanf(s, "%3s %d, %d", mon, &day, &year) == 3) {
        month = month_str_to_int(mon);
        if (month>0 && day>0 && year>0) { snprintf(out, 11, "%02d/%02d/%04d", day, month, year); return; }
    }
    if (sscanf(s, "%3s %d", mon, &year) == 2) {
        month = month_str_to_int(mon);
        if (month>0 && year>0) { snprintf(out, 11, "01/%02d/%04d", month, year); return; }
    }
    if (sscanf(s, "%d", &year) == 1 && strlen(s) == 4)
        snprintf(out, 11, "01/01/%04d", year);
    else strcpy(out, "01/01/2000");
}

//formata preço para saída
static void format_price_str(char *buf, size_t bufsize, float value) {
    char tmp[64];
    snprintf(tmp, sizeof(tmp), "%.2f", value);
    size_t L = strlen(tmp);
    if (L >= 3 && strcmp(tmp + L - 3, ".00") == 0)
        snprintf(buf, bufsize, "0.0");
    else if (L >= 2 && tmp[L-1] == '0')
        snprintf(buf, bufsize, "%.1f", value);
    else snprintf(buf, bufsize, "%.2f", value);
}

//imprime arrays
static void print_array_brackets(char arr[][MAX_STR], int n) {
    printf("[");
    for (int i = 0; i < n; ++i) {
        if (i) printf(", "); 
        printf("%s", arr[i]);
    }
    printf("]");
}
static void print_langs(char arr[][MAX_STR], int n) {//saida apresentava erro nos idiomas então fiz um print para os idiomas
    printf("[");
    for (int i = 0; i < n; ++i) {
        if (i) printf(","); 
        printf("%s", arr[i]);
    }
    printf("]");
}

//divide linhas
static int splitCSVLine(const char *line, char fields[][MAX_STR], int max_fields) {
    int fi = 0, inQuotes = 0, ci = 0;
    char cur[MAX_STR];
    for (int i = 0; line[i] && fi < max_fields; ++i) {
        char c = line[i];
        if (c == '"') inQuotes = !inQuotes;
        else if (c == ',' && !inQuotes) {
            cur[ci] = '\0';
            strncpy(fields[fi++], trim(cur), MAX_STR-1);
            ci = 0;
        } else if (ci < MAX_STR-1) cur[ci++] = c;
    }
    cur[ci] = '\0';
    strncpy(fields[fi++], trim(cur), MAX_STR-1);
    return fi;
}

//funções 

static void game_init(Game *g) {
    memset(g, 0, sizeof(Game));
    g->id = -1;
    strcpy(g->releaseDate, "01/01/0001");
    g->metacriticScore = -1;
    g->userScore = -1.0f;
}

static void game_set_id(Game *g, const char *s) { g->id = s ? atoi(s) : -1; }
static void game_set_name(Game *g, const char *s) { strncpy(g->name, s ? trim((char*)s) : "", MAX_STR-1); }
static void game_set_release_date(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); normalize_release_date(tmp, g->releaseDate); }
static void game_set_estimated_owners(Game *g, const char *s) { g->estimatedOwners = parse_estimated_owners(s); }
static void game_set_price(Game *g, const char *s) { g->price = parse_price(s); }
static void game_set_supported_languages(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); parse_array_field_into(tmp, g->supportedLanguages, &g->numSupportedLanguages, MAX_LIST); }
static void game_set_metacritic(Game *g, const char *s) { g->metacriticScore = (s && strlen(s)) ? atoi(s) : -1; }
static void game_set_user_score(Game *g, const char *s) { g->userScore = parse_user_score(s); }
static void game_set_achievements(Game *g, const char *s) { g->achievements = (s && strlen(s)) ? atoi(s) : 0; }
static void game_set_publishers(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); split_by_comma_into(tmp, g->publishers, &g->numPublishers, MAX_LIST); }
static void game_set_developers(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); split_by_comma_into(tmp, g->developers, &g->numDevelopers, MAX_LIST); }
static void game_set_categories(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); parse_array_field_into(tmp, g->categories, &g->numCategories, MAX_LIST); }
static void game_set_genres(Game *g, const char *s) { char tmp[MAX_STR]; strncpy(tmp, s ? s : "", MAX_STR-1); parse_array_field_into(tmp, g->genres, &g->numGenres, MAX_LIST); }
static void game_set_tags(Game *g, const char *s) { 
    char tmp[MAX_STR]; 
    strncpy(tmp, s ? s : "", MAX_STR-1); 
    tmp[MAX_STR-1] = '\0';
    parse_array_field_into(tmp, g->tags, &g->numTags, MAX_LIST); 
}

static void game_print(const Game *g) {
    char pricebuf[64];
    format_price_str(pricebuf, sizeof(pricebuf), g->price);
    printf("=> %d ## %s ## %s ## %d ## %s ## ", g->id, g->name, g->releaseDate, g->estimatedOwners, pricebuf);
    print_langs(g->supportedLanguages, g->numSupportedLanguages);
    printf(" ## %d ## ", g->metacriticScore);
    if (g->userScore < 0.0f) {
        printf("-1.0 ## ");
    } else {
        printf("%.1f ## ", g->userScore);
    }
    printf("%d ## ", g->achievements);
    print_array_brackets(g->publishers, g->numPublishers); printf(" ## ");
    print_array_brackets(g->developers, g->numDevelopers); printf(" ## ");
    print_array_brackets(g->categories, g->numCategories); printf(" ## ");
    print_array_brackets(g->genres, g->numGenres); printf(" ## ");
    print_array_brackets(g->tags, g->numTags); printf(" ##\n");
}

//leitura do CSV

static int readFromCSV(const char *filePath, Game *games, int maxGames) {
    FILE *f = fopen(filePath, "r");
    if (!f) return 0;
    char line[MAX_LINE];
    if (!fgets(line, sizeof(line), f)) { fclose(f); return 0; }
    int count = 0;
    while (fgets(line, sizeof(line), f) && count < maxGames) {
        char *nl = strchr(line, '\n'); if (nl) *nl = '\0';
        char fields[MAX_FIELDS][MAX_STR] = {{0}};
        int nfields = splitCSVLine(line, fields, MAX_FIELDS);
        if (nfields < 14) continue;
        Game g; game_init(&g);
        game_set_id(&g, fields[0]);
        game_set_name(&g, fields[1]);
        game_set_release_date(&g, fields[2]);
        game_set_estimated_owners(&g, fields[3]);
        game_set_price(&g, fields[4]);
        game_set_supported_languages(&g, fields[5]);
        game_set_metacritic(&g, fields[6]);
        game_set_user_score(&g, fields[7]);
        game_set_achievements(&g, fields[8]);
        game_set_publishers(&g, fields[9]);
        game_set_developers(&g, fields[10]);
        game_set_categories(&g, fields[11]);
        game_set_genres(&g, fields[12]);
        game_set_tags(&g, fields[13]);
        games[count++] = g;
    }
    fclose(f);
    return count;
}

//controle e busca
static Game *findById(Game *games, int n, int id) {
    for (int i = 0; i < n; ++i)
        if (games[i].id == id) return &games[i];
    return NULL;
}

static int Parada(const char *line) {
    return (strlen(line) == 3 && strcmp(line, "FIM") == 0);
}

static void swap(Game *a, Game *b) {
    Game temp = *a;
    *a = *b;
    *b = temp;
}

//selection sort por name
static void selectionSort(Game arr[], int n) {
    int i, j, min_idx;

    // subvetor ++
    for (i = 0; i < n - 1; i++) {
        // index do menor não ordenado
        min_idx = i;
        for (j = i + 1; j < n; j++) {
            if (strcmp(arr[j].name, arr[min_idx].name) < 0) {
                min_idx = j;
            }
        }

        if (min_idx != i) {
            swap(&arr[i], &arr[min_idx]);
        }
    }
}

//main

int main(void) {
    const char *csvPath = "/tmp/games.csv";
    static Game games[MAX_GAMES]; 
    int n = readFromCSV(csvPath, games, MAX_GAMES);

    // Array aux
    static Game gamesToPrint[MAX_GAMES]; 
    int nToPrint = 0;

    char input[MAX_STR];
    // leitura de IDs e selecionando registros
    while (fgets(input, sizeof(input), stdin)) {
        char *nl = strchr(input, '\n'); if (nl) *nl = '\0';
        trim(input);
        if (Parada(input)) break;
        if (strlen(input) == 0) continue;

        char *endptr;
        long idl = strtol(input, &endptr, 10);
        
        // valida ID
        if (*endptr == '\0') { 
            Game *g = findById(games, n, (int)idl);
            
            // Se encontrado, adiciona ao array de jogos a imprimir
            if (g && nToPrint < MAX_GAMES) {
                gamesToPrint[nToPrint++] = *g; 
            }
        }
    }

    // OrdenaçãoSelection Sort por name
    selectionSort(gamesToPrint, nToPrint);

    //print ordenado
    for (int i = 0; i < nToPrint; i++) {
        game_print(&gamesToPrint[i]);
    }

    return 0;
}