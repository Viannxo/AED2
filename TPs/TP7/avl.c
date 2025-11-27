#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define CSV_PATH "/tmp/games.csv"
#define LINE_BUF 4096

typedef struct {
    int id;
    char *name;
} Record;

typedef struct {
    Record *data;
    size_t size;
    size_t cap;
} RecordArray;

void ra_init(RecordArray *ra) {
    ra->data = NULL;
    ra->size = 0;
    ra->cap = 0;
}

void ra_push(RecordArray *ra, int id, const char *name) {
    if (ra->size == ra->cap) {
        ra->cap = ra->cap == 0 ? 256 : ra->cap * 2;
        ra->data = realloc(ra->data, ra->cap * sizeof(Record));
        if (!ra->data) { perror("realloc"); exit(EXIT_FAILURE); }
    }
    ra->data[ra->size].id = id;
    ra->data[ra->size].name = strdup(name);
    if (!ra->data[ra->size].name) { perror("strdup"); exit(EXIT_FAILURE); }
    ra->size++;
}

void ra_free(RecordArray *ra) {
    for (size_t i = 0; i < ra->size; i++) free(ra->data[i].name);
    free(ra->data);
    ra->data = NULL;
    ra->size = ra->cap = 0;
}


int cmp_record(const void *a, const void *b) {
    const Record *ra = a;
    const Record *rb = b;
    if (ra->id < rb->id) return -1;
    if (ra->id > rb->id) return 1;
    return 0;
}

Record *ra_find(RecordArray *ra, int id) {
    size_t left = 0, right = ra->size;
    while (left < right) {
        size_t mid = (left + right) / 2;
        if (ra->data[mid].id == id) return &ra->data[mid];
        if (ra->data[mid].id < id) left = mid + 1;
        else right = mid;
    }
    return NULL;
}

//parsing do csv
int parse_id_name(const char *line, int *out_id, char *out_name, size_t name_cap) {
    const char *p = line;
    char field[LINE_BUF];
    int field_idx = 0;
    size_t field_pos = 0;
    int in_quotes = 0;

    // extrai campo 0 (id)
    field_pos = 0;
    in_quotes = 0;
    field_idx = 0;
    while (*p && (*p == ' ' || *p == '\t')) p++; // skip leading
    while (*p) {
        char c = *p;
        if (c == '"' ) {
            in_quotes = !in_quotes;
            p++;
            continue;
        }
        if (!in_quotes && c == ',') {
            break;
        }
        if (field_pos + 1 < sizeof(field)) field[field_pos++] = c;
        p++;
    }
    field[field_pos] = '\0';
    // parse id
    if (field_pos == 0) return 0;
    char *endptr;
    long id = strtol(field, &endptr, 10);
    if (*endptr != '\0' && *endptr != ' ' && *endptr != '\t') {
        return 0;
    }
    *out_id = (int) id;

    if (*p == ',') p++;
    field_pos = 0;
    in_quotes = 0;
    while (*p == ' ' || *p == '\t') p++;
    if (*p == '"') {
        in_quotes = 1;
        p++;
    }
    while (*p) {
        char c = *p;
        if (in_quotes) {
            if (c == '"') {
                if (*(p+1) == '"') {
                    if (field_pos + 1 < sizeof(field)) field[field_pos++] = '"';
                    p += 2;
                    continue;
                } else {
                    p++;
                    break;
                }
            } else {
                if (field_pos + 1 < sizeof(field)) field[field_pos++] = c;
                p++;
            }
        } else {
            if (c == ',') break;
            if (field_pos + 1 < sizeof(field)) field[field_pos++] = c;
            p++;
        }
    }
    field[field_pos] = '\0';
    size_t len = strlen(field);
    while (len > 0 && (field[len-1] == ' ' || field[len-1] == '\t')) field[--len] = '\0';
    strncpy(out_name, field, name_cap-1);
    out_name[name_cap-1] = '\0';
    return 1;
}

//avl por nome
typedef struct Node {
    char *name;
    struct Node *left, *right;
    int height;
} Node;

Node *root = NULL;

int node_height(Node *n) { return n ? n->height : 0; }
int max_int(int a, int b) { return a > b ? a : b; }

Node *new_node(const char *name) {
    Node *n = malloc(sizeof(Node));
    if (!n) { perror("malloc"); exit(EXIT_FAILURE); }
    n->name = strdup(name);
    if (!n->name) { perror("strdup"); exit(EXIT_FAILURE); }
    n->left = n->right = NULL;
    n->height = 1;
    return n;
}

Node *right_rotate(Node *y) {
    Node *x = y->left;
    Node *T2 = x->right;
    x->right = y;
    y->left = T2;
    y->height = max_int(node_height(y->left), node_height(y->right)) + 1;
    x->height = max_int(node_height(x->left), node_height(x->right)) + 1;
    return x;
}

Node *left_rotate(Node *x) {
    Node *y = x->right;
    Node *T2 = y->left;
    y->left = x;
    x->right = T2;
    x->height = max_int(node_height(x->left), node_height(x->right)) + 1;
    y->height = max_int(node_height(y->left), node_height(y->right)) + 1;
    return y;
}

int get_balance(Node *n) {
    if (!n) return 0;
    return node_height(n->left) - node_height(n->right);
}

Node *avl_insert(Node *node, const char *name) {
    if (node == NULL) return new_node(name);

    int cmp = strcmp(name, node->name);
    if (cmp < 0)
        node->left = avl_insert(node->left, name);
    else if (cmp > 0)
        node->right = avl_insert(node->right, name);
    else
        return node;

    node->height = 1 + max_int(node_height(node->left), node_height(node->right));

    int balance = get_balance(node);

    // Left Left
    if (balance > 1 && strcmp(name, node->left->name) < 0)
        return right_rotate(node);

    // Right Right
    if (balance < -1 && strcmp(name, node->right->name) > 0)
        return left_rotate(node);

    // Left Right
    if (balance > 1 && strcmp(name, node->left->name) > 0) {
        node->left = left_rotate(node->left);
        return right_rotate(node);
    }

    // Right Left
    if (balance < -1 && strcmp(name, node->right->name) < 0) {
        node->right = right_rotate(node->right);
        return left_rotate(node);
    }

    return node;
}

void avl_insert_root(const char *name) {
    root = avl_insert(root, name);
}

int avl_search_path_rec(const char *name, Node *node) {
    if (node == NULL) return 0;
    int cmp = strcmp(name, node->name);
    if (cmp == 0) {
        return 1;
    } else if (cmp < 0) {
        printf(" esq");
        return avl_search_path_rec(name, node->left);
    } else {
        printf(" dir");
        return avl_search_path_rec(name, node->right);
    }
}

void avl_search_print(const char *name) {
    printf("%s: raiz", name);
    int found = avl_search_path_rec(name, root);
    if (found) printf(" SIM\n");
    else printf(" NAO\n");
}

/* free tree */
void free_tree(Node *n) {
    if (!n) return;
    free_tree(n->left);
    free_tree(n->right);
    free(n->name);
    free(n);
}

// main
void trim_newline(char *s) {
    size_t l = strlen(s);
    while (l > 0 && (s[l-1] == '\n' || s[l-1] == '\r')) s[--l] = '\0';
}

int main(void) {
    FILE *csv = fopen(CSV_PATH, "r");
    if (!csv) {
        perror("Erro ao abrir CSV");
        return 1;
    }

    RecordArray ra;
    ra_init(&ra);

    char line[LINE_BUF];
    // lê header (se houver)
    if (!fgets(line, sizeof(line), csv)) {
        fclose(csv);
        fprintf(stderr, "CSV vazio ou inacessível\n");
        return 1;
    }
    // processa as linhas do CSV
    while (fgets(line, sizeof(line), csv)) {
        trim_newline(line);
        if (strlen(line) == 0) continue;
        int id;
        char namebuf[1024];
        if (parse_id_name(line, &id, namebuf, sizeof(namebuf))) {
            ra_push(&ra, id, namebuf);
        } else {
        }
    }
    fclose(csv);

    // ordena por id para busca binária
    qsort(ra.data, ra.size, sizeof(Record), cmp_record);

    /* 2) Ler IDs da entrada padrão e inserir os jogos correspondentes na AVL */
    char input[LINE_BUF];

    while (fgets(input, sizeof(input), stdin)) {
        trim_newline(input);
        if (strcmp(input, "FIM") == 0) break;
        if (strlen(input) == 0) continue;
        // tenta parsear inteiro
        char *endptr;
        long id = strtol(input, &endptr, 10);
        if (*endptr != '\0') continue; 
        Record *r = ra_find(&ra, (int)id);
        if (r != NULL) {
            avl_insert_root(r->name);
        }
    }

    while (fgets(input, sizeof(input), stdin)) {
        trim_newline(input);
        if (strcmp(input, "FIM") == 0) break;
        if (strlen(input) == 0) continue;
        avl_search_print(input);
    }

    /* libera recursos */
    free_tree(root);
    ra_free(&ra);

    return 0;
}
