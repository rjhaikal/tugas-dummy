Vue.component('ba-stock-table', {
    template: '#tpl-stock',
    props: ['items'],
    data() {
        return {
            filters: {
                upbjj: '',
                kategori: '',
                lowStock: false
            },
            sortBy: 'judul', // judul, qty, harga
            sortDesc: false,
            newStock: {
                kode: '',
                judul: '',
                kategori: '',
                upbjj: '',
                lokasiRak: '',
                harga: 0,
                qty: 0,
                safety: 0,
                catatan: ''
            },
            editingId: null, // Use a unique identifier or index
            editForm: {},
            upbjjOptions: [], // Will be populated from ApiService
            kategoriOptions: [] // Will be populated from ApiService
        };
    },
    async created() {
        // Load options for filters and forms
        const data = await ApiService.init(); // Ensure data is loaded or get from cache
        if (data) {
            this.upbjjOptions = data.upbjjList;
            this.kategoriOptions = data.kategoriList;
        }
    },
    watch: {
        'filters.upbjj': function(newVal) {
            // Reset kategori when UPBJJ changes
            if (!newVal) {
                this.filters.kategori = '';
            }
        }
    },
    computed: {
        filteredItems() {
            let result = this.items.slice();

            // Filter by UPBJJ
            if (this.filters.upbjj) {
                result = result.filter(item => item.upbjj === this.filters.upbjj);
            }

            // Filter by Kategori
            if (this.filters.kategori) {
                result = result.filter(item => item.kategori === this.filters.kategori);
            }

            // Filter Low Stock
            if (this.filters.lowStock) {
                result = result.filter(item => item.qty < item.safety || item.qty === 0);
            }

            // Sort
            result.sort((a, b) => {
                let valA = a[this.sortBy];
                let valB = b[this.sortBy];

                if (typeof valA === 'string') valA = valA.toLowerCase();
                if (typeof valB === 'string') valB = valB.toLowerCase();

                if (valA < valB) return this.sortDesc ? 1 : -1;
                if (valA > valB) return this.sortDesc ? -1 : 1;
                return 0;
            });

            return result;
        },
        availableKategoriOptions() {
            // Dependent options: if UPBJJ is selected, maybe filter categories?
            // The prompt says: "Implementasikan dependent options seperti misalkan ketika memilih filter stok berdasarkan ‘UT-daerah’, baru akan memunculkan filter untuk ‘Kategori Mata Kuliah’"
            // It implies the filter UI for Kategori only appears if UPBJJ is selected.
            return this.kategoriOptions;
        }
    },
    methods: {
        formatCurrency(value) {
            return new Intl.NumberFormat('id-ID', { style: 'currency', currency: 'IDR' }).format(value);
        },
        resetFilters() {
            this.filters.upbjj = '';
            this.filters.kategori = '';
            this.filters.lowStock = false;
            this.sortBy = 'judul';
            this.sortDesc = false;
        },
        addStock() {
            // Simple validation
            if (!this.newStock.kode || !this.newStock.judul) return;

            const itemToAdd = { ...this.newStock };
            // Convert numbers
            itemToAdd.harga = Number(itemToAdd.harga);
            itemToAdd.qty = Number(itemToAdd.qty);
            itemToAdd.safety = Number(itemToAdd.safety);

            ApiService.addStock(itemToAdd);
            
            // Reset form
            this.newStock = {
                kode: '',
                judul: '',
                kategori: '',
                upbjj: '',
                lokasiRak: '',
                harga: 0,
                qty: 0,
                safety: 0,
                catatan: ''
            };
        },
        startEdit(item) {
            this.editingId = item.kode + item.upbjj; // Assuming composite key
            this.editForm = { ...item };
        },
        cancelEdit() {
            this.editingId = null;
            this.editForm = {};
        },
        saveEdit() {
            ApiService.updateStock(this.editForm);
            this.editingId = null;
            this.editForm = {};
        },
        confirmDelete(item) {
            // Use the app-modal if possible, but here we need to access the root or ref
            // Since we are inside a component, we can emit an event or use $root.$refs.modal if structured that way
            // Or just use window.confirm for simplicity if modal ref is hard to reach, 
            // BUT the prompt says "app-modal" component exists and "Terdapat pop up yang mengkonfirmasi".
            // Let's try to find the modal on root.
            
            if (this.$root.$refs.modal) {
                this.$root.$refs.modal.show(
                    'Konfirmasi Hapus',
                    `Apakah Anda yakin ingin menghapus stok ${item.judul}?`,
                    () => {
                        ApiService.deleteStock(item);
                    }
                );
            } else {
                if (confirm(`Apakah Anda yakin ingin menghapus stok ${item.judul}?`)) {
                    ApiService.deleteStock(item);
                }
            }
        }
    }
});