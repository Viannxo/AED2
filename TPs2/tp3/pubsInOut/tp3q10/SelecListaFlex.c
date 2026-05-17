#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <time.h>

// --- Função Auxiliar de Cópia Manual de String (Substituindo strcpy) ---
void copiar_string(char *destino, const char *origem) {
    int i = 0;
    while (origem[i] != '\0') {
        destino[i] = origem[i];
        i++;
    }
    destino[i] = '\0';
}

// --- Estruturas e Formatação de Data ---
typedef struct Data {
    int ano;
    int mes;
    int dia;
} Data;

Data parse_data(char *s) {
    Data d;
    sscanf(s, " %d - %d - %d ", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data *d, char *Buffer) {
    sprintf(Buffer, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}

// --- Estruturas e Formatação de Hora ---
typedef struct Hora {
    int hora;
    int minuto;
} Hora;

Hora parse_hora(char *s) {
    Hora h;
    sscanf(s, " %d : %d ", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora *h, char *Buffer) {
    sprintf(Buffer, "%02d:%02d", h->hora, h->minuto);
}

// --- Estruturas e Formatação de Preço ---
typedef struct Price {
    int faixa;
} Price;

Price parse_price(char *s) {
    Price p;
    p.faixa = 0;
    while (*s) {
        if (*s == '$') p.faixa++;
        s++;
    }
    return p;
}

void formatar_price(Price *p, char *Buffer) {
    int i;
    for (i = 0; i < p->faixa; i++) {
        Buffer[i] = '$';
    }
    Buffer[i] = '\0';
}

// --- Estruturas e Formatação de Tipos de Cozinha ---
typedef struct TiposCozinha {
    char **tipos;
    int quantidade;
} TiposCozinha;

// Criação manual sem a utilização de strtok
TiposCozinha create(const char *tipos) {
    TiposCozinha tc;
    int count = 1;

    for (int i = 0; tipos[i] != '\0'; i++) {
        if (tipos[i] == ';') {
            count++;
        }
    }

    tc.quantidade = count;
    tc.tipos = (char **)malloc(count * sizeof(char *));

    int idx = 0;
    int inicio = 0;
    for (int i = 0; ; i++) {
        if (tipos[i] == ';' || tipos[i] == '\0') {
            int tam = i - inicio;
            tc.tipos[idx] = (char *)malloc((tam + 1) * sizeof(char));
            for (int j = 0; j < tam; j++) {
                tc.tipos[idx][j] = tipos[inicio + j];
            }
            tc.tipos[idx][tam] = '\0';
            idx++;
            inicio = i + 1;
            if (tipos[i] == '\0') break;
        }
    }

    return tc;
}

void formatar_tipos(TiposCozinha *tc, char *buffer) {
    int offset = 0;
    offset += sprintf(buffer + offset, "[");

    for (int i = 0; i < tc->quantidade; i++) {
        offset += sprintf(buffer + offset, "%s", tc->tipos[i]);

        if (i < tc->quantidade - 1) {
            offset += sprintf(buffer + offset, ",");
        }
    }

    sprintf(buffer + offset, "]");
}

// --- Estruturas e Formatação de Avaliação ---
typedef struct Avaliacao {
    float avaliacao;
} Avaliacao;

void formatar_avaliacao(Avaliacao *a, char *Buffer) {
    sprintf(Buffer, "%.1f", a->avaliacao);
}

// --- Estruturas e Formatação de Funcionamento ---
typedef struct Funcionamento {
    bool func;
} Funcionamento;

void formatar_funcionamento(bool f, char *Buffer) {
    if (f) {
        copiar_string(Buffer, "true");
    } else {
        copiar_string(Buffer, "false");
    }
}

// --- Estrutura Principal do Restaurante ---
typedef struct Restaurante {
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    Avaliacao avaliacao;
    TiposCozinha tiposCozinha;
    Price preco;
    Data dataAbertura;
    Hora abertura, fechamento;
    Funcionamento funcionamento;
} Restaurante;

Restaurante parse_restaurante(char *linha) {
    Restaurante r;
    char *campos[11];
    int idx = 0;

    campos[idx++] = linha;

    for (int i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            linha[i] = '\0';
            campos[idx++] = &linha[i + 1];
        }
    }

    r.id = atoi(campos[0]);
    copiar_string(r.nome, campos[1]);
    copiar_string(r.cidade, campos[2]);
    r.capacidade = atoi(campos[3]);

    r.avaliacao.avaliacao = atof(campos[4]);
    r.tiposCozinha = create(campos[5]);
    r.preco = parse_price(campos[6]);

    // Busca manual do hífen para substituir strchr
    int hifen_idx = -1;
    for (int i = 0; campos[7][i] != '\0'; i++) {
        if (campos[7][i] == '-') {
            hifen_idx = i;
            break;
        }
    }
    if (hifen_idx != -1) {
        campos[7][hifen_idx] = '\0';
        r.abertura = parse_hora(campos[7]);
        r.fechamento = parse_hora(&campos[7][hifen_idx + 1]);
    } else {
        r.abertura.hora = 0; r.abertura.minuto = 0;
        r.fechamento.hora = 0; r.fechamento.minuto = 0;
    }

    r.dataAbertura = parse_data(campos[8]);

    if (campos[9] != NULL) {
        r.funcionamento.func = (campos[9][0] == 't' || campos[9][0] == 'T');
    } else {
        r.funcionamento.func = false;
    }

    return r;
}

void formatar_restaurante(Restaurante *r, char *buffer) {
    char dataBuffer[20], horaABuffer[20], horaFBuffer[20];
    char precoBuffer[10], tiposBuffer[200], funcBuffer[10], avalBuffer[10];

    formatar_data(&r->dataAbertura, dataBuffer);
    formatar_hora(&r->abertura, horaABuffer);
    formatar_hora(&r->fechamento, horaFBuffer);
    formatar_price(&r->preco, precoBuffer);
    formatar_tipos(&r->tiposCozinha, tiposBuffer);
    formatar_funcionamento(r->funcionamento.func, funcBuffer);
    formatar_avaliacao(&r->avaliacao, avalBuffer);

    sprintf(buffer,
        "[%d ## %s ## %s ## %d ## %s ## %s ## %s ## %s-%s ## %s ## %s]",
        r->id, r->nome, r->cidade, r->capacidade, avalBuffer,
        tiposBuffer, precoBuffer, horaABuffer, horaFBuffer, dataBuffer, funcBuffer
    );
}

// --- Alteração: Renomeado de array para rest com capacidade segura ---
typedef struct {
    Restaurante rest[1000];
    int n;
} ColecaoRestaurantes;

void ler_csv(ColecaoRestaurantes *c, const char *path) {
    FILE *f = fopen(path, "r");
    if (!f) return;

    char linha[1024];
    if (fgets(linha, sizeof(linha), f) == NULL) {
        fclose(f);
        return;
    }

    while (fgets(linha, sizeof(linha), f) && c->n < 1000) {
        // Remoção manual de quebras de linha para evitar strcspn
        int len = 0;
        while (linha[len] != '\0') len++;
        while (len > 0 && (linha[len - 1] == '\r' || linha[len - 1] == '\n')) {
            linha[len - 1] = '\0';
            len--;
        }
        c->rest[c->n++] = parse_restaurante(linha);
    }
    fclose(f);
}

Restaurante* buscar_por_id(ColecaoRestaurantes *c, int id) {
    for (int i = 0; i < c->n; i++) {
        if (c->rest[i].id == id) {
            return &c->rest[i];
        }
    }
    return NULL;
}

// --- Implementação da Lista Dinâmica Flexível ---
typedef struct Celula {
    Restaurante *elemento;
    struct Celula *prox;
} Celula;

typedef struct {
    Celula *primeiro;
    Celula *ultimo;
    int tamanho;
} ListaFlexivel;

Celula* nova_celula(Restaurante *r) {
    Celula *nova = (Celula*)malloc(sizeof(Celula));
    nova->elemento = r;
    nova->prox = NULL;
    return nova;
}

void init_lista(ListaFlexivel *l) {
    l->primeiro = nova_celula(NULL); 
    l->ultimo = l->primeiro;
    l->tamanho = 0;
}

void inserir_fim(ListaFlexivel *l, Restaurante *r) {
    l->ultimo->prox = nova_celula(r);
    l->ultimo = l->ultimo->prox;
    l->tamanho++;
}

void mostrar_lista(ListaFlexivel *l) {
    char buffer[500];
    for (Celula *i = l->primeiro->prox; i != NULL; i = i->prox) {
        formatar_restaurante(i->elemento, buffer);
        printf("%s\n", buffer);
    }
}

// --- Alteração: Renomeado de selection_sort_flexivel para SelecSortFlex ---
void SelecSortFlex(ListaFlexivel *l, int *comparacoes, int *movimentacoes) {
    *comparacoes = 0;
    *movimentacoes = 0;

    for (Celula *i = l->primeiro->prox; i != NULL && i->prox != NULL; i = i->prox) {
        Celula *menor = i;
        for (Celula *j = i->prox; j != NULL; j = j->prox) {
            (*comparacoes)++;
            if (strcmp(j->elemento->nome, menor->elemento->nome) < 0) {
                menor = j;
            }
        }
        if (menor != i) {
            Restaurante *temp = i->elemento;
            i->elemento = menor->elemento;
            menor->elemento = temp;
            (*movimentacoes) += 3;
        }
    }
}

// --- Alteração: Renomeado de criar_log para cLog ---
void cLog(int comparacoes, int movimentacoes, double tempo_ms) {
    FILE *log = fopen("844387.selecao_flexivel.txt", "w");
    if (log != NULL) {
        fprintf(log, "844387\t%d\t%d\t%lf\n", comparacoes, movimentacoes, tempo_ms);
        fclose(log);
    }
}

// --- Programa Principal ---
int main() {
    ColecaoRestaurantes col;
    col.n = 0;
    ler_csv(&col, "/tmp/restaurantes.csv");

    ListaFlexivel lista;
    init_lista(&lista);

    char entrada[50];

    while (scanf("%49s", entrada) == 1) {
        if (strcmp(entrada, "FIM") == 0 || strcmp(entrada, "-1") == 0) {
            break; 
        }

        int idBusca = atoi(entrada);
        Restaurante *r = buscar_por_id(&col, idBusca);
        if (r != NULL) {
            inserir_fim(&lista, r);
        }
    }

    int comparacoes = 0;
    int movimentacoes = 0;

    clock_t start = clock();
    SelecSortFlex(&lista, &comparacoes, &movimentacoes);
    clock_t end = clock();
    
    double tempo_ms = ((double)(end - start) / CLOCKS_PER_SEC) * 1000.0;

    mostrar_lista(&lista);

    cLog(comparacoes, movimentacoes, tempo_ms);

    return 0;
}