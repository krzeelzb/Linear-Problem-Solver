# Linear-Problem-Solver

Wejście:
1. Warunki ograniczające dla PL (będącego PP) o zmiennych decyzyjnych x1, x2,..., xn postaci: 

a1*x1 + a2*x2 + ... + an*xn <= c1 
b1*x1 + b2*x2 + ... + bn*xn <= c2

Zakładamy, że  x1, x2,..., xn >=0

2. Funkcja celu dla tegp PL: F(x1,x2,...,xn) = P1 * x1 + P2 *x2 + ... + Pn *xn -> max

Wyjście:
- Lista punktów ograniczających zbiór rozwiązań dopuszczalnych dla PD
- Punkt V = (x1, x2, ... , xn) realizujący optimum PP
- Wartość maksymalną: F(V)


Przykład dla:
0.5*x1 + 0.4*x2 + 0.4*x3 + 0.2*x4 <= 2000

0.4*x1 + 0.2*x2 + 0.5*x4 <= 2800

F(x1,x2,x3,x4) = 10*x1 + 14*x2 + 8*x3 + 11*x4 -> max
