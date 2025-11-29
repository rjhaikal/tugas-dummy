const { createApp } = Vue;

createApp({
    data() {
        return {
            ekspedisiList: [
                { kode: "REG", nama: "Reguler (3-5 hari)" },
                { kode: "EXP", nama: "Ekspres (1-2 hari)" }
            ],
            paketList: [
                { kode: "PAKET-UT-001", nama: "PAKET IPS Dasar", isi: ["EKMA4116","EKMA4115"], harga: 120000 },
                { kode: "PAKET-UT-002", nama: "PAKET IPA Dasar", isi: ["BIOL4201","FISIP4001"], harga: 140000 }
            ],
            trackingList: [
                {
                    nomorDO: "DO2025-0001",
                    nim: "123456789",
                    nama: "Rina Wulandari",
                    status: "Dalam Perjalanan",
                    ekspedisi: "JNE",
                    tanggalKirim: "2025-08-25",
                    paketDipilih: "PAKET-UT-001",
                    totalHarga: 120000,
                    riwayatPerjalanan: [
                        { waktu: "2025-08-25 10:12:20", keterangan: "Penerimaan di Loket: TANGSEL" },
                        { waktu: "2025-08-25 14:07:56", keterangan: "Tiba di Hub: JAKSEL" },
                        { waktu: "2025-08-26 08:44:01", keterangan: "Diteruskan ke Kantor Tujuan" }
                    ]
                }
            ],
            formDO: {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketDipilih: '',
                tanggalKirim: this.getTodayDate()
            },
            searchDO: '',
            trackingResult: null,
            searchAttempted: false,
            selectedPaket: null,
            errors: {},
            sequenceNumber: 1
        }
    },
    
    computed: {
        nomorDOGenerated() {
            const year = new Date().getFullYear();
            const seq = String(this.sequenceNumber).padStart(3, '0');
            return `DO${year}-${seq}`;
        },
        
        totalHargaPaket() {
            if (this.selectedPaket) {
                return this.selectedPaket.harga;
            }
            return 0;
        }
    },
    
    watch: {
        'formDO.nim'(newVal) {
            if (newVal && !/^\d{0,9}$/.test(newVal)) {
                this.formDO.nim = newVal.slice(0, -1);
            }
        },
        
        searchDO(newVal) {
            console.log(`Search DO: ${newVal}`);
            if (!newVal) {
                this.trackingResult = null;
                this.searchAttempted = false;
            }
        },
        
        'formDO.paketDipilih'(newVal) {
            if (newVal) {
                console.log(`Paket dipilih: ${newVal}`);
            }
        }
    },
    
    methods: {
        getTodayDate() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        },
        
        formatRupiah(angka) {
            return new Intl.NumberFormat('id-ID', {
                style: 'currency',
                currency: 'IDR',
                minimumFractionDigits: 0
            }).format(angka);
        },
        
        formatTanggal(dateString) {
            const options = { 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
            };
            return new Date(dateString).toLocaleDateString('id-ID', options);
        },
        
        onPaketChange() {
            if (this.formDO.paketDipilih) {
                this.selectedPaket = this.paketList.find(
                    p => p.kode === this.formDO.paketDipilih
                );
            } else {
                this.selectedPaket = null;
            }
        },
        
        onSearchInput() {
            console.log('User sedang mengetik:', this.searchDO);
        },
        
        validateFormDO() {
            this.errors = {};
            let isValid = true;
            
            if (!/^\d{9}$/.test(this.formDO.nim)) {
                this.errors.nim = 'NIM harus 9 digit angka';
                isValid = false;
            }
            
            if (this.formDO.nama.trim().length < 3) {
                this.errors.nama = 'Nama minimal 3 karakter';
                isValid = false;
            }
            
            if (!this.formDO.ekspedisi) {
                this.errors.ekspedisi = 'Pilih ekspedisi';
                isValid = false;
            }
            
            if (!this.formDO.paketDipilih) {
                this.errors.paket = 'Pilih paket bahan ajar';
                isValid = false;
            }
            
            return isValid;
        },
        
        tambahDO() {
            if (!this.validateFormDO()) {
                alert('Mohon lengkapi form dengan benar!');
                return;
            }
            
            const newDO = {
                nomorDO: this.nomorDOGenerated,
                nim: this.formDO.nim,
                nama: this.formDO.nama,
                ekspedisi: this.formDO.ekspedisi,
                paketDipilih: this.formDO.paketDipilih,
                tanggalKirim: this.formDO.tanggalKirim,
                totalHarga: this.totalHargaPaket,
                status: 'Diproses',
                riwayatPerjalanan: [
                    {
                        waktu: new Date().toLocaleString('id-ID'),
                        keterangan: `Paket diterima untuk diproses - ${this.selectedPaket.namaPaket}`
                    }
                ]
            };
            
            this.trackingList.push(newDO);
            
            this.sequenceNumber++;
            
            this.resetFormDO();
            
            alert(`Delivery Order ${newDO.nomorDO} berhasil dibuat!\n\nNama: ${newDO.nama}\nPaket: ${this.selectedPaket.namaPaket}\nTotal: ${this.formatRupiah(newDO.totalHarga)}`);
            
            window.scrollTo({
                top: document.body.scrollHeight,
                behavior: 'smooth'
            });
        },
        
        resetFormDO() {
            this.formDO = {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketDipilih: '',
                tanggalKirim: this.getTodayDate()
            };
            this.selectedPaket = null;
            this.errors = {};
        },
        
        cariTracking() {
            this.searchAttempted = true;
            
            if (!this.searchDO.trim()) {
                alert('Masukkan nomor DO!');
                return;
            }
            
            const found = this.trackingList.find(
                item => item.nomorDO.toLowerCase() === this.searchDO.trim().toLowerCase()
            );
            
            if (found) {
                this.trackingResult = found;
                console.log('Tracking ditemukan:', found);
            } else {
                this.trackingResult = null;
                console.log('Tracking tidak ditemukan');
            }
        },
        
        lihatDetail(nomorDO) {
            this.searchDO = nomorDO;
            this.cariTracking();
            
            setTimeout(() => {
                const trackingSection = document.querySelector('.tracking-result');
                if (trackingSection) {
                    trackingSection.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }, 100);
        },
        
        getStatusClass(status) {
            const statusMap = {
                'Diproses': 'status-diproses',
                'Dalam Perjalanan': 'status-perjalanan',
                'Terkirim': 'status-terkirim'
            };
            return statusMap[status] || '';
        }
    },
    
    mounted() {
        console.log('Vue App untuk Tracking DO telah dimount');
        console.log(`Total ${this.trackingList.length} DO dalam sistem`);
        console.log(`Total ${this.paketList.length} paket tersedia`);
        
        this.formDO.tanggalKirim = this.getTodayDate();
    }
}).mount('#app');
