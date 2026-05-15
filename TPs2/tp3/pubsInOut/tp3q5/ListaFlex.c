#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

// --- Estruturas de Dados ---
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

// --- Estruturas da Lista Flexível ---
typedef struct Celula {
    Restaurante elemento;
    struct Celula* prox;
} Celula;

Celula* primeiro;
Celula* ultimo;
int tamanho;

Celula* novaCelula(Restaurante r) {
    Celula* nova = (Celula*)malloc(sizeof(Celula));
    nova->elemento = r;
    nova->prox = NULL;
    return nova;
}

void iniciar() {
    Restaurante rest; 
    // Usando rest para a célula cabeça sem definir id = -1 explicitamente
    primeiro = novaCelula(rest);
    ultimo = primeiro;
    tamanho = 0;
}

void inserirInicio(Restaurante r) {
    Celula* tmp = novaCelula(r);
    tmp->prox = primeiro->prox;
    primeiro->prox = tmp;
    if (primeiro == ultimo) {
        ultimo = tmp;
    }
    tamanho++;
}

void inserirFim(Restaurante r) {
    ultimo->prox = novaCelula(r);
    ultimo = ultimo->prox;
    tamanho++;
}

void inserir(Restaurante r, int pos) {
    if (pos < 0 || pos > tamanho) return; // Não printa erro, apenas sai
    if (pos == 0) {
        inserirInicio(r);
    } else if (pos == tamanho) {
        inserirFim(r);
    } else {
        Celula* i = primeiro;
        for (int j = 0; j < pos; j++, i = i->prox);
        Celula* tmp = novaCelula(r);
        tmp->prox = i->prox;
        i->prox = tmp;
        tamanho++;
    }
}

Restaurante removerInicio() {
    if (primeiro == ultimo) {
        Restaurante rest; 
        rest.id = -1; // Retorna -1 em caso de erro
        return rest;
    }
    Celula* tmp = primeiro->prox;
    Restaurante resp = tmp->elemento;
    primeiro->prox = tmp->prox;
    if (ultimo == tmp) ultimo = primeiro;
    free(tmp);
    tamanho--;
    return resp;
}

Restaurante removerFim() {
    if (primeiro == ultimo) {
        Restaurante rest; 
        rest.id = -1; 
        return rest;
    }
    Celula* i;
    for (i = primeiro; i->prox != ultimo; i = i->prox);
    Restaurante resp = ultimo->elemento;
    ultimo = i;
    free(ultimo->prox);
    ultimo->prox = NULL;
    tamanho--;
    return resp;
}

Restaurante remover(int pos) {
    if (primeiro == ultimo || pos < 0 || pos >= tamanho) {
        Restaurante rest; 
        rest.id = -1; 
        return rest;
    }
    if (pos == 0) return removerInicio();
    if (pos == tamanho - 1) return removerFim();

    Celula* i = primeiro;
    for (int j = 0; j < pos; j++, i = i->prox);
    Celula* tmp = i->prox;
    Restaurante resp = tmp->elemento;
    i->prox = tmp->prox;
    free(tmp);
    tamanho--;
    return resp;
}

// --- Parsing Manual ---
Restaurante parse_restaurante(char *linha) {
    Restaurante r;
    char *ptr = linha;
    char temp[500];
    int pos = 0;

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.id = atoi(temp);
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') r.nome[pos++] = *ptr++;
    r.nome[pos] = '\0';
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') r.cidade[pos++] = *ptr++;
    r.cidade[pos] = '\0';
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.capacidade = atoi(temp);
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.avaliacao = atof(temp);
    if (*ptr == ',') ptr++; pos = 0;

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

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.faixaPreco = pos;
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    sscanf(temp, "%d:%d-%d:%d", &r.abertura.hora, &r.abertura.minuto, &r.fechamento.hora, &r.fechamento.minuto);
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != ',' && *ptr != '\0') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    sscanf(temp, "%d-%d-%d", &r.dataAbertura.ano, &r.dataAbertura.mes, &r.dataAbertura.dia);
    if (*ptr == ',') ptr++; pos = 0;

    while (*ptr != '\0' && *ptr != '\n' && *ptr != '\r') temp[pos++] = *ptr++;
    temp[pos] = '\0';
    r.aberto = (strcmp(temp, "true") == 0);

    return r;
}

// --- Funções Auxiliares ---
Restaurante buscarNaBase(ColecaoRestaurantes* base, int idBusca) {
    for (int i = 0; i < base->n; i++) {
        if (base->array[i].id == idBusca) return base->array[i];
    }
    Restaurante rest; rest.id = -1; return rest;
}

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

    iniciar();
    char entrada[50];

    while (scanf("%s", entrada) == 1) {
        if (strcmp(entrada, "FIM") == 0 || strcmp(entrada, "-1") == 0) break;
        int idBusca = atoi(entrada);
        Restaurante r = buscarNaBase(&base, idBusca);
        if (r.id != -1) inserirFim(r);
    }

    clock_t inicio_tempo = clock();

    int numOps;
    if (scanf("%d", &numOps) == 1) {
        for (int i = 0; i < numOps; i++) {
            char op[5];
            scanf("%s", op);
            
            if (op[0] == 'I' && op[1] == 'I') {
                int id; scanf("%d", &id);
                Restaurante r = buscarNaBase(&base, id);
                if (r.id != -1) inserirInicio(r);
            } 
            else if (op[0] == 'I' && op[1] == 'F') {
                int id; scanf("%d", &id);
                Restaurante r = buscarNaBase(&base, id);
                if (r.id != -1) inserirFim(r);
            } 
            else if (op[0] == 'I' && op[1] == '*') {
                int pos, id; scanf("%d %d", &pos, &id);
                Restaurante r = buscarNaBase(&base, id);
                if (r.id != -1) inserir(r, pos);
            } 
            else if (op[0] == 'R' && op[1] == 'I') {
                Restaurante rem = removerInicio();
                if(rem.id != -1) printf("(R)%s\n", rem.nome);
            } 
            else if (op[0] == 'R' && op[1] == 'F') {
                Restaurante rem = removerFim();
                if(rem.id != -1) printf("(R)%s\n", rem.nome);
            } 
            else if (op[0] == 'R' && op[1] == '*') {
                int pos; scanf("%d", &pos);
                Restaurante rem = remover(pos);
                if(rem.id != -1) printf("(R)%s\n", rem.nome);
            }
        }
    }

    clock_t fim_tempo = clock();

    for (Celula* i = primeiro->prox; i != NULL; i = i->prox) {
        imprimir(&i->elemento);
    }

    FILE *log = fopen("844387_lista_flexivel.txt", "w");
    if (log != NULL) {
        double tempo = ((double)(fim_tempo - inicio_tempo) / CLOCKS_PER_SEC) * 1000.0;
        fprintf(log, "844387\t0\t0\t%.2lfms", tempo);
        fclose(log);
    }

    return 0;
}