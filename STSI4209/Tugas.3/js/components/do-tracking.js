Vue.component('do-tracking', {
    template: '#tpl-tracking',
    props: ['data'], // The tracking data list
    data() {
        return {
            searchQuery: '',
            searchResult: null,
            showAddForm: false,
            newDO: {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketKode: '',
                tanggalKirim: ''
            },
            pengirimanList: [],
            paketList: [],
            selectedPaketDetail: null,
            
            // For adding status
            newStatus: {
                keterangan: ''
            }
        };
    },
    async created() {
        const data = await ApiService.init();
        if (data) {
            this.pengirimanList = data.pengirimanList;
            this.paketList = data.paket;
        }
    },
    watch: {
        'newDO.paketKode': function(val) {
            this.selectedPaketDetail = this.paketList.find(p => p.kode === val);
        }
    },
    computed: {
        trackingData() {
            let rawData = [];
            // Prioritize prop data, but fallback to ApiService if prop is empty
            if (this.data && this.data.length > 0) {
                rawData = this.data;
            } else {
                rawData = ApiService.data.tracking || [];
            }

            // Normalize data to ensure compatibility with both old (nested) and new (flat) formats
            return rawData.map(item => {
                // If item already has noDO, it's likely in the new format
                if (item.noDO) return item;
                
                // Check for old nested format: { "DO2025-001": { ... } }
                const keys = Object.keys(item);
                if (keys.length === 1 && keys[0].startsWith('DO')) {
                    const noDO = keys[0];
                    return {
                        noDO: noDO,
                        ...item[noDO]
                    };
                }
                return item;
            });
        }
    },
    methods: {
        performSearch() {
            console.log('Performing search for:', this.searchQuery);
            
            if (!this.searchQuery) {
                this.searchResult = null;
                return;
            }
            // Search by No DO or NIM
            const query = this.searchQuery.toLowerCase().trim();
            
            // Use the normalized trackingData
            const sourceData = this.trackingData;
            console.log('Source data for search:', sourceData);

            // Ensure data exists before filtering
            if (!sourceData || !Array.isArray(sourceData)) {
                console.error("Tracking data is missing or invalid", sourceData);
                this.searchResult = [];
                return;
            }

            this.searchResult = sourceData.filter(item => {
                const noDO = item.noDO ? item.noDO.toLowerCase() : '';
                const nim = item.nim ? item.nim.toLowerCase() : '';
                return noDO.includes(query) || nim.includes(query);
            });
            console.log('Search result:', this.searchResult);
        },
        clearSearch() {
            this.searchQuery = '';
            this.searchResult = null;
        },
        formatDate(dateStr) {
            if (!dateStr) return '';
            const date = new Date(dateStr);
            const options = { day: 'numeric', month: 'long', year: 'numeric' };
            return date.toLocaleDateString('id-ID', options);
        },
        formatCurrency(value) {
            return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(value);
        },
        generateNoDO() {
            const year = new Date().getFullYear();
            // Count existing DOs for this year to generate sequence
            // This is a simple client-side generation, in real app should be server-side
            const count = this.trackingData.filter(d => d.noDO && d.noDO.startsWith(`DO${year}`)).length + 1;
            const sequence = String(count).padStart(3, '0');
            return `DO${year}-${sequence}`;
        },
        submitNewDO() {
            if (!this.newDO.nim || !this.newDO.nama || !this.newDO.ekspedisi || !this.newDO.paketKode) {
                alert('Mohon lengkapi data');
                return;
            }

            const noDO = this.generateNoDO();
            const doItem = {
                noDO: noDO,
                nim: this.newDO.nim,
                nama: this.newDO.nama,
                ekspedisi: this.newDO.ekspedisi,
                paket: this.selectedPaketDetail, // Store full object or just code? Prompt says "Total Harga, diambil dari data pada Array of Objects paket"
                tanggalKirim: this.newDO.tanggalKirim || new Date().toISOString().split('T')[0],
                history: [
                    {
                        waktu: new Date().toLocaleString('id-ID'),
                        keterangan: 'DO dibuat'
                    }
                ]
            };

            ApiService.addDO(doItem);
            
            // Reset form
            this.newDO = {
                nim: '',
                nama: '',
                ekspedisi: '',
                paketKode: '',
                tanggalKirim: ''
            };
            this.selectedPaketDetail = null;
            this.showAddForm = false;
            
            // Refresh search if needed or show success
            alert(`DO Berhasil dibuat: ${noDO}`);
        },
        addStatus(doItem) {
            if (!this.newStatus.keterangan) return;
            
            const status = {
                waktu: new Date().toLocaleString('id-ID'),
                keterangan: this.newStatus.keterangan
            };
            
            // We need to update the item in the list. 
            // Since doItem is a reference to the object in the array, pushing to history should work if Vue observes it.
            if (!doItem.history) this.$set(doItem, 'history', []);
            doItem.history.push(status);
            
            this.newStatus.keterangan = '';
        }
    }
});