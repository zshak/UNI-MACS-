import os.path
import typing
from collections import defaultdict
from typing import List, Union

import n2t.core.vm_translator.Parser as parser
from n2t.core.vm_translator.translator import Translator

file = ""
vmTranslatorI: Translator = Translator(file)

instruction_to_function: dict[
    str, Union[typing.Callable[[str], str], typing.Callable[[str, str], str]]
] = {}
function_calls: dict[str, int] = defaultdict(int)
vmI_instructions: dict[
    str, Union[typing.Callable[[str], str], typing.Callable[[str, str], str]]
] = {}


def __push(ad: str) -> str:
    asm_code = (
        "@"
        + ad
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


def __load(fun: str, dis: str) -> str:
    asm_code = (
        "@LCL"
        + "\n"
        + "D=M"
        + "\n"
        + "@"
        + dis
        + "\n"
        + "A=D-A"
        + "\n"
        + "D=M"
        + "\n"
        + "@"
        + fun
        + "\n"
        + "M=D"
        + "\n"
    )
    return asm_code


def get_boot_code(input_path: str) -> str:
    boot_code = "//SP = 256\n"
    if os.path.isdir(input_path):
        boot_code += (
            "@256"
            + "\n"
            + "D=A"
            + "\n"
            + "@SP"
            + "\n"
            + "M=D"
            + "\n"
            + "\n//call Sys.init 0\n"
            + call_function("call Sys.init 0")
        )
    return boot_code


def load_four() -> str:
    return (
        __load("THAT", "1")
        + __load("THIS", "2")
        + __load("ARG", "3")
        + __load("LCL", "4")
    )


def return_function(vm_instruction: str) -> str:
    if "/" in file:
        file_name_to_create = file.split("/")
    else:
        file_name_to_create = file.split("\\")
    file_n = file_name_to_create[len(file_name_to_create) - 1]
    translation = (
        __load("R14", "5")
        + vmTranslatorI.pop_function(
            "pop argument 0", os.path.join(file, file_n + ".vm")
        )
        + "@ARG"
        + "\n"
        + "D=M+1"
        + "\n"
        + "@SP"
        + "\n"
        + "M=D"
        + "\n"
        + load_four()
        + "@R14"
        + "\n"
        + "A=M"
        + "\n"
        + "0;JMP"
        + "\n"
    )
    return translation


def call_function(vm_instruction: str) -> str:
    print(vm_instruction)
    _, function_name, argument_number = vm_instruction.split()
    lb = function_name + "$ret." + str(function_calls[function_name])
    translation = (
        "@"
        + lb
        + "\n"
        + "D=A"
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
        + __push("LCL")
        + __push("ARG")
        + __push("THIS")
        + __push("THAT")
        + "@"
        + argument_number
        + "\n"
        + "D=A"
        + "\n"
        + "@5"
        + "\n"
        + "D=D+A"
        + "\n"
        + "@SP"
        + "\n"
        + "A=M"
        + "\n"
        + "D=A-D"
        + "\n"
        + "@ARG"
        + "\n"
        + "M=D"
        + "\n"
        + "@SP"
        + "\n"
        + "D=M"
        + "\n"
        + "@LCL"
        + "\n"
        + "M=D"
        + "\n"
        + goto_function("goto " + function_name)
        + label_function("label " + lb)
    )
    function_calls[function_name] += 1
    return translation


def function_function(vm_instruction: str) -> str:
    _, function_name, variable_number = vm_instruction.split()
    translation = label_function("label " + function_name)
    for i in range(int(variable_number)):
        translation += vmTranslatorI.push_function("push constant 0", file)
    return translation


def if_goto_function(vm_instruction: str) -> str:
    _, label = vm_instruction.split()
    asm_code = (
        "@SP"
        + "\n"
        + "M=M-1"
        + "\n"
        + "A=M"
        + "\n"
        + "D=M"
        + "\n"
        + "@"
        + label
        + "\n"
        + "D;JNE"
        + "\n"
    )
    return asm_code


def goto_function(vm_instruction: str) -> str:
    _, label = vm_instruction.split()
    asm_code = "@" + label + "\n" + "0;JMP" + "\n"
    return asm_code


def label_function(vm_instruction: str) -> str:
    _, label = vm_instruction.split()
    asm_code = "(" + label + ")\n"
    return asm_code


def get_asm_instructions(
    vm_instruction: str,
    instruction_to_function: dict[
        str, Union[typing.Callable[[str], str], typing.Callable[[str, str], str]]
    ],
) -> str:
    asm_code = "//" + vm_instruction + "\n"
    type_to_call = vm_instruction.split()[0]
    # print(type_to_call)

    if type_to_call in vmI_instructions:
        if type_to_call == "push":
            if "/" in file:
                file_name_to_create = file.split("/")
            else:
                file_name_to_create = file.split("\\")
            file_n = file_name_to_create[len(file_name_to_create) - 1]
            asm_code += vmTranslatorI.push_function(
                vm_instruction, os.path.join(file, file_n + ".vm")
            )
        elif type_to_call == "pop":
            if "/" in file:
                file_name_to_create = file.split("/")
            else:
                file_name_to_create = file.split("\\")
            file_n = file_name_to_create[len(file_name_to_create) - 1]
            asm_code += vmTranslatorI.pop_function(
                vm_instruction, os.path.join(file, file_n + ".vm")
            )
        else:
            asm_code += vmI_instructions[type_to_call](vm_instruction)
    else:
        asm_code += instruction_to_function[type_to_call](vm_instruction)

    return asm_code


def translate_vm_files(file_name: str) -> List[str]:
    asm_instructions: list[str] = []
    global file
    file = file_name
    global instruction_to_function
    global vmTranslatorI
    print(file_name)
    instruction_to_function = {
        "label": label_function,
        "goto": goto_function,
        "if-goto": if_goto_function,
        "function": function_function,
        "call": call_function,
        "return": return_function,
    }

    global vmI_instructions
    vmI_instructions = {
        "add": vmTranslatorI.add_function,
        "sub": vmTranslatorI.sub_function,
        "neg": vmTranslatorI.neg_function,
        "eq": vmTranslatorI.eq_function,
        "gt": vmTranslatorI.gt_function,
        "lt": vmTranslatorI.lt_function,
        "and": vmTranslatorI.and_function,
        "or": vmTranslatorI.or_function,
        "not": vmTranslatorI.not_function,
        "push": vmTranslatorI.push_function,
        "pop": vmTranslatorI.pop_function,
    }
    file_name_to_create = []
    if "/" in file_name:
        file_name_to_create = file_name.split("/")
    else:
        file_name_to_create = file_name.split("\\")
    file_n = file_name_to_create[len(file_name_to_create) - 1]

    asm_instructions += get_boot_code(file_name)
    if not os.path.isdir(file_name):
        vmTranslatorI = Translator(file_name)
        vm_instructions = parser.get_instructions(file_name)
        asm_instructions = [
            get_asm_instructions(vm_instruction, instruction_to_function)
            for vm_instruction in vm_instructions
        ]
    else:
        for file in os.listdir(file_name):
            if file.endswith(".vm"):
                print("file: " + os.path.join(file_name, file_n + ".vm"))
                vmTranslatorI = Translator(os.path.join(file_name, file_n + ".vm"))
                vm_instructions = parser.get_instructions(os.path.join(file_name, file))
                asm_instructions += [
                    get_asm_instructions(vm_instruction, instruction_to_function)
                    for vm_instruction in vm_instructions
                ]
    return asm_instructions
