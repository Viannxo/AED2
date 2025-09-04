/*
Crie um método recursivo que recebe um número inteiro como
parâmetro e retorna a soma de seus dígitos. Na saída padrão,
para cada linha de entrada, escreva uma linha de saída com
o resultado da soma dos dígitos.
Por exemplo, se a entrada for 12345, a saída deve ser 15.
*/

#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

bool Parada(char str[])
{
    return strlen(str) == 3 && str[0] == 'F' && str[1] == 'I' && str[2] == 'M';
}

int somaDigitos(int n)
{
    if (n == 0)
    {
        return 0;
    }
    else
    {
        return (n % 10) + somaDigitos(n / 10);
    }
}

int main()
{
    char in[100];
    while (fgets(in, sizeof(in), stdin))
    {
        if (Parada(in))
        {
            return 0;
        }
        else
        {
            // remover o '\n' do final
            in[strcspn(in, "\n")] = 0;

            int num = atoi(in);

            // parar se a entrada for exatamente "0"
            if (num == 0 && strcmp(in, "0") == 0)
            {
                return 0;
            }

            int result = somaDigitos(num);
            printf("%d\n", result);
        }
    }
    return 0;
}
