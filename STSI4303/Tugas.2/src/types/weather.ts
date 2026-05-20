export interface WeatherHourly {
  time: string;
  temperature_2m: number;
}

export interface OpenMeteoResponse {
  hourly?: {
    time?: string[];
    temperature_2m?: number[];
  };
}
