from dataclasses import dataclass, field
from typing import List, Protocol

from app.Logging import ConsoleLogger, Logger
from app.WeatherObservers import WeatherObserver

class Station(Protocol):
    def add_observer(self, observer: WeatherObserver) -> None:
        pass

    def remove_observer(self, observer: WeatherObserver) -> None:
        pass

    def set_weather_data(self, temperature: float, wind_speed: float, humidity: float) -> None:
        pass

class WeatherStation(Station):
    def __init__(self) -> None:
        self.observers: List[WeatherObserver] = []
        self.temperature = 0.0
        self.humidity = 0.0
        self.wind_speed = 0.0

    def add_observer(self, observer: WeatherObserver) -> None:
        self.observers.append(observer)

    def remove_observer(self, observer: WeatherObserver) -> None:
        self.observers.remove(observer)

    def set_weather_data(self, temperature: float, wind_speed: float, humidity: float) -> None:
        self.temperature = temperature
        self.humidity = humidity
        self.wind_speed = wind_speed
        self.notify_observers()

    def notify_observers(self) -> None:
        for observer in self.observers:
            observer.update(self.temperature, self.humidity, self.wind_speed)

@dataclass
class WeatherLogger(Station):
    station : WeatherStation = field(default_factory = WeatherStation)
    logger: Logger = field(default_factory=ConsoleLogger)
    week = 0

    def add_observer(self, observer: WeatherObserver) -> None:
        self.logger.log(f"Adding Alert: {observer.__class__.__name__}")
        self.station.add_observer(observer)

    def remove_observer(self, observer: WeatherObserver) -> None:
        self.logger.log(f"Removing Alert: {observer.__class__.__name__}")
        self.station.remove_observer(observer)

    def set_weather_data(self, temperature: float, wind_speed: float, humidity: float) -> None:
        self.week += 1

        if self.week != 1:
            self.logger.log("-------------\n")
        self.logger.log(f"Week {self.week}")
        self.logger.log(f"Setting weather data: temperature: {temperature}, wind speed: {wind_speed}, humidity: {humidity}")

        self.station.set_weather_data(temperature, wind_speed, humidity)
