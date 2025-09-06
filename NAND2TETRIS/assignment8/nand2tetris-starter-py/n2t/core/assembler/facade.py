from __future__ import annotations

import re
from dataclasses import dataclass
from typing import Dict, Iterable, List

dest_table = {
    "null": "000",
    "A": "100",
    "D": "010",
    "M": "001",
    "AD": "110",
    "AM": "101",
    "MD": "011",
    "AMD": "111",
}

comp_table = {
    "0": "0101010",
    "1": "0111111",
    "-1": "0111010",
    "D": "0001100",
    "A": "0110000",
    "M": "1110000",
    "!D": "0001101",
    "!A": "0110001",
    "!M": "1110001",
    "-D": "0001111",
    "-A": "0110011",
    "-M": "1110011",
    "D+1": "0011111",
    "A+1": "0110111",
    "M+1": "1110111",
    "D-1": "0001110",
    "A-1": "0110010",
    "M-1": "1110010",
    "D+A": "0000010",
    "D+M": "1000010",
    "D-A": "0010011",
    "D-M": "1010011",
    "A-D": "0000111",
    "M-D": "1000111",
    "D&A": "0000000",
    "D&M": "1000000",
    "D|A": "0010101",
    "D|M": "1010101",
}

jump_table = {
    "null": "000",
    "JGT": "001",
    "JEQ": "010",
    "JGE": "011",
    "JLT": "100",
    "JNE": "101",
    "JLE": "110",
    "JMP": "111",
}


# -----------------------------------
# -----------------------------------
# -----------------------------------


class symbolTable:
    def __init__(self) -> None:
        self.symbol_dic: Dict[str, int] = {}
        self.running_memory_address: int = 16
        self.initialize_symbol_table()

    def initialize_symbol_table(self) -> None:
        for i in range(16):
            self.add("R" + str(i), i)
        self.add("SCREEN", 16384)
        self.add("KBD", 24576)
        self.add("SP", 0)
        self.add("LCL", 1)
        self.add("ARG", 2)
        self.add("THIS", 3)
        self.add("THAT", 4)

    def add(self, key: str, value: int) -> None:
        self.symbol_dic[key] = value

    def getValue(self, key: str) -> int:
        if key in self.symbol_dic.keys():
            return self.symbol_dic[key]
        return -1


# ---------------------------------------------------
# ---------------------------------------------------


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        symbol_table = symbolTable()
        filtered_code: List[str] = self.parse_input(assembly, symbol_table)

        res = self.translate(filtered_code, symbol_table)

        # res[len(res) - 1] = res[len(res) - 1] + "/r"
        # res.append("\r")
        for word in res:
            print(word)
        return res

    def should_skip(self, word: str) -> bool:
        return word == "" or word[0] == "/"

    def parse_input(
        self, assembly: Iterable[str], symbol_table: symbolTable
    ) -> List[str]:
        res: List[str] = []
        index = 0
        for word in assembly:
            word = word.replace(" ", "")
            if self.should_skip(word):
                continue
            separator = "/"
            word = word.partition(separator)[0]
            # print(word)
            # word is a valid input
            if word[0] == "(":
                word = word.strip("(,)")
                symbol_table.add(word, index)
                continue
            index += 1
            res.append(word)
        return res

    def translate(
        self, filtered_code: List[str], symbol_table: symbolTable
    ) -> List[str]:
        res: List[str] = []
        for word in filtered_code:
            # print(word)
            self.process_word(word, res, symbol_table)
        return res

    def process_word(
        self, word: str, res: List[str], symbol_table: symbolTable
    ) -> None:
        # 3 types of word: predefined symbols, variable symbols, instuctions
        # if starts with @ and not in dictionary -> variable
        # if starts with @ and in dictionary -> replace with its value
        # else replace with instruction value

        res_str = ""
        if word[0] != "@":
            split_arr = re.split("[=;]", word)
            # print(split_arr)
            if len(split_arr) == 2:
                if split_arr[1][0] != "J":
                    res_str = (
                        "111"
                        + comp_table[split_arr[1]]
                        + dest_table[split_arr[0]]
                        + "000"
                    )
                else:
                    if not split_arr[0].isnumeric():
                        res_str = (
                            "111"
                            + comp_table[split_arr[0]]
                            + dest_table["null"]
                            + jump_table[split_arr[1]]
                        )
                    else:
                        res_str = (
                            "111"
                            + comp_table["0"]
                            + dest_table["null"]
                            + jump_table[split_arr[1]]
                        )

            else:
                res_str = (
                    "111"
                    + comp_table[split_arr[1]]
                    + dest_table[split_arr[0]]
                    + jump_table[split_arr[2]]
                )
            res.append(res_str)
            return

        # not instruction

        word = word[1:]
        # print(word)
        if word.isnumeric():
            binary_string = str(format(int(word), "b"))
            binary_string = binary_string.zfill(16)
            res.append(binary_string)
            return
        if symbol_table.getValue(word) != -1:
            res_word = str(format(symbol_table.getValue(word), "b"))
            res_word = res_word.zfill(16)
            res.append(res_word)
        else:
            binary_string = str(format(symbol_table.running_memory_address, "b"))
            binary_string = binary_string.zfill(16)
            res.append(binary_string)
            symbol_table.add(word, symbol_table.running_memory_address)
            symbol_table.running_memory_address += 1
        return
