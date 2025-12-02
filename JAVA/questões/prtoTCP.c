#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int num;
    char line[20];
} Packet;

int cmp(const void *a, const void *b) {
    Packet *pa = (Packet*)a;
    Packet *pb = (Packet*)b;
    return pa->num - pb->num;
}

int main() {
    char buffer[20];

    while (1) {
        printf("Digite 1 para novo caso, 0 para sair: ");
        scanf("%s", buffer);
        if (strcmp(buffer, "0") == 0) break;

        Packet arr[2000];
        int count = 0;

        printf("Digite pacotes ('Package XXX'), ou 0 para finalizar caso:\n");
        while (1) {
            scanf("%s", buffer);
            if (strcmp(buffer, "0") == 0) break;

            int num;
            scanf("%d", &num);

            arr[count].num = num;
            sprintf(arr[count].line, "Package %03d", num);
            count++;
        }

        qsort(arr, count, sizeof(Packet), cmp);

        printf("\nPacotes ordenados:\n");
        for (int i = 0; i < count; i++)
            printf("%s\n", arr[i].line);

        printf("\n");
    }

    return 0;
}
