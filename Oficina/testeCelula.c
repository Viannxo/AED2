#include <stdio.h>
#include <stdlib.h>



typedef struct Cel{
    int element;
    struct Cel *prox;
}Cel;

Cel *top = NULL;

//criar celula / construtor

Cel* createCel(int x){
    Cel* tmp = (Cel*)malloc(sizeof(Cel));
    tmp -> element = x; 
    tmp -> prox = NULL;
    return tmp;
}

void insert(int x){
    Cel* tmp = createCel(x);
    // elemento temporario pra criar o pacote
    tmp->prox= top;
    // novo topo é esse tmp inicializado
    top = tmp;
}

void show(){

    Cel* now = top;
    printf("\n [");

    while (now != NULL)
    {
        printf(" %d ", now->element);
        now = now -> prox;

    }
    printf("] \n");
}

int dele() {
    if (top == NULL){
        printf("\n Empty Stack \n");
        return -1;
    }
    Cel* tmp = top;
    int aux = top->element;
    // salva elemento removido
    top = top->prox;
    // muda o topo para o proximo elemento
    tmp->prox = NULL;
    free(tmp);
    tmp=NULL;
    return aux;
}

void sum()
{
    Cel* now = top;
    int sum = 0;

    while (now != NULL){
        sum += now->element;
        now = now->prox;
    }
    printf(" \n Soma igual:" + sum);
}

    int main(){
        for(int i =0; i<10; i++){
            insert(i+1);
        }
        show();
        sum();

        for(int i =0; i<10; i++){
            int removed = dele();
            printf("Item removido %d", removed);
            show();
        }
    show();

    return 0;
}

