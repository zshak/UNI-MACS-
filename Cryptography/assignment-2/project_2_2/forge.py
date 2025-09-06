import sys

from oracle import Mac, Oracle_Connect, Vrfy, Oracle_Disconnect


file_name = sys.argv[1]
file = open(file_name)
content = file.read()

content = content.strip()


def xor(block1, block2):
    res = ''.join([chr(block1[i]^block2[i]) for i in range(len(block1))])
    return res

def get_mac_forgery(content):

    #initialize 16 bytes obj with 0s
    cur_mac = bytes(16)

    # iterate by 2 blocks
    for bloc_ind in range(0, len(content), 32):
        first_block = content[bloc_ind:bloc_ind + 16]
        second_block = content[bloc_ind + 16:bloc_ind + 32]
        assert len(first_block) == len(second_block)

        first_block_bytes = first_block.encode()
        first_block_xord = xor(first_block_bytes, cur_mac)
        message = first_block_xord + second_block
        cur_mac = Mac(message, 32)
    return cur_mac

Oracle_Connect()
# content = "I, the server, hereby agree that I will pay $100 to this student"
res = get_mac_forgery(content)
print(res.hex())
Oracle_Disconnect()