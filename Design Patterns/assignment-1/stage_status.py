from typing import Protocol

from creature import Creature


class SporeStageStatus(Protocol):
    def message(self, pray: Creature, predator: Creature) -> str:
        pass

    def is_intermediate(self) -> bool:
        pass

    def is_terminal(self) -> bool:
        pass


class PreChaseStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = f"Pray Stats: {pray.stats()} \nPredator Stats: {predator.stats()}"
        return mess

    def is_intermediate(self) -> bool:
        return False

    def is_terminal(self) -> bool:
        return False


class ChaseStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = (
            f"Pray location: {pray.current_location()} "
            f"\nPredator Location: {predator.current_location()}"
        )
        return mess

    def is_intermediate(self) -> bool:
        return False

    def is_terminal(self) -> bool:
        return False


class PraySurvivedStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = "Pray ran into infinity"
        return mess

    def is_intermediate(self) -> bool:
        return False

    def is_terminal(self) -> bool:
        return True


class PredatorCaughtPrayStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = "Predator Caught Pray"
        return mess

    def is_intermediate(self) -> bool:
        return True

    def is_terminal(self) -> bool:
        return False


class PreFightStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = (
            f"They Will Fight! \n Pray Health: {pray.current_health()} "
            f"\n Predator Health: {pray.current_health()}"
        )
        return mess

    def is_intermediate(self) -> bool:
        return True

    def is_terminal(self) -> bool:
        return False


class PredatorWonFightStatus(SporeStageStatus):
    def message(self, pray: Creature, predator: Creature) -> str:
        mess = "Some R-rated things have happened"
        return mess

    def is_intermediate(self) -> bool:
        return False

    def is_terminal(self) -> bool:
        return True
