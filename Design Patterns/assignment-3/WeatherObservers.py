from typing import Protocol, List, Optional


class WeatherObserver(Protocol):
    def update(self, temperature: float, humidity: float, wind_speed: float) -> None:
        pass

class WeatherDisplay(WeatherObserver):
    def update(self, temperature: float, humidity: float, wind_speed: float) -> None:
        print(f"WeatherDisplay: Showing Temperature = {temperature}°C, Humidity = {humidity}%, Wind Speed = {wind_speed} km/h")

class TemperatureAlert(WeatherObserver):
    def __init__(self, threshold: float) -> None:
        self.threshold = threshold

    def update(self, temperature: float, humidity: float, wind_speed: float) -> None:
        if temperature > self.threshold:
            print(f"TemperatureAlert: **Alert! Temperature exceeded {self.threshold}°C: {temperature}°C**")

class WindSpeedAlert(WeatherObserver):
    def __init__(self, num_updates_to_observe_for_alert: int) -> None:
        self.num_updates_to_observe_for_alert : int = num_updates_to_observe_for_alert
        self.wind_speed_history : List[Optional[float]] = [None] * self.num_updates_to_observe_for_alert

    def is_increasing_wind_speed(self) -> bool:
        prev_wind_speed: Optional[float] = None

        for wind_speed in self.wind_speed_history:
            if wind_speed is None:
                return False

            if prev_wind_speed is not None and wind_speed <= prev_wind_speed:
                return False
            prev_wind_speed = wind_speed

        return True

    def push_back_wind_speed(self, wind_speed: float) -> None:
        if None in self.wind_speed_history:
            index: int = self.wind_speed_history.index(None)
            self.wind_speed_history[index] = wind_speed
        else:
            self.wind_speed_history = self.wind_speed_history[1:] + [wind_speed]

    def update(self, temperature: float, humidity: float, wind_speed: float):
        self.push_back_wind_speed(wind_speed)
        if self.is_increasing_wind_speed():
            print(f"WindSpeedAlert: **Alert! Wind speed is increasing: {self.wind_speed_history} km/h → {wind_speed} km/h**")

class HumidityAlert(WeatherObserver):
    def __init__(self, threshold : float) -> None:
        self.threshold = threshold

    def update(self, temperature: float, humidity: float, wind_speed: float):
        if humidity > self.threshold:
            print(f"HumidityAlert: **Alert! Humidity exceeded {self.threshold}%: {humidity}%**")