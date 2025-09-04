/*1. Palíndromo (Recursivo)
Crie um método recursivo que receba uma 
string como parâmetro e retorne se essa 
é um “Palíndromo”. Na saída padrão, para
cada linha de entrada, escreva uma linha
de saída com SIM/NÃO indicando se a linha 
é um palíndromo. Observe que a entrada 
pode conter caracteres não letras.*/

#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool Parada(char str[]){
    return strlen(str) == 3 && str[0]== 'F' && str[1] == 'I' && str[2] == 'M';
}


bool palindromo(char str[], int inicio, int fim){
    if(inicio >= fim){
        return true;
    }
    if(str[inicio] != str[fim]){
        return false;
    }
    return palindromo(str, inicio + 1, fim - 1);
}

int main() {
    char str[1000];
    int n;

    // lê uma linha
    fgets(str, sizeof(str), stdin);

    // tira o \n do final
    n = strlen(str);
    if (n > 0 && str[n - 1] == '\n') {
        str[n - 1] = '\0';
        n--;
    }

    while (!Parada(str)) {
        if (palindromo(str, 0, n - 1)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }

        if (fgets(str, sizeof(str), stdin) == NULL) {
            return 0; 
        }

        n = strlen(str);
        if (n > 0 && str[n - 1] == '\n') {
            str[n - 1] = '\0';
            n--;
        }
    }

    return 0;
}