export interface CryptoCurrency {
  id: string;
  rank: string;
  name: string;
  symbol: string;
  price_usd: string;
}

export interface CoinLoreTickersResponse {
  data?: CryptoCurrency[];
}
