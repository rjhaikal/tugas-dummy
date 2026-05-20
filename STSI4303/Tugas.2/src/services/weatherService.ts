import type { OpenMeteoResponse, WeatherHourly } from '../types/weather';

const WEATHER_API_URL =
  'https://api.open-meteo.com/v1/forecast?latitude=-6.2&longitude=106.8&hourly=temperature_2m&timezone=Asia%2FJakarta';

export async function fetchJakartaWeather(): Promise<WeatherHourly[]> {
  const response = await fetch(WEATHER_API_URL);

  if (!response.ok) {
    throw new Error('Gagal mengambil data cuaca dari server.');
  }

  const data = (await response.json()) as OpenMeteoResponse;
  const times = data.hourly?.time ?? [];
  const temperatures = data.hourly?.temperature_2m ?? [];

  const total = Math.min(times.length, temperatures.length);
  const combined: WeatherHourly[] = [];

  for (let i = 0; i < total; i += 1) {
    combined.push({
      time: times[i],
      temperature_2m: temperatures[i],
    });
  }

  return combined.slice(0, 10);
}
