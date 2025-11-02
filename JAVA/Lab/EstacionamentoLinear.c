/*
Após muito tempo juntando dinheiro, Rafael finalmente conseguiu 
comprar seu carro (parcelado, é claro). Chega de pegar ônibus, 
agora sua vida será mais fácil. Pelo menos isso é o que ele pensava,
até ouvir falar do estacionamento perto da faculdade onde ele decidiu
estacionar o carro todos os dias.

O estacionamento tem apenas um corredor, com largura o suficiente para
acomodar um carro, e profundidade suficiente para acomodar K carros, 
um atrás do outro. Como este estacionamento só tem um portão, só é 
possível entrar e sair por ele.

Quando o primeiro carro entra no estacionamento, o mesmo ocupa a posição
próxima à parede, ao fundo do estacionamento. Todos os próximos carros 
estacionam logo atrás dele, formando uma fila. Obviamente, não é possível
que um carro passe por cima de outro, portanto só é possível que um carro
saia do estacionamento se ele for o último da fila.

Dados o horário de chegada e saída prevista de N motoristas, incluindo Rafael,
diga se é possível que todos consigam estacionar e remover seus carros no 
estacionamento citado.

Entrada
Haverá diversos casos de teste. Cada caso de teste inicia com dois inteiros 
N e K (3 ≤ N ≤ 10⁴, 1 ≤ K ≤ 10³), representando o número de motoristas que 
farão uso do estacionamento, e o número de carros que o estacionamento 
consegue comportar, respectivamente.

Em seguida haverá N linhas, cada uma contendo dois inteiros Ci e 
Si (1 ≤ Ci, Si ≤ 10⁵), representando, respectivamente, o horário de
chegada e saída do motorista i (1 ≤ i ≤ N). Os valores de Ci são dados
de forma crescente, ou seja, Ci < Ci+1 para todo 1 ≤ i < N. Não haverá
mais de um motorista que chegam ao mesmo tempo, e nem mais de um motorista
que saiam ao mesmo tempo. É possível que um motorista consiga estacionar 
no mesmo momento em que outro motorista deseja sair. O último caso de teste
é indicado quando N = K = 0, o qual não deverá ser processado.

Saída
Para cada caso de teste imprima uma linha, contendo a palavra “Sim”, caso seja 
possível que todos os N motoristas façam uso do estacionamento, ou “Nao” caso contrário.
*/

#include <stdio.h>
#include <stdlib.h>
#include <stdbool>

// celula
typedef struct CelulaDupla {
    Car *carro;        // Elemento inserido na celula.
    struct CelulaDupla* prox; // Aponta a celula prox.
    struct CelulaDupla* ant;  // Aponta a celula anterior.
} CelulaDupla;

CelulaDupla* novaCelulaDupla(int elemento) {
   CelulaDupla* nova = (CelulaDupla*) malloc(sizeof(CelulaDupla));
   nova->elemento = elemento;
   nova->ant = nova->prox = NULL;
   return nova;
}

CelulaDupla* primeiro;
CelulaDupla* ultimo;


/**
 * Cria uma lista dupla sem elementos (somente no cabeca).
 */
void start () {
   primeiro = novaCelulaDupla(-1);
   ultimo = primeiro;
}

void inserirFim(int x) {
   ultimo->prox = novaCelulaDupla(x);
   ultimo->prox->ant = ultimo;
   ultimo = ultimo->prox;
}

int removerFim() {
   if (primeiro == ultimo) {
      errx(1, "Erro ao remover (vazia)!");
   } 
   int resp = ultimo->elemento;
   ultimo = ultimo->ant;
   ultimo->prox->ant = NULL;
   free(ultimo->prox);
   ultimo->prox = NULL;
   return resp;
}



int tamanho() {
   int tamanho = 0; 
   CelulaDupla* i;
   for(i = primeiro; i != ultimo; i = i->prox, tamanho++);
   return tamanho;
}

void inserir(int x, int pos) {

   int tam = tamanho();

   if(pos < 0 || pos > tam){
      errx(1, "Erro ao remover (posicao %d/%d invalida!", pos, tam);
   } else if (pos == 0){
      inserirInicio(x);
   } else if (pos == tam){
      inserirFim(x);
   } else {
      // Caminhar ate a posicao anterior a insercao
      CelulaDupla* i = primeiro;
      int j;
      for(j = 0; j < pos; j++, i = i->prox);

      CelulaDupla* tmp = novaCelulaDupla(x);
      tmp->ant = i;
      tmp->prox = i->prox;
      tmp->ant->prox = tmp->prox->ant = tmp;
      tmp = i = NULL;
   }
}


int remover(int pos) {
   int resp;
   int tam = tamanho();

   if (primeiro == ultimo){
      errx(1, "Erro ao remover (vazia)!");
   } else if(pos < 0 || pos >= tam){
      errx(1, "Erro ao remover (posicao %d/%d invalida!", pos, tam);
   } else if (pos == 0){
      resp = removerInicio();
   } else if (pos == tam - 1){
      resp = removerFim();
   } else {
      // Caminhar ate a posicao anterior a insercao
      CelulaDupla* i = primeiro->prox;
      int j;
      for(j = 0; j < pos; j++, i = i->prox);

      i->ant->prox = i->prox;
      i->prox->ant = i->ant;
      resp = i->elemento;
      i->prox = i->ant = NULL;
      free(i);
      i = NULL;
   }

   return resp;
}

//alterar para pesquisa inversa, pesquiar anterior
bool pesquisar(int x) {
   bool resp = false;
   CelulaDupla* i;

   for (i = primeiro->prox; i != NULL; i = i->prox) {
      if(i->elemento == x){
         resp = true;
         i = ultimo;
      }
   }
   return resp;
}


typedef ParkingLot{
    int tam;
}

typedef Car{
    int entry;
    int out;
}Car;


int main(){


}