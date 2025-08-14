/* Somatorio de 
    5 (sigma) n=1 de n^2
    Este programa calcula o somatório de n^2 de 1 até 5.
*/


#include <stdio.h>

int main(){
    int n=0 ;
    int soma=0;
    for(n=1; n<=5; n++){
        soma += n*n;
    }
    printf("Somatorio de 5 (sigma) n=1 de n^2\n");
    printf("Este programa calcula o somatorio de n^2 de 1 ate 5.\n");
    printf("\n");


    printf("%d\n", soma);
    return 0;


}