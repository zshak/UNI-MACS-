from dataclasses import dataclass, field
from typing import Protocol

from stat_holder import ClassicSporeStatHolder


class ClassicSporeMoveAlgorithm(Protocol):
    def move(self, stats: ClassicSporeStatHolder) -> None:
        pass

    REQUIRED_STAMINA: int
    STAMINA_COST: int
    SPEED: int


@dataclass
class TerminalMoveAlgorithm(ClassicSporeMoveAlgorithm):
    REQUIRED_STAMINA: int = 0
    STAMINA_COST: int = 0
    SPEED: int = 0

    def move(self, stats: ClassicSporeStatHolder) -> None:
        pass


@dataclass
class CrawlingMoveAlgorithm(ClassicSporeMoveAlgorithm):
    next_algo: ClassicSporeMoveAlgorithm = field(default_factory=TerminalMoveAlgorithm)
    REQUIRED_STAMINA: int = 0
    STAMINA_COST: int = 1
    SPEED: int = 1

    def move(self, stats: ClassicSporeStatHolder) -> None:
        if stats.stamina > self.REQUIRED_STAMINA:
            move_action(stats, self)
        else:
            self.next_algo.move(stats)


@dataclass
class HoppingMoveAlgorithm(ClassicSporeMoveAlgorithm):
    next_algo: ClassicSporeMoveAlgorithm = field(default_factory=CrawlingMoveAlgorithm)
    REQUIRED_STAMINA: int = 20
    STAMINA_COST: int = 2
    SPEED: int = 3

    def move(self, stats: ClassicSporeStatHolder) -> None:
        if stats.stamina > self.REQUIRED_STAMINA:
            move_action(stats, self)
        else:
            self.next_algo.move(stats)


@dataclass
class WalkingMoveAlgorithm(ClassicSporeMoveAlgorithm):
    next_algo: ClassicSporeMoveAlgorithm = field(default_factory=HoppingMoveAlgorithm)
    REQUIRED_STAMINA: int = 40
    STAMINA_COST: int = 2
    SPEED: int = 4

    def move(self, stats: ClassicSporeStatHolder) -> None:
        if stats.stamina > self.REQUIRED_STAMINA:
            move_action(stats, self)
        else:
            self.next_algo.move(stats)


@dataclass
class RunningMoveAlgorithm(ClassicSporeMoveAlgorithm):
    next_algo: ClassicSporeMoveAlgorithm = field(default_factory=WalkingMoveAlgorithm)
    REQUIRED_STAMINA: int = 60
    STAMINA_COST: int = 4
    SPEED: int = 6

    def move(self, stats: ClassicSporeStatHolder) -> None:
        if stats.stamina > self.REQUIRED_STAMINA:
            move_action(stats, self)
        else:
            self.next_algo.move(stats)


@dataclass
class FlyingMoveAlgorithm(ClassicSporeMoveAlgorithm):
    next_algo: ClassicSporeMoveAlgorithm = field(default_factory=RunningMoveAlgorithm)
    REQUIRED_STAMINA: int = 80
    STAMINA_COST: int = 4
    SPEED: int = 8

    def move(self, stats: ClassicSporeStatHolder) -> None:
        if stats.stamina > self.REQUIRED_STAMINA:
            move_action(stats, self)
        else:
            self.next_algo.move(stats)


def move_action(stats: ClassicSporeStatHolder, algo: ClassicSporeMoveAlgorithm) -> None:
    stats.stamina = stats.stamina - algo.STAMINA_COST
    stats.location = stats.location + algo.SPEED
