/* Somatorio de 
    5 (sigma) 1 soma de  3i
    Este programa calcula o somatório de 3i de 1 até 5.
*/

#include <stdio.h>

int main(){
    int i=0 ;
    int soma=0;
    for(i=1; i<=5; i++){
        soma += 3*i;
    }
    printf("Somatorio de 5 (sigma) 1 de 3i\n");
    printf("Este programa calcula o somatorio de 3i de 1 ate 5.\n");
    printf("\n");


    printf("%d\n", soma);
    return 0;


}