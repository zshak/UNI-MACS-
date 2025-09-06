from dataclasses import field, dataclass
from typing import Optional

from app.SimulationStrategies import RandomWeatherProviderStrategy, RandomObserverProviderStrategy
from app.SimulationStrategies import WeatherProviderStrategy, ObserverProviderStrategy
from app.WeatherObservers import WeatherObserver
from app.WeatherStations import Station
from app.WeatherStations import WeatherLogger


@dataclass
class WeatherSimulator:
    # ესენი მანუალურად გასატესტადაა თუ მოგინდა
    # weather_provider_strategy: WeatherProviderStrategy = field(
    #     default_factory = ManualWeatherProviderStrategy)
    # observer_provider_strategy: ObserverProviderStrategy = field(
    #     default_factory = lambda: ManualObserverProviderStrategy(1)
    # )
    weather_provider_strategy: WeatherProviderStrategy = field(
        default_factory = lambda: RandomWeatherProviderStrategy(3,5))
    observer_provider_strategy: ObserverProviderStrategy = field(
        default_factory = lambda: RandomObserverProviderStrategy(1)
    )

    weather_station : Station = field(default_factory = WeatherLogger)
    NUM_SIMULATIONS = 10

    def simulate(self) -> None:
        for _ in range(self.NUM_SIMULATIONS):
            observer : Optional[WeatherObserver] = self.observer_provider_strategy.provide_observer()
            if observer is not None:
                self.weather_station.add_observer(observer)

            temperature = self.weather_provider_strategy.provide_temperature()
            wind_speed = self.weather_provider_strategy.provide_wind_speed()
            humidity = self.weather_provider_strategy.provide_humidity()

            self.weather_station.set_weather_data(temperature, wind_speed, humidity)