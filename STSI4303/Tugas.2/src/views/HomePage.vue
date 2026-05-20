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
import { computed, onMounted, ref } from 'vue';
import { fetchJakartaWeather } from '../services/weatherService';
import type { WeatherHourly } from '../types/weather';

const weatherList = ref<WeatherHourly[]>([]);
const loading = ref<boolean>(true);
const errorMessage = ref<string>('');

const minTemperature = computed<number>(() => {
  if (weatherList.value.length === 0) {
    return 0;
  }

  return Math.min(...weatherList.value.map((item) => item.temperature_2m));
});

const maxTemperature = computed<number>(() => {
  if (weatherList.value.length === 0) {
    return 0;
  }

  return Math.max(...weatherList.value.map((item) => item.temperature_2m));
});

const averageTemperature = computed<number>(() => {
  if (weatherList.value.length === 0) {
    return 0;
  }

  const total = weatherList.value.reduce((sum, item) => sum + item.temperature_2m, 0);
  return total / weatherList.value.length;
});

const formatTime = (value: string): string => {
  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return date.toLocaleString('id-ID', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

onMounted(async () => {
  try {
    weatherList.value = await fetchJakartaWeather();
  } catch (error: unknown) {
    errorMessage.value =
      error instanceof Error ? error.message : 'Terjadi kesalahan saat memuat data.';
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <ion-page>
    <ion-header :translucent="true">
      <ion-toolbar>
        <ion-title>Cuaca Jakarta</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content :fullscreen="true" class="page-content">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Cuaca Jakarta</ion-title>
        </ion-toolbar>
      </ion-header>

      <div class="hero-card ion-padding">
        <p class="hero-label">Prakiraan dari Open-Meteo</p>
        <h2 class="hero-title">Monitoring Suhu per Jam</h2>
        <p class="hero-subtitle">Data yang ditampilkan adalah 10 data pertama sesuai ketentuan tugas.</p>
      </div>

      <div v-if="loading" class="status-text">Memuat data cuaca...</div>
      <div v-else-if="errorMessage" class="status-text error-text">{{ errorMessage }}</div>

      <div v-else class="content-wrapper ion-padding">
        <ion-grid class="summary-grid" fixed>
          <ion-row>
            <ion-col size="4">
              <ion-card class="summary-card warm">
                <ion-card-header>
                  <ion-card-subtitle>Minimum</ion-card-subtitle>
                  <ion-card-title>{{ minTemperature.toFixed(1) }}°C</ion-card-title>
                </ion-card-header>
              </ion-card>
            </ion-col>
            <ion-col size="4">
              <ion-card class="summary-card normal">
                <ion-card-header>
                  <ion-card-subtitle>Rata-rata</ion-card-subtitle>
                  <ion-card-title>{{ averageTemperature.toFixed(1) }}°C</ion-card-title>
                </ion-card-header>
              </ion-card>
            </ion-col>
            <ion-col size="4">
              <ion-card class="summary-card hot">
                <ion-card-header>
                  <ion-card-subtitle>Maksimum</ion-card-subtitle>
                  <ion-card-title>{{ maxTemperature.toFixed(1) }}°C</ion-card-title>
                </ion-card-header>
              </ion-card>
            </ion-col>
          </ion-row>
        </ion-grid>

        <ion-card class="table-card">
          <ion-card-header>
            <ion-card-title>Daftar Suhu per Jam</ion-card-title>
            <ion-card-subtitle>Kolom: time dan temperature_2m</ion-card-subtitle>
          </ion-card-header>
          <ion-card-content>
            <ion-grid class="weather-table">
              <ion-row class="header-row">
                <ion-col size="8"><strong>Waktu</strong></ion-col>
                <ion-col size="4" class="temperature-col"><strong>Suhu (°C)</strong></ion-col>
              </ion-row>

              <ion-row v-for="item in weatherList" :key="item.time" class="data-row">
                <ion-col size="8">{{ formatTime(item.time) }}</ion-col>
                <ion-col size="4" class="temperature-col">
                  <span class="temperature-pill">{{ item.temperature_2m.toFixed(1) }}</span>
                </ion-col>
              </ion-row>
            </ion-grid>
          </ion-card-content>
        </ion-card>
      </div>
    </ion-content>
  </ion-page>
</template>

<style scoped>
.page-content {
  --background: linear-gradient(180deg, #f3f7ff 0%, #f8fbff 45%, #ffffff 100%);
}

.hero-card {
  margin: 12px;
  border-radius: 16px;
  color: #ffffff;
  background: linear-gradient(135deg, #2563eb 0%, #0ea5e9 100%);
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.2);
}

.hero-label {
  margin: 0;
  font-size: 13px;
  opacity: 0.92;
}

.hero-title {
  margin: 6px 0;
  font-size: 22px;
  font-weight: 700;
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
  margin-top: 24px;
  text-align: center;
  color: #475569;
}

.error-text {
  color: #dc2626;
}

.summary-grid {
  padding: 0 6px;
}

.summary-card {
  margin: 8px 4px;
  border-radius: 14px;
  color: #ffffff;
  box-shadow: 0 8px 16px rgba(15, 23, 42, 0.12);
}

.summary-card ion-card-subtitle,
.summary-card ion-card-title {
  color: #ffffff;
}

.summary-card.warm {
  background: linear-gradient(135deg, #4f46e5 0%, #6366f1 100%);
}

.summary-card.normal {
  background: linear-gradient(135deg, #0f766e 0%, #14b8a6 100%);
}

.summary-card.hot {
  background: linear-gradient(135deg, #c2410c 0%, #f97316 100%);
}

.table-card {
  --background: #ffffff;
  margin-top: 10px;
  border-radius: 14px;
  border: 1px solid #dbe7ff;
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.08);
}

.table-card ion-card-title {
  color: #0f172a;
  font-weight: 700;
}

.table-card ion-card-subtitle {
  color: #475569;
}

.weather-table {
  border: 1px solid #dbe7ff;
  border-radius: 10px;
  overflow: hidden;
  background-color: #ffffff;
}

.header-row {
  background: linear-gradient(90deg, #dbeafe 0%, #eff6ff 100%);
  color: #1e3a8a;
  padding: 10px 0;
  font-size: 14px;
}

.data-row {
  border-top: 1px solid #e2e8f0;
  padding: 10px 0;
  color: #1f2937;
  font-size: 14px;
}

.data-row:nth-child(even) {
  background-color: #f8fafc;
}

.temperature-col {
  text-align: right;
}

.temperature-pill {
  display: inline-block;
  min-width: 58px;
  text-align: center;
  padding: 4px 10px;
  border-radius: 999px;
  color: #1e3a8a;
  background-color: #e0ecff;
  border: 1px solid #bfdbfe;
  font-weight: 700;
}
</style>
