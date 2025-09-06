
def repeated_xor(inp, key):
    inp_bytes = inp.encode('utf-8')
    key = key.encode('utf-8')
    encrypted_arr = []
    for ind, byte in enumerate(inp_bytes):
        encrypted_arr.append(byte ^ key[ind % len(key)])

    encrypted_bytes = bytes(encrypted_arr)

    return encrypted_bytes.hex()



key = input().strip()
inp = input().strip()

print(repeated_xor(inp, key))
