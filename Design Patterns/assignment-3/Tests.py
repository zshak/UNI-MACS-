import pytest
from unittest.mock import patch, MagicMock

from app.WeatherObservers import WeatherObserver, TemperatureAlert, WindSpeedAlert, HumidityAlert, WeatherDisplay
from app.SimulationStrategies import RandomWeatherProviderStrategy, ManualWeatherProviderStrategy, RandomObserverProviderStrategy, ManualObserverProviderStrategy, RandomThresholdProviderStrategy, ManualThresholdProviderStrategy
from app.WeatherStations import WeatherStation, WeatherLogger
from app.WeatherSimulator import WeatherSimulator
from app.Logging import ConsoleLogger


@pytest.fixture
def mock_input():
    with patch('builtins.input', side_effect=['25', '15', '60', '1', '2']):
        yield

@pytest.fixture
def mock_input_single():
    with patch('builtins.input', return_value='1') as mock:
        yield mock

def test_console_logger_log():
    with patch('builtins.print') as mock_print:
        logger = ConsoleLogger()
        logger.log("Test message")
        mock_print.assert_called_once_with("Test message")

def test_random_weather_provider_strategy():
    strategy = RandomWeatherProviderStrategy(10, 30)
    temperature = strategy.provide_temperature()
    wind_speed = strategy.provide_wind_speed()
    humidity = strategy.provide_humidity()
    assert 10 <= temperature <= 30
    assert 10 <= wind_speed <= 30
    assert 10 <= humidity <= 30

def test_manual_weather_provider_strategy(mock_input):
    strategy = ManualWeatherProviderStrategy()
    temperature = strategy.provide_temperature()
    wind_speed = strategy.provide_wind_speed()
    humidity = strategy.provide_humidity()
    assert temperature == 25.0
    assert wind_speed == 15.0
    assert humidity == 60.0

def test_random_threshold_provider_strategy():
    strategy = RandomThresholdProviderStrategy()
    threshold = strategy.provide_threshold()
    assert 1 <= threshold <= 2

def test_manual_threshold_provider_strategy(mock_input_single):
    strategy = ManualThresholdProviderStrategy()
    threshold = strategy.provide_threshold()
    assert threshold == 1.0
    mock_input_single.assert_called_once()

def test_random_observer_provider_strategy():
    strategy = RandomObserverProviderStrategy(1)
    observer = strategy.provide_observer()
    assert isinstance(observer, (TemperatureAlert, WindSpeedAlert, HumidityAlert, type(None)))

def test_manual_observer_provider_strategy(mock_input):
    strategy = ManualObserverProviderStrategy(1)
    observer = strategy.provide_observer()
    assert isinstance(observer, (TemperatureAlert, WindSpeedAlert, HumidityAlert, type(None)))

def test_temperature_alert():
    alert = TemperatureAlert(25)
    with patch('builtins.print') as mock_print:
        alert.update(26, 50, 10)
        mock_print.assert_called_once()
        alert.update(20, 50, 10)
        assert mock_print.call_count == 1

def test_wind_speed_alert():
    alert = WindSpeedAlert(3)
    with patch('builtins.print') as mock_print:
        alert.update(20, 50, 10)
        alert.update(20, 50, 12)
        alert.update(20, 50, 15)
        mock_print.assert_called_once()
        alert.update(20, 50, 10)
        assert mock_print.call_count == 1

def test_humidity_alert():
    alert = HumidityAlert(60)
    with patch('builtins.print') as mock_print:
        alert.update(25, 65, 10)
        mock_print.assert_called_once()
        alert.update(25, 55, 10)
        assert mock_print.call_count == 1

def test_weather_display():
    display = WeatherDisplay()
    with patch('builtins.print') as mock_print:
        display.update(25, 60, 10)
        mock_print.assert_called_once()

def test_weather_station():
    station = WeatherStation()
    observer = MagicMock(spec=WeatherObserver)
    station.add_observer(observer)
    station.set_weather_data(25, 10, 60)
    observer.update.assert_called_once_with(25, 60, 10)

    station.remove_observer(observer)
    station.set_weather_data(30, 70, 15)
    assert observer.update.call_count == 1

def test_weather_logger():
    logger = WeatherLogger()
    observer = MagicMock(spec=WeatherObserver)
    with patch('app.Logging.ConsoleLogger.log') as mock_log:
        logger.add_observer(observer)
        logger.set_weather_data(25, 60, 10)
        mock_log.assert_called()
        logger.remove_observer(observer)
        mock_log.assert_called()

def test_weather_simulator():
    simulator = WeatherSimulator()
    with patch('app.WeatherSimulator.RandomWeatherProviderStrategy.provide_temperature') as mock_temp, \
            patch('app.WeatherSimulator.RandomWeatherProviderStrategy.provide_wind_speed') as mock_wind, \
            patch('app.WeatherSimulator.RandomWeatherProviderStrategy.provide_humidity') as mock_humidity, \
            patch('app.WeatherSimulator.RandomObserverProviderStrategy.provide_observer') as mock_observer, \
            patch('app.WeatherStations.WeatherLogger.set_weather_data') as mock_set_data:
        mock_temp.return_value = 25
        mock_wind.return_value = 15
        mock_humidity.return_value = 60
        mock_observer.return_value = WeatherDisplay()

        simulator.simulate()

        assert mock_temp.call_count == simulator.NUM_SIMULATIONS
        assert mock_wind.call_count == simulator.NUM_SIMULATIONS
        assert mock_humidity.call_count == simulator.NUM_SIMULATIONS
        assert mock_set_data.call_count == simulator.NUM_SIMULATIONS

def test_weather_logger_add_observer():
    mock_logger = MagicMock(spec=ConsoleLogger)
    wrapped_station = WeatherStation()
    logger = WeatherLogger(station=wrapped_station, logger=mock_logger)

    observer = MagicMock(spec=WeatherObserver)
    logger.add_observer(observer)

    mock_logger.log.assert_called_once_with(f"Adding Alert: {observer.__class__.__name__}")
    assert observer in wrapped_station.observers

def test_weather_logger_remove_observer():
    mock_logger = MagicMock(spec=ConsoleLogger)
    wrapped_station = WeatherStation()
    logger = WeatherLogger(station=wrapped_station, logger=mock_logger)

    observer = MagicMock(spec=WeatherObserver)
    logger.add_observer(observer)
    logger.remove_observer(observer)

    mock_logger.log.assert_called_with(f"Removing Alert: {observer.__class__.__name__}")
    assert observer not in wrapped_station.observers


def test_weather_logger_set_weather_data():
    mock_logger = MagicMock(spec=ConsoleLogger)
    wrapped_station = WeatherStation()
    logger = WeatherLogger(station=wrapped_station, logger=mock_logger)

    logger.set_weather_data(25, 10, 60)
    mock_logger.log.assert_any_call("Week 1")
    mock_logger.log.assert_any_call("Setting weather data: temperature: 25, wind speed: 10, humidity: 60")

    logger.set_weather_data(30, 70, 15)
    mock_logger.log.assert_any_call("-------------\n")
    mock_logger.log.assert_any_call("Week 2")
    mock_logger.log.assert_any_call("Setting weather data: temperature: 30, wind speed: 70, humidity: 15")
    assert wrapped_station.temperature == 30
    assert wrapped_station.humidity == 15
    assert wrapped_station.wind_speed == 70