package Oficina;

class Cel {
    public int element;
    public Cel prox;

    public Cel() {
        this(0);
    }

    // construtor para a classe celula

    public Cel(int x) {
        this.element = x;
        this.prox = null;
    }
    
    //Dependendo do uso, a classe Celula pode ser 
    // utilizada para implementar diferentes estruturas
    // de dados, como pilhas, filas ou listas encadeadas.
    // Por exemplo, em uma pilha, cada célula representaria
    // um elemento da pilha, e o ponteiro "prox" apontaria
    // para a próxima célula na pilha. Em uma lista encadeada,
    // cada célula representaria um nó da lista, e o ponteiro
    // "prox" apontaria para o próximo nó na lista.



}


