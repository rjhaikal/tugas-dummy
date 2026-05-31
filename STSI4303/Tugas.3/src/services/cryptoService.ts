import type { CoinLoreTickersResponse, CryptoCurrency } from '../types/crypto';

const COINLORE_API_URL = 'https://api.coinlore.net/api/tickers/';

export async function fetchCryptoCurrencies(): Promise<CryptoCurrency[]> {
  const response = await fetch(COINLORE_API_URL);

  if (!response.ok) {
    throw new Error('Gagal mengambil data cryptocurrency dari server.');
  }

  const result = (await response.json()) as CoinLoreTickersResponse;
  return result.data ?? [];
}
