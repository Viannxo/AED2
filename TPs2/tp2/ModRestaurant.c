#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>

typedef struct Data{
    int ano;
    int mes;
    int dia;
} Data;

Data parse_data(char * s){
    Data d;
    sscanf(s , " %d - %d - %d " , &d.ano , &d.mes , &d.dia);
    return d;
}

void formatar_data(Data* d , char * Buffer){
    sprintf(Buffer , "%02d - %02d - %04d" , d->dia, d->mes , d->ano);
}

typedef struct Hora{
    int hora;
    int minuto;
} Hora;

Hora parse_hora(char * s){
    Hora h;
    sscanf(s , " %d : %d " , &h.hora , &h.minuto);
    return h;
}

void formatar_hora(Hora* h , char * Buffer){
    sprintf(Buffer , "%02d : %02d" , h->hora , h->minuto);
}

typedef struct Price {
    int faixa;
} Price;

// Conta quantos '$' existem na linha string recebida do CSV
Price parse_price(char *s) {
    Price p;
    p.faixa = 0;
    while(*s) {
        if(*s == '$') p.faixa++;
        s++;
    }
    return p;
}

// Transforma o inteiro 'faixa' de volta em uma string de '$$$'
void formatar_price(Price* p, char *Buffer) {
    int i;
    for(i = 0; i < p->faixa; i++) {
        Buffer[i] = '$';
    }
    Buffer[i] = '\0'; // i já está na posição correta após o loop
}

typedef struct TiposCozinha {
    char **tipos;   // Equivalente ao String[] types
    int quantidade; // Equivalente ao types.length
} TiposCozinha;

void formatar_tipos(TiposCozinha *tc, char *buffer) {
    int offset = 0; // Controla a "posição" atual no buffer

    // Começa com o colchete inicial.
    offset += sprintf(buffer + offset, "[");

    for (int i = 0; i < tc->quantidade; i++) {
        // Escreve a string atual a partir de onde paramos (buffer + offset)
        offset += sprintf(buffer + offset, "%s", tc->tipos[i]);

        // Se não for o último, adiciona a vírgula
        if (i < tc->quantidade - 1) {
            offset += sprintf(buffer + offset, ",");
        }
    }

    // Fecha o colchete no final
    sprintf(buffer + offset, "]");
}
typedef struct Nome {
    char *nome;
} Nome;

void formatar_nome(Nome* n, char *Buffer) {
    sprintf(Buffer, "%s", n->nome);
}

typedef struct Capacidade{    
    int capacidade;
} Capacidade;

void formatar_capacidade(Capacidade* c, char *Buffer) {
    sprintf(Buffer, "%d", c->capacidade);
}

typedef struct avaliacao {
    float avaliacao;
} Avaliacao;

void formatar_avaliacao(Avaliacao* a, char *Buffer) {
    sprintf(Buffer, "%.1f", a->avaliacao);
}

typedef struct Id{
    int id;
}

void formatar_id(Id* i, char *Buffer) {
    sprintf(Buffer, "%d", i->id);
}

typedef struct Cidade {
    char *cidade;
} Cidade;

void formatar_cidade(Cidade* c, char *Buffer) {
    sprintf(Buffer, "%s", c->cidade);
}

typedef struct Funcionamento {
    bool funcionamento;
} Funcionamento;

void formatar_funcionamento(Funcionamento* f, char *Buffer) {
    sprintf(Buffer, "%s", f->funcionamento ? "true" : "false");
}

typedef struct Restaurante {
    int id;
    char nome[100];
    char cidade[100];
    int capacidade;
    float avaliacao;
    TiposCozinha tiposCozinha;
    Price preco;
    Hora horaAbertura;
    Hora horaFechamento;
    Data dataAbertura;
    bool funcionamento;
} Restaurante;

void formatar_restaurante(Restaurante *r, char *buffer) {
    char dataBuffer[20], horaABuffer[20], horaFBuffer[20];
    char precoBuffer[10], tiposBuffer[200], funcBuffer[10];

    formatar_data(&r->dataAbertura, dataBuffer);
    formatar_hora(&r->horaAbertura, horaABuffer);
    formatar_hora(&r->horaFechamento, horaFBuffer);
    formatar_price(&r->preco, precoBuffer);
    formatar_tipos(&r->tiposCozinha, tiposBuffer);
    formatar_funcionamento(r->funcionamento, funcBuffer);

    // Substituído %b por %s e adicionados os buffers corretos
    sprintf(buffer, "[=> %d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %s-%s ## %s ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao, 
            tiposBuffer, precoBuffer, horaABuffer, horaFBuffer, dataBuffer, funcBuffer);
}

typedef struct ColecaoRestaurantes {
    Restaurante array[1000];
    int n;
} ColecaoRestaurantes;

Restaurante parse_restaurante(char *linha) {
    Restaurante r;
    char *campos[15];
    int idx = 0;
    
    // Truque para separar por vírgulas sem usar strtok
    campos[idx++] = linha;
    for (int i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            linha[i] = '\0'; // Quebra a string original
            campos[idx++] = &linha[i + 1];
        } else if (linha[i] == '\n' || linha[i] == '\r') {
            linha[i] = '\0'; // Remove quebra de linha
        }
    }

    // Convertendo os dados usando sscanf e sprintf (como strcpy)
    sscanf(campos[0], "%d", &r.id);
    sprintf(r.nome, "%s", campos[1]);
    sprintf(r.cidade, "%s", campos[2]);
    sscanf(campos[3], "%d", &r.capacidade);
    sscanf(campos[4], "%f", &r.avaliacao);
    
    // Tratando Tipos de Cozinha (assumindo que seja campo 5 e separados por '/')
    r.tiposCozinha.quantidade = 0;
    char *tipoPtr = campos[5];
    char *inicioTipo = tipoPtr;
    for (int i = 0; ; i++) {
        if (tipoPtr[i] == '/' || tipoPtr[i] == '\0') {
            char backup = tipoPtr[i];
            tipoPtr[i] = '\0';
            sprintf(r.tiposCozinha.tipos[r.tiposCozinha.quantidade++], "%s", inicioTipo);
            if (backup == '\0') break;
            inicioTipo = &tipoPtr[i + 1];
        }
    }

    sscanf(campos[6], "%d", &r.preco.faixa);
    sscanf(campos[7], "%d:%d", &r.horaAbertura.hora, &r.horaAbertura.minuto);
    sscanf(campos[8], "%d:%d", &r.horaFechamento.hora, &r.horaFechamento.minuto);
    sscanf(campos[9], "%d-%d-%d", &r.dataAbertura.ano, &r.dataAbertura.mes, &r.dataAbertura.dia);
    
    // Boolean
    r.funcionamento = (campos[10][0] == 't' || campos[10][0] == 'T');

    return r;
}

// Equivalente ao seu lerCsv(String path)
void ler_csv(ColecaoRestaurantes *colecao, const char *path) {
    FILE *file = fopen(path, "r");
    if (!file) return; // Silencioso como no seu catch block

    char linha[1024];
    
    // Pula o cabeçalho
    if (fgets(linha, sizeof(linha), file) != NULL) {
        while (fgets(linha, sizeof(linha), file) != NULL && colecao->n < 1000) {
            colecao->array[colecao->n++] = parse_restaurante(linha);
        }
    }
    fclose(file);
}


int main(){
    ColecaoRestaurantes colecao;
    colecao.n = 0;

    // Carrega os dados para a memória
    ler_csv(&colecao, "/tmp/restaurantes.csv");

    int idBusca;
    char bufferImpressao[500];

    // Equivalente ao: while (sc.nextInt() != -1)
    while (scanf("%d", &idBusca) == 1 && idBusca != -1) {
        
        // Busca o restaurante
        for (int i = 0; i < colecao.n; i++) {
            if (colecao.array[i].id == idBusca) {
                formatar_restaurante(&colecao.array[i], bufferImpressao);
                printf("%s\n", bufferImpressao);
                break;
            }
        }
    }

    return 0;
}