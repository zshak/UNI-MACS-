from typing import Protocol

from creature import Creature


class FightAlgorithm(Protocol):
    def fight(self, pray: Creature, predator: Creature) -> Creature:
        pass


class DefaultSporeFightAlgorithm(FightAlgorithm):
    def fight(self, pray: Creature, predator: Creature) -> Creature:
        while True:
            predator.take_damage(pray.attack())
            if predator.current_health() < 0:
                return pray
            pray.take_damage(predator.attack())
            if pray.current_health() < 0:
                return predator
