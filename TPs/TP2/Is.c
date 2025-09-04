/*

Crie um método iterativo que recebe uma string e 
retorna true se a mesma é composta somente por vogais.

Crie outro método iterativo que recebe uma string e 
retorna true se a mesma é composta somente por consoantes.

Crie um terceiro método iterativo que recebe uma string 
e retorna true se a mesma corresponde a um número inteiro.

Crie um quarto método iterativo que recebe uma string e 
retorna true se a mesma corresponde a um número real.

Na saída padrão, para cada linha de entrada, escreva outra
de saída da seguinte forma X1 X2 X3 X4 onde cada Xi é um 
booleano indicando se a é entrada é: composta somente por 
vogais (X1); composta somente somente por consoantes (X2);
um número inteiro (X3); um número real (X4). Se Xi for 
verdadeiro, seu valor será SIM, caso contrário, NÃO.

*/

#include <stdio.h>
#include <stdbool.h>

int strLength(char str[]);
bool Parada(char str[]);
bool isVogal(char str[]);
bool isConsoante(char str[]);
bool isNum(char str[]);
bool isReal(char str[]);

int main() {
    char str[1000];

    while (true) {
        // Leitura da linha inteira
        fgets(str, sizeof(str), stdin);

        // Remove o '\n' ao final
        int i = 0;
        while (str[i] != '\0') {
            if (str[i] == '\n') {
                str[i] = '\0';
                break;
            }
            i++;
        }

        if (Parada(str)) break;

        printf("%s ", isVogal(str) ? "SIM" : "NAO");
        printf("%s ", isConsoante(str) ? "SIM" : "NAO");
        printf("%s ", isNum(str) ? "SIM" : "NAO");
        printf("%s\n", isReal(str) ? "SIM" : "NAO");
    }

    return 0;
}

int strLength(char str[]) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

bool Parada(char str[]) {
    return (str[0] == 'F' && str[1] == 'I' && str[2] == 'M' && str[3] == '\0');
}

bool isVogal(char str[]) {
    int len = strLength(str);
    if (len == 0) return false;

    for (int i = 0; i < len; i++) {
        char c = str[i];
        if (!(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
              c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')) {
            return false;
        }
    }
    return true;
}

bool isConsoante(char str[]) {
    int len = strLength(str);
    if (len == 0) return false;

    for (int i = 0; i < len; i++) {
        char c = str[i];
        bool letraMaiuscula = (c >= 'A' && c <= 'Z');
        bool letraMinuscula = (c >= 'a' && c <= 'z');

        if (!(letraMaiuscula || letraMinuscula)) return false; // não é letra
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
            c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            return false; // é vogal
        }
    }
    return true;
}

// Verifica se a string é número inteiro
bool isNum(char str[]) {
    int len = strLength(str);
    if (len == 0) return false; // string vazia não é número

    for (int i = 0; i < len; i++) {
        if (!(str[i] >= '0' && str[i] <= '9')) {
            return false;
        }
    }
    return true;
}

// Verifica se a string é número real (apenas um ponto ou vírgula)
bool isReal(char str[]) {
    int len = strLength(str);
    if (len == 0) return false; // string vazia não é real

    int countPonto = 0;
    for (int i = 0; i < len; i++) {
        if (str[i] == '.' || str[i] == ',') {
            countPonto++;
            if (countPonto > 1) return false; // mais de um separador
        }
        else if (!(str[i] >= '0' && str[i] <= '9')) {
            return false; // algum caractere não numérico
        }
    }
    return (countPonto == 0 || countPonto == 1); // deve ter 0 ou 1 separador
}
