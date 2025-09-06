from collections import Counter


def calculate_score(sentence):
    # მეორე თეიბლი არ მუშაობდა რატომღაც ვერ მივხვდი

    english_letter_frequency_table = {
        'a': .08167, 'b': .01492, 'c': .02782, 'd': .04253,
        'e': .12702, 'f': .02228, 'g': .02015, 'h': .06094,
        'i': .06094, 'j': .00153, 'k': .00772, 'l': .04025,
        'm': .02406, 'n': .06749, 'o': .07507, 'p': .01929,
        'q': .00095, 'r': .05987, 's': .06327, 't': .09056,
        'u': .02758, 'v': .00978, 'w': .02360, 'x': .00150,
        'y': .01974, 'z': .00074, ' ': .13000
    }

    # pure_sentence = sentence.replace(" ", "").lower()
    pure_sentence_parsed = ''.join(ch for ch in sentence if ch.isalpha())

    res = 0
    for ch in sentence:
        res += english_letter_frequency_table[ch] if ch in english_letter_frequency_table else 0
    return res
    # sentence_freq = Counter(pure_sentence_parsed)
    # score = sum(english_letter_frequency_table[char] * sentence_freq[char] for char in sentence_freq)
    # return score


def find_key(input):
    key = None
    cur_score = float('-inf')
    cur_sentence = ''
    for try_key in range(256):
        xord = [try_key ^ byte for byte in input.encode()]
        sentence = bytes(xord).decode('utf-8', errors='ignore')
        score = calculate_score(sentence)
        if score > cur_score:
            key = try_key
            cur_score = score
            cur_sentence = sentence

    return key


def ham_dis(str1, str2):
    # bytes_1 = bytes(str1.encode('utf-8'))
    # binary_representation_1 = ''.join(format(byte, '08b') for byte in bytes_1)
    #
    # bytes_2 = bytes(str2.encode('utf-8'))
    # binary_representation_2 = ''.join(format(byte, '08b') for byte in bytes_2)

    binary_1 = [bin(byte)[2:].zfill(8) for byte in str1]
    binary_2 = [bin(byte)[2:].zfill(8) for byte in str2]
    d = sum(c1 != c2 for c1, c2 in zip(binary_1, binary_2))
    return d


def get_key_size(text):
    key_size = 2

    # text_to_bytes = bytes(text.encode('utf-8'))
    # binary_representation = ''.join(format(byte, '08b') for byte in text_to_bytes)
    # print(len(binary_representation))
    cur_key_size = 0
    cur_score = float('inf')
    while key_size <= 40:
        div = 0
        score = 0
        l = len(text)
        for i in range(0, len(text), key_size):
            first_part = text[i:i + key_size]
            second_part = text[i + key_size:i + 2 * key_size]
            ham_d = ham_dis(first_part, second_part)
            ham_d = ham_d / key_size
            score += ham_d
            div += 1

        score /= div
        if score < cur_score:
            cur_key_size = key_size
            cur_score = score
        key_size = key_size + 1
    return cur_key_size


def get_blocks_of_key_size(key_size, texttt):
    # text_to_bytes = bytes(texttt.encode('utf-8'))
    res = [texttt[i:i + key_size] for i in range(0, len(texttt), key_size)]
    print(res)
    return res


def transpose_blocks(blocks, keysize):
    transposed_blocks = [blocks[i::keysize] for i in range(keysize)]
    return transposed_blocks


def get_keys_from_blocks(transposed_blocks):
    res = []
    for block in transposed_blocks:
        if len(block) == 0:
            continue
        a = block[0].hex()

        res.append(find_key(a))
    return res


from base64 import b64decode


def decrypt(ciphertext, text):
    key_size = get_key_size(ciphertext)
    # blocks = get_blocks_of_key_size(key_size, text)
    # transposed_blocks = transpose_blocks(blocks, key_size)
    # keys = get_keys_from_blocks(transposed_blocks)
    # my_bytes = keys[0].to_bytes((keys[0].bit_length() + 7) // 8, byteorder='big')
    ranges = [""] * key_size
    # ranges = defaultdict(str)
    t = b64decode(text).decode()
    for i in range(len(t)):
        ranges[i % key_size] += t[i]

    key = ''.join([chr(find_key(r)) for r in ranges])

    return key


def repeated_xor(inp, key):
    encrypted_arr = []
    for ind, byte in enumerate(inp):
        encrypted_arr.append(chr(ord(byte) ^ ord(key[ind % len(key)])))

    return ''.join(encrypted_arr)


import base64
import itertools
from operator import itemgetter

text = input().strip()
ciphertext = bytes(text, 'utf_8')
keys = decrypt(ciphertext, text)
# arr = [bin(k) for k in keys]
b = repeated_xor(b64decode(text).decode(), keys)
print(b)
