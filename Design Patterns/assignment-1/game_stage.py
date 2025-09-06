from dataclasses import dataclass, field
from typing import List, Protocol

from creature import Creature
from fight_algorithms import DefaultSporeFightAlgorithm, FightAlgorithm
from stage_status import (
    ChaseStatus,
    PraySurvivedStatus,
    PreChaseStatus,
    PredatorWonFightStatus,
    PreFightStatus,
    SporeStageStatus,
)


class ClassicSporeGameStage(Protocol):
    def action(self, pray: Creature, predator: Creature) -> None:
        pass

    def is_game_over(self) -> bool:
        pass

    def current_status(self, pray: Creature, predator: Creature) -> str:
        pass


class ClassicSporeGameMultiStage(ClassicSporeGameStage, Protocol):
    def is_stage_over(self) -> bool:
        pass


@dataclass
class ClassicSporeFightStage(ClassicSporeGameMultiStage):
    stage_status: SporeStageStatus = field(default_factory=PreFightStatus)
    fight_algorithm: FightAlgorithm = field(default_factory=DefaultSporeFightAlgorithm)

    def action(self, pray: Creature, predator: Creature) -> None:
        winner = self.fight_algorithm.fight(pray=pray, predator=predator)
        if winner == pray:
            self.stage_status = PraySurvivedStatus()
        else:
            self.stage_status = PredatorWonFightStatus()

    def is_game_over(self) -> bool:
        return self.stage_status.is_terminal()

    def is_stage_over(self) -> bool:
        return self.stage_status.is_intermediate()

    def current_status(self, pray: Creature, predator: Creature) -> str:
        return self.stage_status.message(pray, predator)


@dataclass
class ClassicSporeChaseStage(ClassicSporeGameMultiStage):
    stage_status: SporeStageStatus = field(default_factory=PreChaseStatus)

    def action(self, pray: Creature, predator: Creature) -> None:
        if predator.current_location() >= pray.current_location():
            self.stage_status = PreFightStatus()
            return
        if predator.current_stamina() <= 0:
            self.stage_status = PraySurvivedStatus()
            return

        pray.move()
        predator.move()
        self.stage_status = ChaseStatus()

    def is_game_over(self) -> bool:
        return self.stage_status.is_terminal()

    def is_stage_over(self) -> bool:
        return self.stage_status.is_intermediate()

    def current_status(self, pray: Creature, predator: Creature) -> str:
        return self.stage_status.message(pray, predator)


@dataclass
class ClassicSporeStages(ClassicSporeGameStage):
    game_stages: List[ClassicSporeGameMultiStage] = field(
        default_factory=lambda: [ClassicSporeChaseStage(), ClassicSporeFightStage()]
    )
    current_stage: int = 0

    def action(self, pray: Creature, predator: Creature) -> None:
        if self.game_stages[self.current_stage].is_stage_over():
            self.current_stage = self.current_stage + 1
        self.game_stages[self.current_stage].action(pray, predator)

    def is_game_over(self) -> bool:
        return self.game_stages[self.current_stage].is_game_over()

    def current_status(self, pray: Creature, predator: Creature) -> str:
        return self.game_stages[self.current_stage].current_status(pray, predator)
