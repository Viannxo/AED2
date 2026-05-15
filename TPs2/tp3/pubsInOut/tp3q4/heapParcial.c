#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

// --- Estruturas ---
typedef struct Data {
    int ano, mes, dia;
} Data;

typedef struct Hora {
    int hora, minuto;
} Hora;

typedef struct Restaurante {
    int id;
    char nome[150];
    char cidade[150];
    int capacidade;
    double avaliacao;
    char tiposCozinha[300];
    int faixaPreco;
    Data dataAbertura;
    Hora abertura;
    Hora fechamento;
    bool aberto;
} Restaurante;

typedef struct ColecaoRestaurantes {
    Restaurante array[2000];
    int n;
} ColecaoRestaurantes;

// --- Variáveis Globais ---
int comparacoes = 0;
int movimentacoes = 0;

// --- Comparação (Data -> Nome) ---
int compare(Restaurante *a, Restaurante *b) {
    comparacoes++;
    if (a->dataAbertura.ano != b->dataAbertura.ano) {
        return a->dataAbertura.ano - b->dataAbertura.ano;
    }
    if (a->dataAbertura.mes != b->dataAbertura.mes) {
        return a->dataAbertura.mes - b->dataAbertura.mes;
    }
    if (a->dataAbertura.dia != b->dataAbertura.dia) {
        return a->dataAbertura.dia - b->dataAbertura.dia;
    }
    return strcmp(a->nome, b->nome); 
}

void swap(Restaurante *a, Restaurante *b) {
    Restaurante temp = *a;
    *a = *b;
    *b = temp;
    movimentacoes += 3;
}

// --- Max-Heap ---
void reconstruir(Restaurante *array, int tamHeap, int i) {
    int maior = i;
    int esq = 2 * i + 1;
    int dir = 2 * i + 2;

    if (esq < tamHeap && compare(&array[esq], &array[maior]) > 0) maior = esq;
    if (dir < tamHeap && compare(&array[dir], &array[maior]) > 0) maior = dir;

    if (maior != i) {
        swap(&array[i], &array[maior]);
        reconstruir(array, tamHeap, maior);
    }
}

void heapsortParcial(ColecaoRestaurantes *col, int k) {
    int n = col->n;
    if (n == 0) return;
    if (k > n) k = n;

    // Constrói Max-Heap com k elementos
    for (int i = (k / 2) - 1; i >= 0; i--) {
        reconstruir(col->array, k, i);
    }

    // Filtra o resto
    for (int i = k; i < n; i++) {
        // Se o elemento fora do heap for menor que o maior elemento do heap (a raiz)
        if (compare(&col->array[i], &col->array[0]) < 0) {
            // TROCA em vez de sobrescrever. 
            // O que sai do top 10 vai para o final, e não é perdido!
            swap(&col->array[i], &col->array[0]);
            reconstruir(col->array, k, 0);
        }
    }

    // Ordena o Max-Heap para Ordem Crescente (apenas a parte K)
    for (int i = k - 1; i > 0; i--) {
        swap(&col->array[0], &col->array[i]);
        reconstruir(col->array, i, 0);
    }
    
    // ATENÇÃO: col->n = k; FOI REMOVIDO PARA MANTER TODOS OS ELEMENTOS!
}

// --- Parsing (Totalmente Manual, sem Macros) ---
Restaurante parse_restaurante(char *linha) {
    Restaurante r;
    char *ptr = linha;
    char temp[500];
    int pos = 0;

    // ID
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.id = atoi(temp);
    if (*ptr == ',') ptr++; pos = 0;

    // Nome
    while (*ptr != ',' && *ptr != '\0') r.nome[pos++] = *ptr++;
    r.nome[pos] = '\0';
    if (*ptr == ',') ptr++; pos = 0;

    // Cidade
    while (*ptr != ',' && *ptr != '\0') r.cidade[pos++] = *ptr++;
    r.cidade[pos] = '\0';
    if (*ptr == ',') ptr++; pos = 0;

    // Capacidade
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.capacidade = atoi(temp);
    if (*ptr == ',') ptr++; pos = 0;

    // Avaliacao
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.avaliacao = atof(temp);
    if (*ptr == ',') ptr++; pos = 0;

    // Tipos Cozinha
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    if (*ptr == ',') ptr++;
    
    r.tiposCozinha[0] = '[';
    int j = 1;
    for (int i = 0; temp[i] != '\0'; i++) {
        if (temp[i] == ';') r.tiposCozinha[j++] = ',';
        else r.tiposCozinha[j++] = temp[i];
    }
    r.tiposCozinha[j++] = ']';
    r.tiposCozinha[j] = '\0';
    pos = 0;

    // Faixa Preco
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.faixaPreco = pos; // Aproveitando o contator como tamanho
    if (*ptr == ',') ptr++; pos = 0;

    // Horario
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    sscanf(temp, "%d:%d-%d:%d", &r.abertura.hora, &r.abertura.minuto, &r.fechamento.hora, &r.fechamento.minuto);
    if (*ptr == ',') ptr++; pos = 0;

    // Data
    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    sscanf(temp, "%d-%d-%d", &r.dataAbertura.ano, &r.dataAbertura.mes, &r.dataAbertura.dia);
    if (*ptr == ',') ptr++; pos = 0;

    // Aberto
    while (*ptr != '\0' && *ptr != '\n' && *ptr != '\r') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.aberto = (strcmp(temp, "true") == 0);

    return r;
}

// --- Impressão ---
void imprimir(Restaurante *r) {
    char cifroes[15] = ""; 
    for (int i = 0; i < r->faixaPreco && i < 10; i++) strcat(cifroes, "$");

    printf("[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n",
        r->id, r->nome, r->cidade, r->capacidade, r->avaliacao, r->tiposCozinha, cifroes,
        r->abertura.hora, r->abertura.minuto, r->fechamento.hora, r->fechamento.minuto,
        r->dataAbertura.dia, r->dataAbertura.mes, r->dataAbertura.ano, r->aberto ? "true" : "false");
}

int main() {
    ColecaoRestaurantes base;
    base.n = 0;

    FILE *f = fopen("restaurantes.csv", "r");
    if (f == NULL) f = fopen("/tmp/restaurantes.csv", "r");

    if (f != NULL) {
        char linha[1024];
        fgets(linha, sizeof(linha), f);
        while (fgets(linha, sizeof(linha), f) && base.n < 2000) {
            base.array[base.n++] = parse_restaurante(linha);
        }
        fclose(f);
    }

    ColecaoRestaurantes selecionados;
    selecionados.n = 0;
    char entrada[50];

    while (scanf("%s", entrada) == 1) {
        if (strcmp(entrada, "FIM") == 0 || strcmp(entrada, "-1") == 0) break;
        int idBusca = atoi(entrada);
        for (int i = 0; i < base.n; i++) {
            if (base.array[i].id == idBusca) {
                selecionados.array[selecionados.n++] = base.array[i];
                break;
            }
        }
    }

    clock_t inicio = clock();
    heapsortParcial(&selecionados, 10);
    clock_t fim = clock();

    // Modificação: agora imprime todos os elementos lidos.
    // Os 10 primeiros estarão em ordem. Do índice 10 em diante estarão não ordenados.
    for (int i = 0; i < selecionados.n; i++) {
        imprimir(&selecionados.array[i]);
    }

    FILE *log = fopen("844387_heapsort_parcial.txt", "w");
    if (log != NULL) {
        double tempo = ((double)(fim - inicio) / CLOCKS_PER_SEC) * 1000.0;
        fprintf(log, "844387\t%d\t%d\t%.2lfms", comparacoes, movimentacoes, tempo);
        fclose(log);
    }

    return 0;
}