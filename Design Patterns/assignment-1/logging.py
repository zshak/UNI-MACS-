from typing import Protocol


class Logger(Protocol):
    def log(self, message: str) -> None:
        pass


class ConsoleLogger(Logger):
    def log(self, message: str) -> None:
        print(message)
