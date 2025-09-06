from dataclasses import dataclass, field

from game_state import GameState, GameStateLogger


@dataclass
class Game:
    state: GameState = field(default_factory=GameStateLogger)

    def play(self) -> None:
        self.state.generate_world()
        while not self.state.is_over():
            self.state.tick()
