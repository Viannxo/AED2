#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <locale.h>

#define MAX_STR 200
#define MAX_LIST 50
#define MAX_LINE 2048
#define MAX_GAMES 10000

// game
typedef struct {
    int id;
    char name[MAX_STR];
    char releaseDate[11];
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

// funções auxiliares

// substituidor de espaços e aspas
void trim(char *str) {
    if (str == NULL) return;
    int len = strlen(str);
    while (len > 0 && isspace((unsigned char)str[len - 1])) str[--len] = '\0';
    while (*str && isspace((unsigned char)*str)) memmove(str, str + 1, strlen(str));
    if (str[0] == '"' && str[strlen(str) - 1] == '"') {
        memmove(str, str + 1, strlen(str));
        str[strlen(str) - 1] = '\0';
    }
}

// decimal para 0.0, 19.9, 19.99
float parsePrice(const char *s) {
    if (s == NULL || strlen(s) == 0 || strstr(s, "Free") != NULL)
        return 0.0f;
    char temp[32];
    strcpy(temp, s);
    for (int i = 0; temp[i]; i++) if (temp[i] == ',') temp[i] = '.';
    return atof(temp);
}

// num para int
int parseInt(const char *s) {
    if (s == NULL || strlen(s) == 0) return 0;
    return atoi(s);
}

// datas nos formatos (MMM dd, yyyy), (MMM yyyy) ou (yyyy)
void normalizeReleaseDate(const char *src, char *dest) {
    if (src == NULL || strlen(src) == 0) {
        strcpy(dest, "01/01/0001");
        return;
    }
    char s[64];
    strcpy(s, src);
    trim(s);

    // ano
    if (strlen(s) == 4 && isdigit(s[0])) {
        sprintf(dest, "01/01/%s", s);
        return;
    }

    // formato padrão
    strcpy(dest, "01/01/2000");
}

//divisor de string
int parseArrayField(const char *text, char arr[MAX_LIST][MAX_STR]) {
    if (text == NULL || strlen(text) == 0) return 0;

    char temp[MAX_LINE];
    strcpy(temp, text);
    for (int i = 0; temp[i]; i++) {
        if (temp[i] == '[' || temp[i] == ']' || temp[i] == '\'')
            temp[i] = ' ';
    }

    int count = 0;
    char *token = strtok(temp, ",");
    while (token && count < MAX_LIST) {
        trim(token);

        // Correções especiais
        if (strcasecmp(token, "Shoot Em Up") == 0)
            strcpy(arr[count], "Shoot 'Em Up");
        else if (strcasecmp(token, "Beat Em Up") == 0)
            strcpy(arr[count], "Beat 'em up");
        else if (strcasecmp(token, "1990s") == 0)
            strcpy(arr[count], "1990's");
        else
            strcpy(arr[count], token);

        count++;
        token = strtok(NULL, ",");
    }
    return count;
}

// divisor de campos
int splitByComma(const char *text, char arr[MAX_LIST][MAX_STR]) {
    if (text == NULL || strlen(text) == 0) return 0;
    char temp[MAX_LINE];
    strcpy(temp, text);

    int count = 0;
    char *token = strtok(temp, ",");
    while (token && count < MAX_LIST) {
        trim(token);
        strcpy(arr[count++], token);
        token = strtok(NULL, ",");
    }
    return count;
}

// formato do preço (0.0, 19.9, 19.99)
void formatPrice(float value, char *dest) {
    sprintf(dest, "%.2f", value);
    int len = strlen(dest);
    if (strcmp(dest + len - 3, ".00") == 0) strcpy(dest, "0.0");
    else if (dest[len - 1] == '0') sprintf(dest, "%.1f", value);
}

// divisor de linhas
int splitCSVLine(const char *line, char fields[20][MAX_LINE]) {
    int fieldIndex = 0;
    int inQuotes = 0;
    const char *ptr = line;
    char *out = fields[fieldIndex];

    while (*ptr) {
        if (*ptr == '"') {
            inQuotes = !inQuotes;
        } else if (*ptr == ',' && !inQuotes) {
            *out = '\0';
            trim(fields[fieldIndex]);
            fieldIndex++;
            out = fields[fieldIndex];
        } else {
            *out++ = *ptr;
        }
        ptr++;
    }
    *out = '\0';
    trim(fields[fieldIndex]);
    return fieldIndex + 1;
}

// funções games

void printGame(const Game *g) {
    char priceStr[16];
    formatPrice(g->price, priceStr);

    printf("=> %d ## %s ## %s ## %d ## %s ## [", g->id, g->name, g->releaseDate,
           g->estimatedOwners, priceStr);

    for (int i = 0; i < g->numSupportedLanguages; i++) {
        printf("%s%s", g->supportedLanguages[i], (i < g->numSupportedLanguages - 1) ? ", " : "");
    }

    printf("] ## %d ## %.1f ## %d ## [", g->metacriticScore, g->userScore, g->achievements);

    for (int i = 0; i < g->numPublishers; i++)
        printf("%s%s", g->publishers[i], (i < g->numPublishers - 1) ? ", " : "");
    printf("] ## [");

    for (int i = 0; i < g->numDevelopers; i++)
        printf("%s%s", g->developers[i], (i < g->numDevelopers - 1) ? ", " : "");
    printf("] ## [");

    for (int i = 0; i < g->numCategories; i++)
        printf("%s%s", g->categories[i], (i < g->numCategories - 1) ? ", " : "");
    printf("] ## [");

    for (int i = 0; i < g->numGenres; i++)
        printf("%s%s", g->genres[i], (i < g->numGenres - 1) ? ", " : "");
    printf("] ## [");

    for (int i = 0; i < g->numTags; i++)
        printf("%s%s", g->tags[i], (i < g->numTags - 1) ? ", " : "");
    printf("] ##\n");
}

// funções de leitura e busca

int readFromCSV(const char *filePath, Game *games) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        perror("Erro ao abrir CSV");
        return 0;
    }

    char line[MAX_LINE];
    fgets(line, sizeof(line), file); // cabeçalho

    int count = 0;
    while (fgets(line, sizeof(line), file) && count < MAX_GAMES) {
        char fields[20][MAX_LINE];
        int n = splitCSVLine(line, fields);
        if (n < 14) continue;

        Game g;
        memset(&g, 0, sizeof(Game));

        g.id = parseInt(fields[0]);
        strcpy(g.name, fields[1]);
        normalizeReleaseDate(fields[2], g.releaseDate);
        g.estimatedOwners = parseInt(fields[3]);
        g.price = parsePrice(fields[4]);
        g.numSupportedLanguages = parseArrayField(fields[5], g.supportedLanguages);
        g.metacriticScore = parseInt(fields[6]);
        g.userScore = atof(fields[7]);
        g.achievements = parseInt(fields[8]);
        g.numPublishers = splitByComma(fields[9], g.publishers);
        g.numDevelopers = splitByComma(fields[10], g.developers);
        g.numCategories = parseArrayField(fields[11], g.categories);
        g.numGenres = parseArrayField(fields[12], g.genres);
        g.numTags = parseArrayField(fields[13], g.tags);

        games[count++] = g;
    }

    fclose(file);
    return count;
}

// Busca por ID
Game *findById(Game *list, int size, int id) {
    for (int i = 0; i < size; i++)
        if (list[i].id == id) return &list[i];
    return NULL;
}

// main

int main() {
    Game games[MAX_GAMES];
    int total = readFromCSV("/tmp/games.csv", games);

    char input[64];
    while (1) {
        if (!fgets(input, sizeof(input), stdin)) break;
        trim(input);
        if (strcmp(input, "FIM") == 0) break;

        int id = atoi(input);
        Game *g = findById(games, total, id);
        if (g) printGame(g);
    }

    return 0;
}
