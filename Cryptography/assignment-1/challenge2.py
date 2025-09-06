input1_hex = input().strip()
input2_hex = input().strip()

input1_bytes = bytes.fromhex(input1_hex)
input2_bytes = bytes.fromhex(input2_hex)

xor_bytes = bytearray()

for ind,byte in enumerate(input1_bytes):
    inp1_int = int(byte)
    inp2_int = int(input2_bytes[ind])
    xor_bytes.append(inp1_int ^ inp2_int)


result_hex = xor_bytes.hex()

print(result_hex)