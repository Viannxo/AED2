import java.util.*;


class Cel {
    public int element;
    public Cel prox;

    public Cel() {
        this(0);
    }

    // Celula funciona como vetor unico com um
    // ponteiro que irá apontar para a proxima
    // celula, onde utilizar:
    // Listas , Pilhas , Fila ,
    // Arvore(colocando 2 ponteiros para os filhos)

    public Cel(int x) {
        this.element = x;
        this.prox = null;
    }
    // construtor para a classe celula
}

class Stack {
    public Cel top;

    public Stack() {
        this.top = null;
    }

    public void insert(int x) {
        Cel tmp = new Cel(x);
        // elemento temporario pra criar o pacote
        tmp.prox = this.top;
        // novo topo é esse tmp inicializado
        this.top=tmp;
    }

    public void show() {

        Cel now=this.top;
        System.out.print("[");

        while (now != null) {
            System.out.print(" " + now.element + " ");
            now = now.prox;
        }

        System.out.print("]");
    }

    public int remove()throws Exception{
        if(this.top == null){
            throw new Exception("Empty Stack");
        }
        int aux=this.top.element;
        //salva elemento removido
        this.top=this.top.prox;
        //muda o topo para o proximo elemento
        return aux;
    }

    public void sum(){
        Cel now=this.top;
        int sum=0;

        while(now!= null){
            sum+=now.element;
            now=now.prox;
        }
        System.out.println(" \n Soma igual:" + sum);
    }

    public class TesteCelula {
        public static void main(String [] args){
            Stack p= new Stack();
            p.insert(1);
            p.insert(2);
            p.insert(3);
            p.show();
            p.sum();
        }
    }
}