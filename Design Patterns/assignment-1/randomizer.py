import random
from dataclasses import dataclass, field
from typing import Protocol

from stat_holder import ClassicSporeStatConstraints, ClassicSporeStatHolder


class RandomChooser(Protocol):
    def random_from_range(self, ran: tuple[int, int]) -> int:
        pass

    def random_from_list(self, ran: list[int]) -> int:
        pass


class DefaultRandomChooser(RandomChooser):
    def random_from_range(self, ran: tuple[int, int]) -> int:
        return random.randrange(ran[0], ran[1])

    def random_from_list(self, ran: list[int]) -> int:
        return random.choice(ran)


@dataclass
class ClassicSporeCreatureRandomizer:
    randomizer: RandomChooser = field(default_factory=DefaultRandomChooser)

    def randomize(
        self, stats: ClassicSporeStatHolder, location: tuple[int, int]
    ) -> None:
        self.__general_stats(stats, location)
        self.__abilities_stats(stats)

    def __abilities_stats(self, stats: ClassicSporeStatHolder) -> None:
        stats.legs = self.randomizer.random_from_list(
            ClassicSporeStatConstraints.LEGS_CONSTRAINT
        )
        stats.teeth = self.randomizer.random_from_list(
            ClassicSporeStatConstraints.TEETH_CONSTRAINT
        )
        stats.claws = self.randomizer.random_from_list(
            ClassicSporeStatConstraints.CLAWS_CONSTRAINT
        )
        stats.wings = self.randomizer.random_from_list(
            ClassicSporeStatConstraints.WINGS_CONSTRAINT
        )

    def __general_stats(
        self, stats: ClassicSporeStatHolder, location: tuple[int, int]
    ) -> None:
        stats.location = self.randomizer.random_from_range(location)
        stats.stamina = self.randomizer.random_from_range(
            ClassicSporeStatConstraints.STAMINA_CONSTRAINT
        )
        stats.health = self.randomizer.random_from_range(
            ClassicSporeStatConstraints.HEALTH_CONSTRAINT
        )
        stats.attack_power = self.randomizer.random_from_range(
            ClassicSporeStatConstraints.ATTACK_POWER_CONSTRAINT
        )
