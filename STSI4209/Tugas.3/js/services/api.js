const ApiService = {
    data: {
        upbjjList: [],
        kategoriList: [],
        pengirimanList: [],
        paket: [],
        stok: [],
        tracking: []
    },

    async init() {
        // Prevent multiple fetches if data is already loaded
        if (this.data.stok.length > 0 || this.data.tracking.length > 0) {
            return this.data;
        }

        try {
            // Add timestamp to prevent caching
            const response = await fetch(`data/dataBahanAjar.json?t=${new Date().getTime()}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const json = await response.json();
            
            this.data.upbjjList = json.upbjjList || [];
            this.data.kategoriList = json.kategoriList || [];
            this.data.pengirimanList = json.pengirimanList || [];
            this.data.paket = json.paket || [];
            this.data.stok = json.stok || [];
            this.data.tracking = json.tracking || [];
            
            console.log('Data loaded successfully:', this.data);
            return this.data;
        } catch (error) {
            console.error('Error loading data:', error);
            return null;
        }
    },

    // Stock Methods
    addStock(item) {
        this.data.stok.push(item);
    },

    updateStock(updatedItem) {
        // Find index by unique key (kode + upbjj) instead of object reference
        const index = this.data.stok.findIndex(item => 
            item.kode === updatedItem.kode && item.upbjj === updatedItem.upbjj
        );
        
        if (index !== -1) {
            // Use splice to trigger Vue reactivity
            this.data.stok.splice(index, 1, updatedItem);
        }
    },

    deleteStock(item) {
        const index = this.data.stok.indexOf(item);
        if (index !== -1) {
            this.data.stok.splice(index, 1);
        }
    },

    // DO Methods
    addDO(doItem) {
        this.data.tracking.push(doItem);
    },

    getDOByNo(noDO) {
        return this.data.tracking.find(t => t.noDO === noDO);
    },

    getDOByNIM(nim) {
        return this.data.tracking.filter(t => t.nim === nim);
    }
};
