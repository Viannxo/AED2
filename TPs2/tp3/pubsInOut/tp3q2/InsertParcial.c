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
    char nome[100];
    char cidade[100];
    int capacidade;
    double avaliacao;
    char tiposCozinha[200];
    int faixaPreco;
    Data dataAbertura;
    Hora abertura;
    Hora fechamento;
    bool aberto;
} Restaurante;

typedef struct ColecaoRestaurantes {
    Restaurante array[1000];
    int n;
} ColecaoRestaurantes;

// --- Variáveis Globais ---
int comparacoes = 0;
int movimentacoes = 0;

// --- Comparação ---
// Ordena por:
// 1 -> Cidade
// 2 -> Nome
// 3 -> ID
int compare(Restaurante *a, Restaurante *b) {

    comparacoes++;

    int resp = strcmp(a->cidade, b->cidade);

    if (resp == 0) {
        resp = strcmp(a->nome, b->nome);

        if (resp == 0) {
            resp = a->id - b->id;
        }
    }

    return resp;
}

// --- Parsing ---
Restaurante parse_restaurante(char *linha) {

    Restaurante r;

    char *ptr = linha;
    char temp[300];
    int pos = 0;

    // ID
    while (*ptr != ',')
        temp[pos++] = *ptr++;

    temp[pos] = '\0';
    r.id = atoi(temp);

    ptr++;
    pos = 0;

    // Nome
    while (*ptr != ',')
        r.nome[pos++] = *ptr++;

    r.nome[pos] = '\0';

    ptr++;
    pos = 0;

    // Cidade
    while (*ptr != ',')
        r.cidade[pos++] = *ptr++;

    r.cidade[pos] = '\0';

    ptr++;
    pos = 0;

    // Capacidade
    while (*ptr != ',')
        temp[pos++] = *ptr++;

    temp[pos] = '\0';
    r.capacidade = atoi(temp);

    ptr++;
    pos = 0;

    // Avaliação
    while (*ptr != ',')
        temp[pos++] = *ptr++;

    temp[pos] = '\0';
    r.avaliacao = atof(temp);

    ptr++;
    pos = 0;

    // Tipos cozinha
    char tipos[200];

    while (*ptr != ',')
        tipos[pos++] = *ptr++;

    tipos[pos] = '\0';

    ptr++;
    pos = 0;

    r.tiposCozinha[0] = '[';

    int j = 1;

    for (int i = 0; tipos[i] != '\0'; i++) {

        if (tipos[i] == ';')
            r.tiposCozinha[j++] = ',';
        else
            r.tiposCozinha[j++] = tipos[i];
    }

    r.tiposCozinha[j++] = ']';
    r.tiposCozinha[j] = '\0';

    // Faixa de preço
    while (*ptr != ',')
        temp[pos++] = *ptr++;

    temp[pos] = '\0';

    r.faixaPreco = strlen(temp);

    ptr++;
    pos = 0;

    // Horário
    char horario[50];

    while (*ptr != ',')
        horario[pos++] = *ptr++;

    horario[pos] = '\0';

    ptr++;
    pos = 0;

    sscanf(
        horario,
        "%d:%d-%d:%d",
        &r.abertura.hora,
        &r.abertura.minuto,
        &r.fechamento.hora,
        &r.fechamento.minuto
    );

    // Data abertura
    char data[50];

    while (*ptr != ',')
        data[pos++] = *ptr++;

    data[pos] = '\0';

    ptr++;
    pos = 0;

    sscanf(
        data,
        "%d-%d-%d",
        &r.dataAbertura.ano,
        &r.dataAbertura.mes,
        &r.dataAbertura.dia
    );

    // Aberto
    while (*ptr != '\0' && *ptr != '\n' && *ptr != '\r')
        temp[pos++] = *ptr++;

    temp[pos] = '\0';

    r.aberto = (strcmp(temp, "true") == 0);

    return r;
}

// --- Impressão ---
void imprimir(Restaurante *r) {

    char cifroes[10] = "";

    for (int i = 0; i < r->faixaPreco; i++) {
        strcat(cifroes, "$");
    }

    printf(
        "[%d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n",
        r->id,
        r->nome,
        r->cidade,
        r->capacidade,
        r->avaliacao,
        r->tiposCozinha,
        cifroes,
        r->abertura.hora,
        r->abertura.minuto,
        r->fechamento.hora,
        r->fechamento.minuto,
        r->dataAbertura.dia,
        r->dataAbertura.mes,
        r->dataAbertura.ano,
        r->aberto ? "true" : "false"
    );
}

// --- Inserção Parcial ---
void insercaoParcial(ColecaoRestaurantes *col, int k) {

    for (int i = 1; i < col->n; i++) {

        Restaurante tmp = col->array[i];
        movimentacoes++;

        int j;

        // Primeiros k elementos
        if (i < k) {

            j = i - 1;

            while (j >= 0 && compare(&tmp, &col->array[j]) < 0) {

                col->array[j + 1] = col->array[j];
                movimentacoes++;

                j--;
            }

            col->array[j + 1] = tmp;
            movimentacoes++;
        }

        // Após formar os k primeiros
        else if (compare(&tmp, &col->array[k - 1]) < 0) {

            j = k - 2;

            while (j >= 0 && compare(&tmp, &col->array[j]) < 0) {

                col->array[j + 1] = col->array[j];
                movimentacoes++;

                j--;
            }

            col->array[j + 1] = tmp;
            movimentacoes++;
        }
    }
}

// --- Main ---
int main() {

    ColecaoRestaurantes base;
    base.n = 0;

    // Caminho do CSV
    FILE *f = fopen("/tmp/restaurantes.csv", "r");

    // Windows fallback
    if (f == NULL) {
        f = fopen("restaurantes.csv", "r");
    }

    if (f != NULL) {

        char linha[1024];

        fgets(linha, sizeof(linha), f);

        while (fgets(linha, sizeof(linha), f)) {
            base.array[base.n++] = parse_restaurante(linha);
        }

        fclose(f);
    }

    ColecaoRestaurantes selecionados;
    selecionados.n = 0;

    // Entrada IDs
    char entrada[50];

    while (scanf("%s", entrada) == 1) {

        if (
            strcmp(entrada, "FIM") == 0 ||
            strcmp(entrada, "-1") == 0
        ) {
            break;
        }

        int idBusca = atoi(entrada);

        for (int i = 0; i < base.n; i++) {

            if (base.array[i].id == idBusca) {

                selecionados.array[selecionados.n++] =
                    base.array[i];

                break;
            }
        }
    }

    clock_t inicio = clock();

    insercaoParcial(&selecionados, 10);

    clock_t fim = clock();

    // Imprime apenas os 10 primeiros
    for (
        int i = 0;
        i < 10 && i < selecionados.n;
        i++
    ) {
        imprimir(&selecionados.array[i]);
    }

    // Log
    FILE *log = fopen("844387_insercaoParcial.txt", "w");

    if (log != NULL) {

        double tempo =
            ((double)(fim - inicio) / CLOCKS_PER_SEC) * 1000.0;

        fprintf(log,"844387\t%d\t%d\t%.2lfms",comparacoes,movimentacoes,tempo);

        fclose(log);
    }

    return 0;
}