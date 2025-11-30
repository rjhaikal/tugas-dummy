Vue.component('order-form', {
    template: '#tpl-order',
    props: ['paket', 'ekspedisi'],
    data() {
        return {
            newDO: {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketKode: '',
                tanggalKirim: ''
            },
            selectedPaketDetail: null
        };
    },
    watch: {
        'newDO.paketKode': function(val) {
            this.selectedPaketDetail = this.paket.find(p => p.kode === val);
        }
    },
    methods: {
        formatCurrency(value) {
            return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(value);
        },
        generateNoDO() {
            const year = new Date().getFullYear();
            // We need access to existing DOs to generate sequence. 
            // Since we don't have them passed as props here, we might need to ask ApiService
            // Or generate a random one/timestamp based for this dummy app if we can't access the list.
            // But ApiService is global.
            const trackingData = ApiService.data.tracking || [];
            const count = trackingData.filter(d => d.noDO.startsWith(`DO${year}`)).length + 1;
            const sequence = String(count).padStart(3, '0');
            return `DO${year}-${sequence}`;
        },
        submitOrder() {
            if (!this.newDO.nim || !this.newDO.nama || !this.newDO.ekspedisi || !this.newDO.paketKode) {
                alert('Mohon lengkapi data pemesanan');
                return;
            }

            const noDO = this.generateNoDO();
            const doItem = {
                noDO: noDO,
                nim: this.newDO.nim,
                nama: this.newDO.nama,
                ekspedisi: this.newDO.ekspedisi,
                paket: this.selectedPaketDetail,
                tanggalKirim: this.newDO.tanggalKirim || new Date().toISOString().split('T')[0],
                history: [
                    {
                        waktu: new Date().toLocaleString('id-ID'),
                        keterangan: 'Pesanan dibuat'
                    }
                ]
            };

            // Emit event to parent to handle addition, or call ApiService directly
            // The prompt says "@created='handleNewDO'", so parent handles it.
            this.$emit('created', doItem);
            
            // Reset form
            this.newDO = {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketKode: '',
                tanggalKirim: ''
            };
            this.selectedPaketDetail = null;
        }
    }
});