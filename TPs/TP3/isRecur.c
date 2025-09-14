#include <stdio.h>
#include <stdbool.h>


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


bool isVogal(char str[], int i) {
    if (str[i] == '\0') return (i > 0); // fim da string e não vazia

    char c = str[i];
    bool ehVogal = (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
                    c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');

    if (!ehVogal) return false;
    return isVogal(str, i + 1);
}

bool isConsoante(char str[], int i) {
    if (str[i] == '\0') return (i > 0); // fim da string e não vazia

    char c = str[i];
    bool letraMaiuscula = (c >= 'A' && c <= 'Z');
    bool letraMinuscula = (c >= 'a' && c <= 'z');
    bool ehVogal = (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
                    c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U');

    if (!(letraMaiuscula || letraMinuscula) || ehVogal) return false;
    return isConsoante(str, i + 1);
}

bool isNum(char str[], int i) {
    if (str[i] == '\0') return (i > 0); // pelo menos 1 dígito

    if (!(str[i] >= '0' && str[i] <= '9')) return false;
    return isNum(str, i + 1);
}

bool isReal(char str[], int i, int countPonto) {
    if (str[i] == '\0') return (i > 0 && countPonto <= 1);

    if (str[i] == '.' || str[i] == ',') {
        countPonto++;
        if (countPonto > 1) return false;
    } else if (!(str[i] >= '0' && str[i] <= '9')) {
        return false;
    }

    return isReal(str, i + 1, countPonto);
}

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

        printf("%s ", isVogal(str, 0) ? "SIM" : "NAO");
        printf("%s ", isConsoante(str, 0) ? "SIM" : "NAO");
        printf("%s ", isNum(str, 0) ? "SIM" : "NAO");
        printf("%s\n", isReal(str, 0, 0) ? "SIM" : "NAO");
    }

    return 0;
}