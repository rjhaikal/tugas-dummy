const app = new Vue({
    el: '#app',
    data: {
        tab: 'stok',
        state: {
            stok: [],
            tracking: [],
            paket: [],
            pengirimanList: [],
            upbjjList: [],
            kategoriList: []
        }
    },
    async created() {
        const data = await ApiService.init();
        if (data) {
            this.state = data;
        }
    },
    methods: {
        handleNewDO(doItem) {
            ApiService.addDO(doItem);
            // Show success message via modal if possible, or alert
            if (this.$refs.modal) {
                this.$refs.modal.show(
                    'Sukses',
                    `Pesanan berhasil dibuat dengan Nomor DO: ${doItem.noDO}`,
                    () => {
                        this.tab = 'tracking';
                    }
                );
            } else {
                alert(`Pesanan berhasil dibuat dengan Nomor DO: ${doItem.noDO}`);
                this.tab = 'tracking';
            }
        }
    }
});