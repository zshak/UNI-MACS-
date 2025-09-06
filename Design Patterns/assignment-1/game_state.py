from dataclasses import dataclass, field
from logging import ConsoleLogger, Logger
from typing import Protocol

from creature import ClassicSporeCreature, Creature
from game_stage import ClassicSporeGameStage, ClassicSporeStages


class GameState(Protocol):
    def generate_world(self) -> None:
        pass

    def tick(self) -> None:
        pass

    def is_over(self) -> bool:
        pass

    def state_status(self) -> str:
        pass


class ClassicSporeGameSpawnRanges:
    PREDATOR_SPAWN_RANGE = (0, 1)
    PRAY_SPAWN_RANGE = (0, 1000)


@dataclass
class SporeGameState(GameState):
    pray: Creature = field(default_factory=ClassicSporeCreature)
    predator: Creature = field(default_factory=ClassicSporeCreature)
    stage: ClassicSporeGameStage = field(default_factory=ClassicSporeStages)

    def generate_world(self) -> None:
        self.pray.spawn(ClassicSporeGameSpawnRanges.PRAY_SPAWN_RANGE)
        self.predator.spawn(ClassicSporeGameSpawnRanges.PREDATOR_SPAWN_RANGE)

    def tick(self) -> None:
        self.stage.action(pray=self.pray, predator=self.predator)

    def is_over(self) -> bool:
        return self.stage.is_game_over()

    def state_status(self) -> str:
        return self.stage.current_status(pray=self.pray, predator=self.predator)


@dataclass
class GameStateLogger(GameState):
    state: GameState = field(default_factory=SporeGameState)
    logger: Logger = field(default_factory=ConsoleLogger)

    def generate_world(self) -> None:
        self.state.generate_world()
        self.logger.log(self.state.state_status())

    def tick(self) -> None:
        self.state.tick()

    def is_over(self) -> bool:
        res = self.state.is_over()
        if res:
            self.logger.log(self.state.state_status())
            self.logger.log("\n")
        return res

    def state_status(self) -> str:
        return self.state.state_status()
