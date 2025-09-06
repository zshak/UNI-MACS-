import math

from oracle import *
from helper import *


def get_divisors(m):
    for i in range(2, m):
        if m % i == 0:
            sec_div = m.__floordiv__(i)
            return (i, sec_div)


def get_signature(m, n):
    # if m = a * b, m x00 = a x00 * bx00 // 1x00

    (a, b) = get_divisors(m)
    first_sign = Sign(a)
    second_sign = Sign(b)

    extra_multiplication = pow(Sign(1), -1, n)
    res = (extra_multiplication * first_sign * second_sign) % n
    verified = Verify(m, res)
    assert verified == 1
    return res

def main():
    with open('project_3_2/input.txt', 'r') as f:
        n = int(f.readline().strip())
        msg = f.readline().strip()

    Oracle_Connect()

    m = ascii_to_int(msg)
    sigma = get_signature(m, n)

    print(sigma)

    Oracle_Disconnect()


if __name__ == '__main__':
    main()
