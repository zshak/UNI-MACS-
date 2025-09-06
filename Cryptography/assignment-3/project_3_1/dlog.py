import math


# Find x such that g^x = h (mod p)
# 0 <= x <= max_x
def discrete_log(p, g, h, max_x):
    # value -> x1
    left_side_vals = {}

    to_multiply_by = pow(g, -1, p)  # 1/g (mod p)

    B = math.ceil(math.sqrt(max_x))
    cur_val = h
    left_side_vals[cur_val] = 0
    for x1 in range(1, B + 1):
        val = (cur_val * to_multiply_by) % p
        left_side_vals[val] = x1
        cur_val = val

    g_pow_b = pow(g, B, p)  # g^b mod p
    cur_val = 1
    if cur_val in left_side_vals:
        return left_side_vals[cur_val]

    for x0 in range(1, B + 1):
        cur_val = (cur_val * g_pow_b) % p
        if cur_val in left_side_vals:
            return x0 * B + left_side_vals[cur_val]


def main():
    p = int(input().strip())
    g = int(input().strip())
    h = int(input().strip())
    max_x = 1 << 40  # 2^40

    dlog = discrete_log(p, g, h, max_x)
    print(dlog)


if __name__ == '__main__':
    main()
