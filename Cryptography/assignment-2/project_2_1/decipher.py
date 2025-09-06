import copy
import math
import sys
from oracle import *
from subprocess import Popen

__block_len = 16


def init_info(content):
    cipher_t_bytes = []
    for ind in range(0, len(content), 2):
        cipher_t_bytes.append(int(content[ind: ind + 2], __block_len))

    num_complete_blocks = len(cipher_t_bytes) / __block_len
    num_blocks = int(math.ceil(num_complete_blocks))
    blocks = []
    for ind in range(num_blocks):
        blocks.append(cipher_t_bytes[ind * __block_len: (ind + 1) * __block_len])

    return (cipher_t_bytes, blocks)


def get_padding_len(cipher_bytes, blocks):
    blocks_copy = copy.deepcopy(blocks)
    num_bytes = len(blocks_copy)
    last_byte = blocks_copy[num_bytes - 1]
    byte_to_manipulate = blocks_copy[num_bytes - 2]

    for i in range(0, __block_len, 1):
        byte_to_manipulate[i] += -1  # nebismieri cvlileba sheidzleba
        request_cipher = byte_to_manipulate + last_byte
        rc = Oracle_Send(request_cipher, 2)
        if rc == 0:
            padding_len = __block_len - i
            return padding_len
    return __block_len


def decipher(first_block, second_block, padding_len):
    a = Oracle_Send(first_block + second_block, 2)
    encoded_data = [-1] * (__block_len - padding_len) + [padding_len] * padding_len

    for guess_index in range(__block_len - padding_len - 1, -1, -1):
        guess_enc_data_index(encoded_data, first_block, guess_index, padding_len, second_block)

    return "".join([chr(ch) for ch in encoded_data])


def guess_enc_data_index(encoded_data, first_block, guess_index, padding_len, second_block):
    first_copy = copy.deepcopy(first_block)
    manipulated_pad_len = padding_len + (__block_len - padding_len - 1) - guess_index + 1
    change_iv_padding(encoded_data, first_copy, guess_index, manipulated_pad_len)
    guess_val(encoded_data, first_block, first_copy, guess_index, manipulated_pad_len, second_block)


def guess_val(encoded_data, first_block, first_copy, guess_index, manipulated_pad_len, second_block):
    # try every value
    for guess in range(0, 256):
        first_copy[guess_index] = guess
        rc = Oracle_Send(first_copy + second_block, 2)
        if rc == 1:
            encoded_data[guess_index] = manipulated_pad_len ^ (first_block[guess_index] ^ first_copy[guess_index])
            break


def change_iv_padding(encoded_data, first_copy, guess_index, manipulated_pad_len):
    # change first block to alter padding
    for first_block_index in range(guess_index + 1, __block_len, 1):
        first_copy[first_block_index] = first_copy[first_block_index] ^ manipulated_pad_len ^ encoded_data[
            first_block_index]


Oracle_Connect()
file_name = sys.argv[1]
file = open(file_name)
content = file.read()

(cipher_bytes, blocks) = init_info(content)
padding_len = get_padding_len(cipher_bytes, blocks)

deciphered_blocks = ""

for i in range(0, len(blocks) - 1, 1):
    if i is not len(blocks) - 2:
        deciphered_blocks += (decipher(blocks[i], blocks[i + 1], 0))
    else:
        deciphered_blocks += (decipher(blocks[i], blocks[i + 1], padding_len))

print(deciphered_blocks.strip())
Oracle_Disconnect()
