/* Somatorio de 
    5 (sigma) 1 de (2i+x)
    Este programa calcula o somatório de 2i+x de 1 até 5.
*/

#include <stdio.h>

int main(){
    int i=0 ;
    int soma=0;
    int x=0;
    for(i=1; i<=5; i++){
        soma += (2*i+x);
        x++;
    }
    printf("Somatorio de 5 (sigma) 1 de (2*i+x)\n");
    printf("Este programa calcula o somatorio de 2i+x de 1 ate 5.\n");
    printf("\n");


    printf("%d\n", soma);
    return 0;


}