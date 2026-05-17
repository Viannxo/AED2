/*
 * Questao 13 - Arvore Binaria em C (TP3)
 *
 * Arvore Binaria em C: Repita o exercicio anterior "Arvore Binaria",
 * realizando a implementacao em C.
 * - Chave de pesquisa: atributo nome
 * - Nao insere elemento se a chave ja estiver na arvore
 * - Entrada: IDs ate -1 (insercao), depois nomes ate FIM (pesquisa)
 * - Saida: caminho de pesquisa (raiz, esq, dir) + SIM/NAO por linha
 * - Ao final: caminhamento em ordem (inorder) de todos os registros
 * - Log: matricula_arvore_binaria.txt (matricula, comparacoes, tempo_ms)
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

/* ===================== ESTRUTURAS AUXILIARES ===================== */

typedef struct Data { int ano, mes, dia; } Data;

Data parse_data(char *s) {
    Data d;
    sscanf(s, " %d - %d - %d ", &d.ano, &d.mes, &d.dia);
    return d;
}
void formatar_data(Data *d, char *buf) {
    sprintf(buf, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}

typedef struct Hora { int hora, minuto; } Hora;

Hora parse_hora(char *s) {
    Hora h;
    sscanf(s, " %d : %d ", &h.hora, &h.minuto);
    return h;
}
void formatar_hora(Hora *h, char *buf) {
    sprintf(buf, "%02d:%02d", h->hora, h->minuto);
}

typedef struct Price { int faixa; } Price;

Price parse_price(char *s) {
    Price p; p.faixa = 0;
    while (*s) { if (*s == '$') p.faixa++; s++; }
    return p;
}
void formatar_price(Price *p, char *buf) {
    int i;
    for (i = 0; i < p->faixa; i++) buf[i] = '$';
    buf[i] = '\0';
}

typedef struct TiposCozinha { char raw[200]; } TiposCozinha;

TiposCozinha create_tipos(const char *tipos) {
    TiposCozinha tc;
    int i = 0;
    while (tipos[i] != '\0') { tc.raw[i] = tipos[i]; i++; }
    tc.raw[i] = '\0';
    return tc;
}
void formatar_tipos(TiposCozinha *tc, char *buf) {
    int bi = 0;
    buf[bi++] = '[';
    for (int i = 0; tc->raw[i] != '\0'; i++)
        buf[bi++] = (tc->raw[i] == ';') ? ',' : tc->raw[i];
    buf[bi++] = ']';
    buf[bi] = '\0';
}

/* ===================== RESTAURANTE ===================== */

typedef struct Restaurante {
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    float avaliacao;
    TiposCozinha tiposCozinha;
    Price preco;
    Data dataAbertura;
    Hora abertura, fechamento;
    bool funcionamento;
} Restaurante;

/* Localiza o '-' para separar horario de abertura/fechamento */
static int find_char(const char *s, char c) {
    for (int i = 0; s[i] != '\0'; i++)
        if (s[i] == c) return i;
    return -1;
}

static void my_trim(char *s) {
    for (int i = 0; s[i] != '\0'; i++)
        if (s[i] == '\r' || s[i] == '\n') { s[i] = '\0'; return; }
}

Restaurante parse_restaurante(char *linha) {
    Restaurante r;
    char *campos[11];
    int idx = 0;

    campos[idx++] = linha;
    for (int i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            linha[i] = '\0';
            campos[idx++] = &linha[i + 1];
        } else if (linha[i] == '\n' || linha[i] == '\r') {
            linha[i] = '\0';
        }
    }

    sscanf(campos[0], "%d",       &r.id);
    sscanf(campos[1], "%99[^\n]", r.nome);
    sscanf(campos[2], "%99[^\n]", r.cidade);
    sscanf(campos[3], "%d",       &r.capacidade);
    sscanf(campos[4], "%f",       &r.avaliacao);

    r.tiposCozinha = create_tipos(campos[5]);
    r.preco = parse_price(campos[6]);

    int hifen = find_char(campos[7], '-');
    if (hifen >= 0) {
        campos[7][hifen] = '\0';
        r.abertura   = parse_hora(campos[7]);
        r.fechamento = parse_hora(campos[7] + hifen + 1);
    } else {
        r.abertura.hora = r.abertura.minuto = 0;
        r.fechamento.hora = r.fechamento.minuto = 0;
    }

    r.dataAbertura = parse_data(campos[8]);

    char func_str[10] = {0};
    if (campos[9] != NULL) sscanf(campos[9], "%9s", func_str);
    r.funcionamento = (strcmp(func_str, "true") == 0 || strcmp(func_str, "True") == 0);

    return r;
}

void formatar_restaurante(Restaurante *r, char *buf) {
    char dataB[20], haB[20], hfB[20], precoB[10], tiposB[200], avalB[10];
    formatar_data(&r->dataAbertura, dataB);
    formatar_hora(&r->abertura, haB);
    formatar_hora(&r->fechamento, hfB);
    formatar_price(&r->preco, precoB);
    formatar_tipos(&r->tiposCozinha, tiposB);
    sprintf(avalB, "%.1f", r->avaliacao);

    sprintf(buf,
        "[%d ## %s ## %s ## %d ## %s ## %s ## %s ## %s-%s ## %s ## %s]",
        r->id, r->nome, r->cidade, r->capacidade,
        avalB, tiposB, precoB, haB, hfB, dataB,
        r->funcionamento ? "true" : "false");
}

/* ===================== COLECAO (CSV) ===================== */

typedef struct { Restaurante array[1000]; int n; } ColecaoRestaurantes;

void ler_csv(ColecaoRestaurantes *c, const char *path) {
    FILE *f = fopen(path, "r");
    if (!f) return;
    char linha[1024];
    if (fgets(linha, sizeof(linha), f) == NULL) { fclose(f); return; }
    while (fgets(linha, sizeof(linha), f) && c->n < 1000) {
        my_trim(linha);
        c->array[c->n++] = parse_restaurante(linha);
    }
    fclose(f);
}

Restaurante *buscar_por_id(ColecaoRestaurantes *c, int id) {
    for (int i = 0; i < c->n; i++)
        if (c->array[i].id == id) return &c->array[i];
    return NULL;
}

/* ===================== ARVORE BINARIA ===================== */

/*
 * No da arvore binaria de pesquisa.
 * Armazena ponteiro para Restaurante (da colecao).
 */
typedef struct No {
    Restaurante *elemento;
    struct No *esq, *dir;
} No;

No *novo_no(Restaurante *r) {
    No *n = (No *) malloc(sizeof(No));
    n->elemento = r;
    n->esq = n->dir = NULL;
    return n;
}

/* Insercao recursiva — chave: nome */
No *inserir(No *i, Restaurante *r) {
    if (i == NULL) return novo_no(r);
    int cmp = strcmp(r->nome, i->elemento->nome);
    if (cmp < 0)      i->esq = inserir(i->esq, r);
    else if (cmp > 0) i->dir = inserir(i->dir, r);
    /* cmp == 0: elemento ja existe, nao insere */
    return i;
}

/*
 * Pesquisa recursiva.
 * Imprime o caminho (raiz esq dir ...) e SIM/NAO.
 * Acumula comparacoes em *comp.
 */
bool pesquisar(No *i, const char *nome, int *comp) {
    if (i == NULL) {
        printf(" NAO\n");
        return false;
    }
    (*comp)++;
    int c = strcmp(nome, i->elemento->nome);
    if (c == 0) {
        printf(" SIM\n");
        return true;
    } else if (c < 0) {
        printf(" esq");
        return pesquisar(i->esq, nome, comp);
    } else {
        printf(" dir");
        return pesquisar(i->dir, nome, comp);
    }
}

/* Caminhamento em ordem (inorder) — imprime em ordem alfabetica */
void caminhar_em_ordem(No *i) {
    if (i != NULL) {
        caminhar_em_ordem(i->esq);
        char buf[500];
        formatar_restaurante(i->elemento, buf);
        printf("%s\n", buf);
        caminhar_em_ordem(i->dir);
    }
}

/* ===================== MAIN ===================== */

int main(void) {
    ColecaoRestaurantes colecao;
    colecao.n = 0;
    ler_csv(&colecao, "/tmp/restaurantes.csv");

    No *raiz = NULL;
    char linha[200];

    /* Parte 1: leitura de IDs (uma por linha) ate -1, insercao na arvore */
    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        my_trim(linha);
        if (strcmp(linha, "-1") == 0 || strcmp(linha, "FIM") == 0) break;
        int id;
        sscanf(linha, "%d", &id);
        Restaurante *r = buscar_por_id(&colecao, id);
        if (r != NULL) raiz = inserir(raiz, r);
    }

    int total_comp = 0;
    clock_t t_inicio = clock();

    /* Parte 2: pesquisa por nomes ate FIM */
    while (fgets(linha, sizeof(linha), stdin) != NULL) {
        my_trim(linha);
        if (strcmp(linha, "FIM") == 0 || linha[0] == '\0') break;
        printf("raiz");
        pesquisar(raiz, linha, &total_comp);
    }

    clock_t t_fim = clock();
    double tempo_ms = (double)(t_fim - t_inicio) / CLOCKS_PER_SEC * 1000.0;

    /* Parte 3: caminhamento em ordem */
    caminhar_em_ordem(raiz);

    /* Log */
    FILE *log = fopen("844387_arvore_binaria.txt", "w");
    if (log != NULL) {
        fprintf(log, "844387\t%d\t%.4f", total_comp, tempo_ms);
        fclose(log);
    }

    return 0;
}