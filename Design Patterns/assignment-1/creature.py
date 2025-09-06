from dataclasses import dataclass, field
from typing import Protocol

from move_algorithms import ClassicSporeMoveAlgorithm, FlyingMoveAlgorithm
from randomizer import ClassicSporeCreatureRandomizer
from stat_holder import ClassicSporeStatHolder


class Creature(Protocol):
    def spawn(self, location: tuple[int, int]) -> None:
        pass

    def current_location(self) -> int:
        pass

    def move(self) -> None:
        pass

    def attack(self) -> int:
        pass

    def current_health(self) -> int:
        pass

    def current_stamina(self) -> int:
        pass

    def take_damage(self, damage: int) -> None:
        pass

    def stats(self) -> str:
        pass


@dataclass
class ClassicSporeCreature(Creature):
    __creature_stats: ClassicSporeStatHolder = field(
        default_factory=ClassicSporeStatHolder
    )
    move_algorithm: ClassicSporeMoveAlgorithm = field(
        default_factory=FlyingMoveAlgorithm
    )

    def spawn(self, location: tuple[int, int]) -> None:
        randomizer = ClassicSporeCreatureRandomizer()
        randomizer.randomize(self.__creature_stats, location)

    def current_location(self) -> int:
        return self.__creature_stats.location

    def current_stamina(self) -> int:
        return self.__creature_stats.stamina

    def move(self) -> None:
        self.move_algorithm.move(self.__creature_stats)

    def attack(self) -> int:
        power = (
            self.__creature_stats.attack_power + self.__creature_stats.teeth
        ) * self.__creature_stats.claws
        return power

    def current_health(self) -> int:
        return self.__creature_stats.health

    def take_damage(self, damage: int) -> None:
        self.__creature_stats.health = self.__creature_stats.health - damage

    def stats(self) -> str:
        return self.__creature_stats.to_str()
