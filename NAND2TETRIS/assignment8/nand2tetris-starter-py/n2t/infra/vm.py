from __future__ import annotations

import os
from dataclasses import dataclass
from pathlib import Path


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    import n2t.core.vm_translator.vmTranslatorII as vm_Translator

    path: Path
    file_name: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls(Path(file_or_directory_name), file_or_directory_name)

    def translate(self) -> None:
        res = self.vm_Translator.translate_vm_files(self.file_name)
        # for line in res:
        #     # print(line)

        # print(self.file_name[: len(self.file_name) - 2] + "asm")
        # file = open(self.file_name[: len(self.file_name) - 2] + "asm", "w")
        # file.writelines(res)
        # file.close()
        file_name_to_create = []
        if "/" in self.file_name:
            file_name_to_create = self.file_name.split("/")
        else:
            file_name_to_create = self.file_name.split("\\")

        if not os.path.isdir(self.file_name):
            print(" orr " + self.file_name)
            last = file_name_to_create[len(file_name_to_create) - 1]
            print(last)
            name = file_name_to_create[len(file_name_to_create) - 2]
            file = open(
                self.file_name[: len(self.file_name) - len(last)] + name + ".asm", "w"
            )
            file.writelines(res)
            file.close()
            return
        # print(self.file_name[: len(self.file_name) - 2] + "asm")
        last = file_name_to_create[len(file_name_to_create) - 1]
        file = open(os.path.join(self.file_name, last + ".asm"), "w")
        # file = open(self.file_name + last + ".asm", "w")

        file.writelines(res)
        file.close()
