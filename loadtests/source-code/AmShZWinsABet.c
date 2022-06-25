#include <stdio.h>
#include <stdlib.h>

#define MMEM 50
#define TYPE int
#define STACK struct stack

struct stack {
  TYPE *arr;
  TYPE size;
  TYPE memsize;
} st;

void pop (STACK *p);
TYPE top (STACK *p);
int strlen (char *p);
void needmem (STACK *p);
void push (STACK *p, TYPE el);
int can_we(int i, int j, int len, int *arr, char *s);

int main() {
  int len, arr[(int)3e5 + 5] = {};
  char s[(int)3e5 + 5];
  scanf("%s", s), st.arr = (TYPE *) malloc (sizeof (TYPE) * MMEM), st.memsize = MMEM;
  len = strlen(s), arr[len] = len, push(&st, len);
  for (int i = len - 1; ~i; i--)
    if (s[i] == '(') {
      arr[i] = i, pop(&st);
      if (!st.size)push(&st, i);
      else if (can_we(i, top(&st), len, arr, s))pop(&st), push(&st, i);
      else arr[i] = top(&st);
    }
    else push(&st, i), arr[i] = i;
  for (int i = 0; i < len; i = arr[i + 1])putchar(*(s + i));
  return 0;
}

int can_we(int i, int j, int len, int *arr, char *s) {
  while (i < j && j < len && s[i] == s[j]) i = arr[i + 1], j = arr[j + 1];
  return i < len && j < len ? s[i] <= s[j] : i == len && j < len;
}

void needmem (STACK *p) {
  TYPE *new_arr = (TYPE *) malloc (sizeof (TYPE) * (p -> memsize + MMEM));
  for (int i = 1; i <= p -> size; ++i)
    *(new_arr + i) = *(p -> arr + i);
  free(p -> arr), p -> arr = new_arr, p -> memsize += MMEM;
}

void push (STACK *p, TYPE el) {
  if (p -> size == p -> memsize - 1)needmem(p);
  ++(p -> size), *(p -> arr + p -> size) = el;
}

void pop (STACK *p) {
  --(p -> size);
}

TYPE top (STACK *p) {
  return *(p -> arr + p -> size);
}

int strlen (char *p) {
  int cnt = 0;
  while (*(p + cnt))++cnt;
  return cnt;
}