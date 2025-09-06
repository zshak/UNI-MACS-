from typing import List


def correct(lines: List[str]) -> List[str]:
    cleaned_lines = []
    for line in lines:
        line.strip()
        if line == "" or line[0] == "/":
            continue
        separator = "/"
        line = line.partition(separator)[0]
        cleaned_lines.append(line)
    return cleaned_lines


def get_instructions(file_name: str) -> List[str]:
    with open(file_name, "r") as file:
        fl = file.read()
    lines = fl.split("\n")
    lines = correct(lines)
    return lines
