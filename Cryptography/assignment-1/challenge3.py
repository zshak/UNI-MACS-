
from collections import Counter
def calculate_score(sentence):

    #https://www3.nd.edu/~busiforc/handouts/cryptography/letterfrequencies.html

    english_letter_frequency_table= {
        'e': 12, 't': 9, 'a': 8, 'i': 8, 'n': 8,
        'o': 8, 's': 8, 'h': 6.4, 'r': 6.2, 'd': 4.4,
        'l': 4, 'u': 3.4, 'c': 3, 'm': 3, 'f': 2.5,
        'w': 2, 'y': 2, 'g': 1.7, 'p': 1.7, 'b': 1.6,
        'v': 1.2, 'k': 8, 'q': 5, 'j': 4, 'x': 4,
        'z': 2
    }

    pure_sentence = sentence.replace(" ", "")
    pure_sentence.lower()
    pure_sentence_parsed = ''.join(ch for ch in pure_sentence if ch.isalpha() and (97 <= ord(ch) <= 122))
    sentence_freq = Counter(pure_sentence_parsed)
    score = sum(english_letter_frequency_table[char] * sentence_freq[char] for char in sentence_freq)
    return score

def find_sentence(input_hex):
    bytes_inp = bytes.fromhex(input_hex)

    key = None
    cur_score = float('-inf')
    cur_sentence = ''
    for try_key in range(256):
        xord = [try_key ^ byte for byte in bytes_inp]
        sentence = bytes(xord).decode('utf-8', errors='ignore')
        score = calculate_score(sentence)
        if score > cur_score:
            key = key
            cur_score = score
            cur_sentence = sentence

    return cur_sentence

input_hex = input().strip()
print(find_sentence(input_hex))