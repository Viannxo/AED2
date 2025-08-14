/* Somatorio de 
    5 (sigma) n=1 de (8j-2m)
    Este programa calcula o somatório de (8j-2m) de 1 até 5.
*/

#include <stdio.h>

int main(){
    int soma=0;
    int somaM=0;
    int j, m;
    for(m=1; m<=5; m++){
        soma=8*m;
        somaM += (8*j-2*m);
    }
    printf("Somatorio de 5 (sigma) 1 de (8j-2m)\n");

    printf("\n");


    printf("%dJ%d\n", soma, somaM);
    return 0;


}