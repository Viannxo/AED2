/**
 * EXERCÍCIO 1 - Remover todos os elementos pares de uma lista simples flexível
 * Complexidade: Theta(n) — percorre a lista uma única vez.
 * 
 * LÓGICA:
 * - Dois ponteiros: 'ant' (anterior) e 'atual'.
 * - Se atual.elemento é par: ant.prox = atual.prox (pula o nó), libera o nó.
 * - Se não é par: apenas avança ant.
 * - A célula-cabeça garante que nunca tentamos remover antes do primeiro nó real.
 */

#include <stdio.h>
#include <stdlib.h>

/* ===== ESTRUTURA ===== */
typedef struct Celula {
    int elemento;
    struct Celula *prox;
} Celula;

typedef struct {
    Celula *primeiro; /* celula-cabeca */
    Celula *ultimo;
} Lista;

/* ===== FUNÇÕES AUXILIARES ===== */
void start(Lista *l) {
    l->primeiro = (Celula*) malloc(sizeof(Celula));
    l->primeiro->prox = NULL;
    l->ultimo = l->primeiro;
}

void inserirFim(Lista *l, int x) {
    Celula *nova = (Celula*) malloc(sizeof(Celula));
    nova->elemento = x;
    nova->prox = NULL;
    l->ultimo->prox = nova;
    l->ultimo = nova;
}

void mostrar(Lista *l) {
    printf("[ ");
    Celula *i;
    for (i = l->primeiro->prox; i != NULL; i = i->prox) {
        printf("%d ", i->elemento);
    }
    printf("]\n");
}

/* ===== IMPLEMENTAÇÃO ===== */
void removerPares(Lista *l) {
    Celula *ant = l->primeiro;   /* começa na cabeça */
    Celula *atual = l->primeiro->prox;

    while (atual != NULL) {
        if (atual->elemento % 2 == 0) {
            /* elemento é par: remover */
            ant->prox = atual->prox;

            /* se removemos o último, atualizar ponteiro */
            if (atual == l->ultimo) {
                l->ultimo = ant;
            }

            free(atual);
            atual = ant->prox; /* avança sem mover 'ant' */
        } else {
            /* elemento é ímpar: apenas avança */
            ant = atual;
            atual = atual->prox;
        }
    }
}

/* ===== TESTE ===== */
int main(void) {
    Lista l;
    start(&l);

    inserirFim(&l, 1);
    inserirFim(&l, 2);
    inserirFim(&l, 3);
    inserirFim(&l, 4);
    inserirFim(&l, 5);
    inserirFim(&l, 6);

    printf("Antes: ");
    mostrar(&l);

    removerPares(&l);

    printf("Depois: ");
    mostrar(&l);

    /* esperado: [ 1 3 5 ] */
    return 0;
}
