from __future__ import annotations

import typing
from dataclasses import dataclass, field
from pathlib import Path
from typing import Dict, Iterable, List


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    path: Path
    file_name: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls(Path(file_or_directory_name), file_or_directory_name)

    line_diffenertial = 0
    TYPE_TO_FUNCTION: Dict[str, typing.Callable[[str], str]] = field(
        default_factory=Dict
    )
    SEGMENTS: Dict[str, str] = field(default_factory=Dict)

    def correct(self, lines: Iterable[str]) -> List[str]:
        cleaned_lines = []
        for line in lines:
            line.strip()
            if line[0] == "/" or line == "":
                continue
            cleaned_lines.append(line)
        return cleaned_lines

    def add_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = M + D"
            + "\n"
            + "@SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        return asm_code

    def sub_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = M - D"
            + "\n"
            + "@SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        return asm_code

    def neg_function(self, instruction: str) -> str:
        asm_code = "@SP" + "\n" + "A = M - 1" + "\n" + "D = M" + "\n" + "M = -D" + "\n"
        return asm_code

    def eq_function(self, instruction: str) -> str:
        # global line_diffenertial
        asm_code = (
            "@SP"
            + "\n"
            + "A = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M - D"
            + "\n"
            + "@PUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "D;JEQ"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = 0"
            + "\n"
            + "@JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "0,JMP"
            + "\n"
            + "(PUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = -1"
            + "\n"
            + "(JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        self.line_diffenertial += 1
        return asm_code

    def gt_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M - D"
            + "\n"
            + "@PUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "D;JGT"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = 0"
            + "\n"
            + "@JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "0,JMP"
            + "\n"
            + "(PUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = -1"
            + "\n"
            + "(JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        self.line_diffenertial += 1
        return asm_code

    def lt_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M - D"
            + "\n"
            + "@PUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "D;JLT"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = 0"
            + "\n"
            + "@JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + "\n"
            + "0,JMP"
            + "\n"
            + "(PUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "A = M - 1"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = -1"
            + "\n"
            + "(JUMPPUTTRUE"
            + str(self.line_diffenertial)
            + ")"
            + "\n"
            + "@SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        self.line_diffenertial += 1
        return asm_code

    def and_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = M&D"
            + "\n"
            + "SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        return asm_code

    def or_function(self, instruction: str) -> str:
        asm_code = (
            "@SP"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "D = M"
            + "\n"
            + "A = A - 1"
            + "\n"
            + "M = M|D"
            + "\n"
            + "SP"
            + "\n"
            + "M = M - 1"
            + "\n"
        )
        return asm_code

    def not_function(self, instruction: str) -> str:
        asm_code = "@SP" + "\n" + "A = A - 1" + "\n" + "M = !M" + "\n"
        return asm_code

    def push_function(self, instruction: str) -> str:
        inst = instruction.split()
        segment = inst[1]
        addr = inst[2]
        asm_code = ""
        if segment in self.SEGMENTS:
            segment_loc = self.SEGMENTS[segment]
            asm_code = (
                "@"
                + addr
                + "\n"
                + "D = A"
                + "\n"
                + "@"
                + segment_loc
                + "\n"
                + "A = M"
                + "\n"
                + "A = A + D"
                + "\n"
                + "D = M"
                + "\n"
                + "@SP"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M + 1"
                + "\n"
            )
        elif segment == "constant":
            asm_code = (
                "@"
                + addr
                + "\n"
                + "D = A"
                + "\n"
                + "@SP"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M + 1"
                + "\n"
            )
        elif segment == "temp":
            asm_code = (
                "@5"
                + "\n"
                + "D = A"
                + "\n"
                + "@"
                + addr
                + "\n"
                + "A = D + A"
                + "\n"
                + "D = M"
                + "\n"
                + "@SP"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M + 1"
                + "\n"
            )
        elif segment == "pointer":
            choose = "THIS"
            if addr == "1":
                choose = "THAT"
            asm_code = (
                "@"
                + choose
                + "\n"
                + "D = M"
                + "\n"
                + "@SP"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M + 1"
                + "\n"
            )
        elif segment == "static":
            asm_code = (
                "@"
                + self.file_name.split("\\")[-1][:-3]
                + "."
                + addr
                + "\n"
                + "D=M"
                + "\n"
                + "@SP"
                + "\n"
                + "A=M"
                + "\n"
                + "M=D"
                + "\n"
                + "@SP"
                + "\n"
                + "M=M+1"
                + "\n"
            )

        return asm_code

    def pop_function(self, instruction: str) -> str:
        inst = instruction.split()
        segment = inst[1]
        addr = inst[2]
        asm_code = ""
        if segment in self.SEGMENTS:
            segment_loc = self.SEGMENTS[segment]
            asm_code = (
                "@"
                + addr
                + "\n"
                + "D = A"
                + "\n"
                + "@"
                + segment_loc
                + "\n"
                + "A = M"
                + "\n"
                + "D = D + A"
                + "\n"
                + "@R13"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M - 1"
                + "\n"
                + "A = M"
                + "\n"
                + "D = M"
                + "\n"
                + "@R13"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
            )
        elif segment == "temp":
            asm_code = (
                "@5"
                + "\n"
                + "D = A"
                + "\n"
                + "@"
                + addr
                + "\n"
                + "D = D + A"
                + "\n"
                + "@R13"
                + "\n"
                + "M = D"
                + "\n"
                + "@SP"
                + "\n"
                + "M = M - 1"
                + "\n"
                + "A = M"
                + "\n"
                + "D = M"
                + "\n"
                + "@R13"
                + "\n"
                + "A = M"
                + "\n"
                + "M = D"
                + "\n"
            )
        elif segment == "pointer":
            choose = "THIS"
            if addr == "1":
                choose = "THAT"
            asm_code = (
                "@SP"
                + "\n"
                + "M = M - 1"
                + "\n"
                + "A = M"
                + "\n"
                + "D = M"
                + "\n"
                + "@"
                + choose
                + "\n"
                + "M = D"
                + "\n"
            )
        elif segment == "static":
            asm_code = (
                "@SP"
                + "\n"
                + "M=M - 1"
                + "\n"
                + "A=M"
                + "\n"
                + "D=M"
                + "\n"
                + "@"
                + self.file_name.split("\\")[-1][:-3]
                + "."
                + addr
                + "\n"
                + "M = D"
                + "\n"
            )

        return asm_code

    def convert_to_asm(self, instruction: str) -> str:
        toAsm = "//" + instruction + "\n"
        inst_type = instruction.split()[0]
        toAsm += (self.TYPE_TO_FUNCTION)[inst_type](instruction)
        return toAsm

    def Parse(self, lines: Iterable[str]) -> None:
        parsed_lines = self.correct(lines)
        assembly_instructions = [
            self.convert_to_asm(instruction) for instruction in parsed_lines
        ]
        for line in assembly_instructions:
            print(line)
        file = open(self.file_name[: len(self.file_name) - 2] + "asm", "w")
        file.writelines(assembly_instructions)
        file.close()

    def translate(self) -> None:
        self.TYPE_TO_FUNCTION = {
            "add": self.add_function,
            "sub": self.sub_function,
            "neg": self.neg_function,
            "eq": self.eq_function,
            "gt": self.gt_function,
            "lt": self.lt_function,
            "and": self.and_function,
            "or": self.or_function,
            "not": self.not_function,
            "push": self.push_function,
            "pop": self.pop_function,
        }

        self.SEGMENTS = {
            "local": "LCL",
            "argument": "ARG",
            "this": "THIS",
            "that": "THAT",
        }
        with open(self.file_name, "r") as file:
            fl = file.read()
        lines = fl.split("\n")
        self.Parse(lines)
