#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>
#include <math.h>


#define MAX_STR 200
#define MAX_ARR 50

// games 
typedef struct Game {
    int id;
    char name[MAX_STR];
    char releaseDate[11]; // dd/MM/yyyy
    int estimatedOwners;
    float price;
    char supportedLanguages[MAX_ARR][MAX_STR];
    int numSupportedLanguages;
    int metacriticScore;
    float userScore;
    int achievements;
    char publishers[MAX_ARR][MAX_STR];
    int numPublishers;
    char developers[MAX_ARR][MAX_STR];
    int numDevelopers;
    char categories[MAX_ARR][MAX_STR];
    int numCategories;
    char genres[MAX_ARR][MAX_STR];
    int numGenres;
    char tags[MAX_ARR][MAX_STR];
    int numTags;
} Game;


// Construtor do Game 
Game* newGame(char** fields); // Cria e popula Game
void printGame(Game* g); // Imprime Game

//funções auxiliares

char* normalizeReleaseDate(const char* s) {
    static char result[11]; 
    if (s == NULL || strlen(s) == 0) { strcpy(result, "01/01/0001"); return result; }
    char temp[MAX_STR]; strcpy(temp, s);
    struct tm tm_full = {0};
    if (strptime(temp, "%b %d, %Y", &tm_full) != NULL) { strftime(result, 11, "%d/%m/%Y", &tm_full); return result; }
    struct tm tm_month_year = {0};
    if (strptime(temp, "%b %Y", &tm_month_year) != NULL) { strftime(result, 11, "01/%m/%Y", &tm_month_year); return result; }
    if (strlen(temp) == 4) { strcpy(result, "01/01/"); strcat(result, temp); return result; }
    strcpy(result, "01/01/2000"); return result;
}

char* formatPrice(float value) {
    static char formatted[10];
    if (fabs(value) < 0.001) { strcpy(formatted, "0.0"); return formatted; }
    sprintf(formatted, "%.2f", value);
    int len = strlen(formatted);
    if (len > 0 && formatted[len - 1] == '0' && formatted[len - 2] != '.') {
        sprintf(formatted, "%.1f", value);
        return formatted;
    }
    return formatted;
}

int parseArrayField(const char* text, char array[MAX_ARR][MAX_STR]) {
    if (text == NULL || strlen(text) == 0) return 0;
    char temp[MAX_STR]; strcpy(temp, text);
    char* clean_text = strtok(temp, "[]");
    if (clean_text == NULL) return 0;
    int count = 0;
    char* token = strtok(clean_text, ",");
    while (token != NULL && count < MAX_ARR) {
        char item[MAX_STR] = "";
        sscanf(token, " '%[^']", item); 
        if (item[0] == '\0') sscanf(token, " %[^,]", item); 
        char *trimmed = item;
        while(isspace((unsigned char)*trimmed)) trimmed++;
        char *end = trimmed + strlen(trimmed) - 1;
        while(end >= trimmed && isspace((unsigned char)*end)) end--;
        *(end + 1) = '\0';
        if (strlen(trimmed) > 0) {
            if (strcasecmp(trimmed, "Shoot Em Up") == 0) strcpy(trimmed, "Shoot 'Em Up");
            else if (strcasecmp(trimmed, "Beat Em Up") == 0) strcpy(trimmed, "Beat 'em up");
            else if (strcasecmp(trimmed, "1990s") == 0) strcpy(trimmed, "1990's");
            strcpy(array[count++], trimmed);
        }
        token = strtok(NULL, ",");
    }
    return count;
}

int splitByComma(const char* text, char array[MAX_ARR][MAX_STR]) {
    if (text == NULL || strlen(text) == 0) return 0;
    char temp[MAX_STR]; strcpy(temp, text);
    int count = 0;
    char* token = strtok(temp, ",");
    while (token != NULL && count < MAX_ARR) {
        char item[MAX_STR] = "";
        sscanf(token, " %[^,]", item);
        char *trimmed = item;
        while(isspace((unsigned char)*trimmed)) trimmed++;
        char *end = trimmed + strlen(trimmed) - 1;
        while(end >= trimmed && isspace((unsigned char)*end)) end--;
        *(end + 1) = '\0';
        if (strlen(trimmed) > 0) {
            strcpy(array[count++], trimmed);
        }
        token = strtok(NULL, ",");
    }
    return count;
}

void printArray(char array[MAX_ARR][MAX_STR], int n) {
    printf("[");
    for (int i = 0; i < n; i++) {
        printf("%s", array[i]);
        if (i < n - 1) printf(", ");
    }
    printf("]");
}

Game* newGame(char** fields) {
    Game* g = (Game*)malloc(sizeof(Game));
    if (g == NULL) return NULL;

    g->id = (fields[0] != NULL) ? atoi(fields[0]) : -1;
    if (fields[1] != NULL) strncpy(g->name, fields[1], MAX_STR - 1); else g->name[0] = '\0';
    if (fields[2] != NULL) strcpy(g->releaseDate, normalizeReleaseDate(fields[2])); else strcpy(g->releaseDate, "01/01/0001");
    if (fields[3] != NULL) { char temp[MAX_STR]; int j = 0; for (int i = 0; fields[3][i] != '\0'; i++) { if (isdigit(fields[3][i])) temp[j++] = fields[3][i]; } temp[j] = '\0'; g->estimatedOwners = (j > 0) ? atoi(temp) : 0; } else { g->estimatedOwners = 0; }
    if (fields[4] != NULL) { if (strcasecmp(fields[4], "Free to Play") == 0) g->price = 0.0f; else { char temp[MAX_STR]; strcpy(temp, fields[4]); for(int i = 0; temp[i] != '\0'; i++) if (temp[i] == ',') temp[i] = '.'; g->price = atof(temp); } } else { g->price = 0.0f; }
    g->numSupportedLanguages = parseArrayField(fields[5], g->supportedLanguages);
    if (fields[6] != NULL && strlen(fields[6]) > 0) g->metacriticScore = atoi(fields[6]); else g->metacriticScore = -1;
    if (fields[7] != NULL && strcasecmp(fields[7], "tbd") != 0 && strlen(fields[7]) > 0) { char temp[MAX_STR]; strcpy(temp, fields[7]); for(int i = 0; temp[i] != '\0'; i++) if (temp[i] == ',') temp[i] = '.'; g->userScore = atof(temp); } else { g->userScore = -1.0f; }
    if (fields[8] != NULL && strlen(fields[8]) > 0) g->achievements = atoi(fields[8]); else g->achievements = 0;
    g->numPublishers = splitByComma(fields[9], g->publishers);
    g->numDevelopers = splitByComma(fields[10], g->developers);
    g->numCategories = parseArrayField(fields[11], g->categories);
    g->numGenres = parseArrayField(fields[12], g->genres);
    g->numTags = parseArrayField(fields[13], g->tags);

    return g;
}

void printGame(Game* g) {
    if (g == NULL) return;
    printf("=> %d ## %s ## %s ## %d ## %s ## ", 
           g->id, g->name, g->releaseDate, g->estimatedOwners, formatPrice(g->price));
    printArray(g->supportedLanguages, g->numSupportedLanguages);
    printf(" ## %d ## %.1f ## %d ## ", g->metacriticScore, g->userScore, g->achievements);
    printArray(g->publishers, g->numPublishers); printf(" ## ");
    printArray(g->developers, g->numDevelopers); printf(" ## ");
    printArray(g->categories, g->numCategories); printf(" ## ");
    printArray(g->genres, g->numGenres); printf(" ## ");
    printArray(g->tags, g->numTags); printf(" ##\n");
}

// encadeamento

// Estrutura de Nó
typedef struct No {
    Game* elemento; // Ponteiro para o registro Game
    struct No* prox;
} No;

// Construtor do Nó
No* newNo(Game* game, No* prox) {
    No* novo = (No*)malloc(sizeof(No));
    if (novo == NULL) {
        perror("Erro ao alocar No");
        exit(EXIT_FAILURE);
    }
    novo->elemento = game;
    novo->prox = prox;
    return novo;
}

// lista 
typedef struct ListaGames {
    No* primeiro; // Sentinela
    No* ultimo;
    int tamanho;
} ListaGames;

// Construtor da Lista
ListaGames* newListaGames() {
    ListaGames* lista = (ListaGames*)malloc(sizeof(ListaGames));
    if (lista == NULL) {
        perror("Erro ao alocar ListaGames");
        exit(EXIT_FAILURE);
    }
    //sentinela
    Game* senti= (Game*)malloc(sizeof(Game)); 
    lista->primeiro = newNo(senti, NULL);
    lista->ultimo = lista->primeiro;
    lista->tamanho = 0;
    return lista;
}

// Metodo aux
No* getNo(ListaGames* lista, int pos) {
    No* i = lista->primeiro;
    for (int j = 0; j <= pos; j++, i = i->prox);
    return i;
}

// --- Metodos da Lista (Alocacao Flexivel) ---

// Inserir no inicio
void inserirInicio(ListaGames* lista, Game* game) {
    No* novo = newNo(game, lista->primeiro->prox);
    lista->primeiro->prox = novo;
    if (lista->primeiro == lista->ultimo) {
        lista->ultimo = novo;
    }
    lista->tamanho++;
}

// Inserir no fim
void inserirFim(ListaGames* lista, Game* game) {
    lista->ultimo->prox = newNo(game, NULL);
    lista->ultimo = lista->ultimo->prox;
    lista->tamanho++;
}

// Inserir
void inserir(ListaGames* lista, Game* game, int pos) {
    if (pos < 0 || pos > lista->tamanho) {
        fprintf(stderr, "Erro ao inserir: Posicao invalida!\n");
        return;
    }
    if (pos == 0) {
        inserirInicio(lista, game);
    } else if (pos == lista->tamanho) {
        inserirFim(lista, game);
    } else {
        No* ant = getNo(lista, pos - 1);
        No* novo = newNo(game, ant->prox);
        ant->prox = novo;
        lista->tamanho++;
    }
}

// Remover do inicio
Game* removerInicio(ListaGames* lista) {
    if (lista->primeiro == lista->ultimo) {
        fprintf(stderr, "Erro ao remover: Lista vazia!\n");
        return NULL;
    }
    No* temp = lista->primeiro->prox;
    lista->primeiro->prox = temp->prox;
    if (temp == lista->ultimo) {
        lista->ultimo = lista->primeiro;
    }
    Game* resp = temp->elemento;
    free(temp);
    lista->tamanho--;
    return resp;
}

// Remover do fim
Game* removerFim(ListaGames* lista) {
    if (lista->primeiro == lista->ultimo) {
        fprintf(stderr, "Erro ao remover: Lista vazia!\n");
        return NULL;
    }
    No* ant = getNo(lista, lista->tamanho - 1);
    Game* resp = ant->prox->elemento;
    free(ant->prox);
    lista->ultimo = ant;
    lista->ultimo->prox = NULL;
    lista->tamanho--;
    return resp;
}

// Remover
Game* remover(ListaGames* lista, int pos) {
    if (lista->primeiro == lista->ultimo || pos < 0 || pos >= lista->tamanho) {
        fprintf(stderr, "Erro ao remover: Posicao invalida ou lista vazia!\n");
        return NULL;
    }
    if (pos == 0) {
        return removerInicio(lista);
    } else if (pos == lista->tamanho - 1) {
        return removerFim(lista);
    } else {
        No* ant = getNo(lista, pos - 1);
        No* temp = ant->prox;
        ant->prox = temp->prox;
        Game* resp = temp->elemento;
        free(temp);
        lista->tamanho--;
        return resp;
    }
}

// Mostrar a lista
void mostrar(ListaGames* lista) {
    for (No* i = lista->primeiro->prox; i != NULL; i = i->prox) {
        printGame(i->elemento);
    }
}

// parada
bool isParada(const char* line) {
    return (strlen(line) == 3 && line[0] == 'F' && line[1] == 'I' && line[2] == 'M');
}

// leitura csv
char** splitCSVLine(const char* line);

void freeFields(char** fields);

Game** readFromCSV(const char* filePath, int* numGames);

Game* findById(Game** list, int numGames, int id);

char** splitCSVLine(const char* line) {
    char** fields = (char**)malloc(14 * sizeof(char*));
    for (int i = 0; i < 14; i++) {
        fields[i] = (char*)malloc(MAX_STR * sizeof(char));
        if (fields[i] == NULL) { for (int j = 0; j < i; j++) free(fields[j]); free(fields); return NULL; }
        fields[i][0] = '\0';
    }
    int fieldIndex = 0; bool inQuotes = false; int charIndex = 0;
    for (int i = 0; line[i] != '\0' && fieldIndex < 14; i++) {
        char c = line[i];
        if (c == '\"') { inQuotes = !inQuotes; } 
        else if (c == ',' && !inQuotes) { fields[fieldIndex][charIndex] = '\0'; fieldIndex++; charIndex = 0; } 
        else { if (charIndex < MAX_STR - 1) { fields[fieldIndex][charIndex++] = c; } }
    }
    if (fieldIndex < 14) fields[fieldIndex][charIndex] = '\0';
    return fields;
}

void freeFields(char** fields) {
    for (int i = 0; i < 14; i++) free(fields[i]);
    free(fields);
}

Game** readFromCSV(const char* filePath, int* numGames) {
    FILE* file = fopen(filePath, "r");
    if (file == NULL) { perror("Erro ao abrir arquivo"); *numGames = 0; return NULL; }

    Game** games = NULL; int capacity = 100; *numGames = 0;
    games = (Game**)malloc(capacity * sizeof(Game*));

    char line[1024 * 5];
    if (fgets(line, sizeof(line), file) == NULL) { fclose(file); free(games); *numGames = 0; return NULL; } // Pula o cabeçalho

    while (fgets(line, sizeof(line), file)) {
        line[strcspn(line, "\n")] = 0; 
        char** fields = splitCSVLine(line);
        if (fields == NULL) continue;
        
        Game* g = newGame(fields);
        freeFields(fields);
        
        if (g != NULL) {
            if (*numGames >= capacity) {
                capacity *= 2;
                games = (Game**)realloc(games, capacity * sizeof(Game*));
                if (games == NULL) { perror("Erro ao realocar array de games"); fclose(file); *numGames = 0; return NULL; }
            }
            games[(*numGames)++] = g;
        }
    }

    fclose(file);
    return games;
}

Game* findById(Game** list, int numGames, int id) {
    for (int i = 0; i < numGames; i++) {
        if (list[i]->id == id) return list[i];
    }
    return NULL;
}

// main
int main() {
    // csv
    int numTodosGames = 0;
    Game** todosGames = readFromCSV("/tmp/games.csv", &numTodosGames);
    ListaGames* lista = newListaGames();

    // Pre-carregamento
    char input[MAX_STR];
    while (true) {
        if (fgets(input, sizeof(input), stdin) == NULL) break;
        input[strcspn(input, "\n")] = 0;
        
        if (isParada(input)) break;

        int id = atoi(input);
        if (id > 0) {
            Game* g = findById(todosGames, numTodosGames, id);
            if (g != NULL) {
                // lista.inserirFim(g);
                inserirFim(lista, g);
            }
        }
    }

    // Inserção e Remoção
    if (fgets(input, sizeof(input), stdin) != NULL) {
        input[strcspn(input, "\n")] = 0;
        int nComandos = atoi(input);

        for (int i = 0; i < nComandos; i++) {
            if (fgets(input, sizeof(input), stdin) == NULL) break;
            input[strcspn(input, "\n")] = 0;

            char linhaComando[MAX_STR];
            strcpy(linhaComando, input);
            char* partes[3] = {NULL};
            int numPartes = 0;
            
            char* token = strtok(linhaComando, " ");
            while (token != NULL && numPartes < 3) {
                partes[numPartes++] = token;
                token = strtok(NULL, " ");
            }

            if (numPartes > 0) {
                char* comando = partes[0];
                Game* gameRemovido = NULL;

                // try/catch (ignora erros de posicao/id)
                if (strcmp(comando, "II") == 0 && numPartes >= 2) {
                    int idII = atoi(partes[1]);
                    Game* gII = findById(todosGames, numTodosGames, idII);
                    if (gII != NULL) inserirInicio(lista, gII);
                } else if (strcmp(comando, "IF") == 0 && numPartes >= 2) {
                    int idIF = atoi(partes[1]);
                    Game* gIF = findById(todosGames, numTodosGames, idIF);
                    if (gIF != NULL) inserirFim(lista, gIF);
                } else if (strcmp(comando, "I*") == 0 && numPartes >= 3) {
                    int posI = atoi(partes[1]);
                    int idI = atoi(partes[2]);
                    Game* gI = findById(todosGames, numTodosGames, idI);
                    if (gI != NULL) inserir(lista, gI, posI);
                } else if (strcmp(comando, "RI") == 0) {
                    gameRemovido = removerInicio(lista);
                } else if (strcmp(comando, "RF") == 0) {
                    gameRemovido = removerFim(lista);
                } else if (strcmp(comando, "R*") == 0 && numPartes >= 2) {
                    int posR = atoi(partes[1]);
                    gameRemovido = remover(lista, posR);
                }

                // Saída removidos
                if (gameRemovido != NULL) {
                    printf("(R) %s\n", gameRemovido->name);
                }
            }
        }
    }

    // Mostrar a lista remanescente
    mostrar(lista);

    // Limpeza de memória
    No* current = lista->primeiro;
    while (current != NULL) {
        No* next = current->prox;
        if (current == lista->primeiro && current->elemento != NULL) free(current->elemento);
        free(current);
        current = next;
    }
    free(lista);
    
    for (int i = 0; i < numTodosGames; i++) free(todosGames[i]);
    free(todosGames);

    return 0;
}