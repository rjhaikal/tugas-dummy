<template>
  <ion-page>
    <ion-header :translucent="true">
      <ion-toolbar>
        <ion-title>Data Cryptocurrency</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content :fullscreen="true" class="page-content">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Crypto</ion-title>
        </ion-toolbar>
      </ion-header>

      <section class="hero-card ion-padding">
        <p class="hero-label">API CoinLore</p>
        <h1 class="hero-title">Daftar Cryptocurrency Dunia</h1>
        <p class="hero-subtitle">Menampilkan field rank, name, symbol, dan price_usd.</p>
      </section>

      <div v-if="loading" class="status-text">Memuat data cryptocurrency...</div>
      <div v-else-if="errorMessage" class="status-text error-text">{{ errorMessage }}</div>

      <section v-else class="content-wrapper ion-padding">
        <ion-card class="summary-card">
          <ion-card-header>
            <ion-card-subtitle>Total Data Ditampilkan</ion-card-subtitle>
            <ion-card-title>{{ cryptoList.length }} Cryptocurrency</ion-card-title>
          </ion-card-header>
        </ion-card>

        <ion-card class="table-card">
          <ion-card-header>
            <ion-card-title>Daftar Harga Crypto</ion-card-title>
            <ion-card-subtitle>Sumber: https://api.coinlore.net/api/tickers/</ion-card-subtitle>
          </ion-card-header>

          <ion-card-content>
            <ion-grid class="crypto-table">
              <ion-row class="header-row">
                <ion-col size="2"><strong>Rank</strong></ion-col>
                <ion-col size="4"><strong>Name</strong></ion-col>
                <ion-col size="3"><strong>Symbol</strong></ion-col>
                <ion-col size="3" class="price-col"><strong>Price USD</strong></ion-col>
              </ion-row>

              <ion-row v-for="crypto in cryptoList" :key="crypto.id" class="data-row">
                <ion-col size="2">
                  <span class="rank-pill">#{{ crypto.rank }}</span>
                </ion-col>
                <ion-col size="4" class="name-col">{{ crypto.name }}</ion-col>
                <ion-col size="3" class="symbol-col">{{ crypto.symbol }}</ion-col>
                <ion-col size="3" class="price-col">${{ formatPrice(crypto.price_usd) }}</ion-col>
              </ion-row>
            </ion-grid>
          </ion-card-content>
        </ion-card>
      </section>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import {
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardSubtitle,
  IonCardTitle,
  IonCol,
  IonContent,
  IonGrid,
  IonHeader,
  IonPage,
  IonRow,
  IonTitle,
  IonToolbar,
} from '@ionic/vue';
import { onMounted, ref } from 'vue';
import { fetchCryptoCurrencies } from '../services/cryptoService';
import type { CryptoCurrency } from '../types/crypto';

const cryptoList = ref<CryptoCurrency[]>([]);
const loading = ref<boolean>(true);
const errorMessage = ref<string>('');

const formatPrice = (price: string): string => {
  const value = Number(price);

  if (Number.isNaN(value)) {
    return price;
  }

  return value.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
};

onMounted(async () => {
  try {
    cryptoList.value = await fetchCryptoCurrencies();
  } catch (error: unknown) {
    errorMessage.value =
      error instanceof Error ? error.message : 'Terjadi kesalahan saat memuat data.';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.page-content {
  --background: linear-gradient(180deg, #eef6ff 0%, #f8fbff 50%, #ffffff 100%);
}

.hero-card {
  margin: 12px;
  border-radius: 18px;
  color: #ffffff;
  background: linear-gradient(135deg, #0f172a 0%, #2563eb 58%, #22c55e 100%);
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.18);
}

.hero-label {
  margin: 0;
  font-size: 13px;
  letter-spacing: 0.04em;
  opacity: 0.9;
  text-transform: uppercase;
}

.hero-title {
  margin: 8px 0;
  font-size: 24px;
  font-weight: 800;
}

.hero-subtitle {
  margin: 0;
  font-size: 14px;
  opacity: 0.96;
}

.content-wrapper {
  padding-top: 0;
}

.status-text {
  margin-top: 28px;
  text-align: center;
  color: #475569;
}

.error-text {
  color: #dc2626;
}

.summary-card,
.table-card {
  --background: #ffffff;
  border: 1px solid #dbeafe;
  border-radius: 16px;
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.08);
}

.summary-card ion-card-subtitle,
.table-card ion-card-subtitle {
  color: #64748b;
}

.summary-card ion-card-title,
.table-card ion-card-title {
  color: #0f172a;
  font-weight: 800;
}

.crypto-table {
  border: 1px solid #dbeafe;
  border-radius: 12px;
  overflow: hidden;
  background-color: #ffffff;
}

.header-row {
  background: linear-gradient(90deg, #dbeafe 0%, #eff6ff 100%);
  color: #1e3a8a;
  padding: 10px 0;
  font-size: 13px;
}

.data-row {
  align-items: center;
  border-top: 1px solid #e2e8f0;
  padding: 11px 0;
  color: #1f2937;
  font-size: 14px;
}

.data-row:nth-child(even) {
  background-color: #f8fafc;
}

.rank-pill {
  display: inline-block;
  min-width: 44px;
  padding: 4px 8px;
  border-radius: 999px;
  text-align: center;
  color: #1e3a8a;
  background-color: #e0ecff;
  border: 1px solid #bfdbfe;
  font-weight: 800;
}

.name-col {
  font-weight: 700;
}

.symbol-col {
  color: #0f766e;
  font-weight: 800;
}

.price-col {
  text-align: right;
  font-weight: 700;
}
</style>
