import random
from dataclasses import dataclass, field
from typing import Protocol, Optional

from app.WeatherObservers import WeatherObserver, TemperatureAlert, WindSpeedAlert, HumidityAlert


class WeatherProviderStrategy(Protocol):
    def provide_temperature(self) -> float:
        pass

    def provide_wind_speed(self) -> float:
        pass

    def provide_humidity(self) -> float:
        pass

class ObserverProviderStrategy(Protocol):
    def provide_observer(self) -> Optional[WeatherObserver]:
        pass

class ThresholdProviderStrategy(Protocol):
    def provide_threshold(self) -> float:
        pass

class RandomWeatherProviderStrategy(WeatherProviderStrategy):
    ROUND_TO = 4

    def __init__(self, low: float, high: float) -> None:
        self.low = low
        self.high = high

    def provide_temperature(self) -> float:
        return random.uniform(self.low, self.high).__round__(self.ROUND_TO)

    def provide_wind_speed(self) -> float:
        return random.uniform(self.low, self.high).__round__(self.ROUND_TO)

    def provide_humidity(self) -> float:
        return random.uniform(self.low, self.high).__round__(self.ROUND_TO)

class ManualWeatherProviderStrategy(WeatherProviderStrategy):
    def provide_temperature(self) -> float:
        temperature = input("Enter temperature: ")
        return float(temperature)

    def provide_wind_speed(self) -> float:
        wind_speed = input("Enter wind speed: ")
        return float(wind_speed)

    def provide_humidity(self) -> float:
        humidity = input("Enter humidity: ")
        return float(humidity)

@dataclass
class RandomThresholdProviderStrategy(ThresholdProviderStrategy):
    ROUND_TO = 4
    LOW = 1
    HIGH = 2

    def provide_threshold(self) -> float:
        return random.uniform(self.LOW, self.HIGH).__round__(self.ROUND_TO)

class ManualThresholdProviderStrategy(ThresholdProviderStrategy):
    def provide_threshold(self) -> float:
        threshold = input("Enter the threshold: ")
        return float(threshold)

@dataclass
class RandomObserverProviderStrategy(ObserverProviderStrategy):
    num_updates_to_observe_for_alert: int
    threshold_strategy: ThresholdProviderStrategy = field(default_factory = RandomThresholdProviderStrategy)

    def provide_observer(self) -> Optional[WeatherObserver]:
        observer_index = random.randrange(1, 4)
        if observer_index == 1:
            return TemperatureAlert(self.threshold_strategy.provide_threshold())
        elif observer_index == 2:
            return WindSpeedAlert(self.num_updates_to_observe_for_alert)
        elif observer_index == 3:
            return HumidityAlert(self.threshold_strategy.provide_threshold())
        else:
            return None

@dataclass
class ManualObserverProviderStrategy(ObserverProviderStrategy):
    num_updates_to_observe_for_alert: int
    threshold_strategy: ThresholdProviderStrategy = field(default_factory = RandomThresholdProviderStrategy)

    def provide_observer(self) -> Optional[WeatherObserver]:
        print("1 - TemperatureAlert, 2 - WindSpeedAlert, 3 - HumidityAlert, else - None")
        observer_index = input("Enter the index of the weather observer: ")
        if observer_index == "1":
            return TemperatureAlert(self.threshold_strategy.provide_threshold())
        elif observer_index == "2":
            return WindSpeedAlert(self.num_updates_to_observe_for_alert)
        elif observer_index == "3":
            return HumidityAlert(self.threshold_strategy.provide_threshold())
        else:
            return None