/* Somatorio de 
    5 (sigma) 0 de i*(i-1)*(5-i)
    Este programa calcula o somatório de i*(i-1)*(5-i) de 0 até 5.
*/

#include <stdio.h>

int main(){
    int i=0 ;
    int soma=0;
    for(i=0; i<=5; i++){
        soma += i*(i-1)*(5-i);
    }
    printf("Somatorio de 5 (sigma) 0 de i*(i-1)*(5-i)\n");
    printf("Este programa calcula o somatorio de i*(i-1)*(5-i) de 0 ate 5.\n");
    printf("\n");


    printf("%d\n", soma);
    return 0;


}