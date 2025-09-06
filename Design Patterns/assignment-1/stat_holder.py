from dataclasses import dataclass


@dataclass
class ClassicSporeStatHolder:
    legs: int = 0
    wings: int = 0
    claws: int = 0
    teeth: int = 0
    stamina: int = 0
    health: int = 0
    location: int = 0
    attack_power: int = 0

    def to_str(self) -> str:
        return (
            f"\n location: {self.location},"
            f"\n legs: {self.legs}, "
            f"\n wings: {self.wings}, "
            f"\n claws: {self.claws}, "
            f"\n teeth: {self.teeth}, "
            f"\n stamina: {self.stamina}, "
            f"\n health: {self.health}, "
            f"\n attack_power: {self.attack_power} "
        )


class ClassicSporeStatConstraints:
    LEGS_CONSTRAINT = [0, 1, 2]
    WINGS_CONSTRAINT = [0, 1, 2]
    CLAWS_CONSTRAINT = [1, 2, 3, 4]
    TEETH_CONSTRAINT = [0, 3, 6, 9]
    STAMINA_CONSTRAINT = (0, 10000)
    HEALTH_CONSTRAINT = (0, 300)
    ATTACK_POWER_CONSTRAINT = (1, 5)
