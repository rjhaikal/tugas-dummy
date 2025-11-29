const { createApp } = Vue;

createApp({
    data() {
        return {
            upbjjList: ["Jakarta", "Surabaya", "Makassar", "Padang", "Denpasar"],
            kategoriList: ["MK Wajib", "MK Pilihan", "Praktikum", "Problem-Based"],
            stokData: [
                {
                    kode: "EKMA4116",
                    judul: "Pengantar Manajemen",
                    kategori: "MK Wajib",
                    upbjj: "Jakarta",
                    lokasiRak: "R1-A3",
                    harga: 65000,
                    qty: 28,
                    safety: 20,
                    catatanHTML: "<em>Edisi 2024, cetak ulang</em>"
                },
                {
                    kode: "EKMA4115",
                    judul: "Pengantar Akuntansi",
                    kategori: "MK Wajib",
                    upbjj: "Jakarta",
                    lokasiRak: "R1-A4",
                    harga: 60000,
                    qty: 7,
                    safety: 15,
                    catatanHTML: "<strong>Cover baru</strong>"
                },
                {
                    kode: "BIOL4201",
                    judul: "Biologi Umum (Praktikum)",
                    kategori: "Praktikum",
                    upbjj: "Surabaya",
                    lokasiRak: "R3-B2",
                    harga: 80000,
                    qty: 12,
                    safety: 10,
                    catatanHTML: "Butuh <u>pendingin</u> untuk kit basah"
                },
                {
                    kode: "FISIP4001",
                    judul: "Dasar-Dasar Sosiologi",
                    kategori: "MK Pilihan",
                    upbjj: "Makassar",
                    lokasiRak: "R2-C1",
                    harga: 55000,
                    qty: 2,
                    safety: 8,
                    catatanHTML: "Stok <i>menipis</i>, prioritaskan reorder"
                }
            ],
            filterUpbjj: '',
            filterKategori: '',
            filterReOrder: false,
            sortBy: '',
            editIndex: null,
            editForm: {
                qty: 0
            },
            formBaru: {
                kode: '',
                judul: '',
                kategori: '',
                upbjj: '',
                lokasiRak: '',
                qty: 0,
                safety: 0,
                harga: 0,
                catatanHTML: ''
            },
            errors: {}
        }
    },
    computed: {
        filteredStok() {
            let result = [...this.stokData];
            
            if (this.filterUpbjj) {
                result = result.filter(item => item.upbjj === this.filterUpbjj);
            }
            
            if (this.filterKategori && this.filterUpbjj) {
                result = result.filter(item => item.kategori === this.filterKategori);
            }
            
            if (this.filterReOrder) {
                result = result.filter(item => item.qty < item.safety || item.qty === 0);
            }
            
            if (this.sortBy === 'judul') {
                result.sort((a, b) => a.judul.localeCompare(b.judul));
            } else if (this.sortBy === 'qty') {
                result.sort((a, b) => a.qty - b.qty);
            } else if (this.sortBy === 'harga') {
                result.sort((a, b) => a.harga - b.harga);
            }
            
            return result;
        }
    },
    
    watch: {
        filterUpbjj(newVal, oldVal) {
            console.log(`Filter UPBJJ berubah dari "${oldVal}" ke "${newVal}"`);
            if (newVal !== oldVal) {
                this.filterKategori = '';
            }
        },
        
        filterReOrder(newVal) {
            console.log(`Filter Re-Order: ${newVal ? 'AKTIF' : 'TIDAK AKTIF'}`);
            if (newVal) {
                console.log(`Menampilkan ${this.filteredStok.length} item yang perlu re-order`);
            }
        },
        
        sortBy(newVal) {
            if (newVal) {
                console.log(`Data diurutkan berdasarkan: ${newVal}`);
            }
        }
    },
    
    methods: {
        formatRupiah(angka) {
            return new Intl.NumberFormat('id-ID', {
                style: 'currency',
                currency: 'IDR',
                minimumFractionDigits: 0
            }).format(angka);
        },
        
        resetFilter() {
            this.filterUpbjj = '';
            this.filterKategori = '';
            this.filterReOrder = false;
            this.sortBy = '';
            console.log('Semua filter direset');
        },
        
        onUpbjjChange() {
            console.log('UPBJJ dipilih, filter kategori sekarang tersedia');
        },
        
        startEdit(index) {
            this.editIndex = index;
            this.editForm.qty = this.stokData[index].qty;
        },
        
        saveEdit() {
            if (this.editForm.qty < 0) {
                alert('Jumlah stok tidak boleh negatif!');
                return;
            }
            
            this.stokData[this.editIndex].qty = this.editForm.qty;
            this.cancelEdit();
            alert('Stok berhasil diupdate!');
        },
        
        cancelEdit() {
            this.editIndex = null;
            this.editForm.qty = 0;
        },
        
        validateForm() {
            this.errors = {};
            let isValid = true;
            
            if (!/^[A-Z]{4}\d{4}$/.test(this.formBaru.kode)) {
                this.errors.kode = 'Format kode harus seperti: EKMA4216 (4 huruf kapital + 4 angka)';
                isValid = false;
            }
            
            if (this.stokData.find(item => item.kode === this.formBaru.kode)) {
                this.errors.kode = 'Kode mata kuliah sudah ada dalam database';
                isValid = false;
            }
            
            if (this.formBaru.judul.trim().length < 5) {
                this.errors.judul = 'Nama mata kuliah minimal 5 karakter';
                isValid = false;
            }
            
            if (this.formBaru.qty < 0) {
                this.errors.qty = 'Jumlah stok tidak boleh negatif';
                isValid = false;
            }
            
            return isValid;
        },
        
        tambahBahanAjar() {
            if (!this.validateForm()) {
                alert('Mohon perbaiki kesalahan pada form!');
                return;
            }
            
            const newItem = {
                kode: this.formBaru.kode.toUpperCase(),
                judul: this.formBaru.judul,
                kategori: this.formBaru.kategori,
                upbjj: this.formBaru.upbjj,
                lokasiRak: this.formBaru.lokasiRak,
                qty: parseInt(this.formBaru.qty),
                safety: parseInt(this.formBaru.safety),
                harga: parseInt(this.formBaru.harga),
                catatanHTML: this.formBaru.catatanHTML || 'Baru ditambahkan'
            };
            
            this.stokData.push(newItem);
            
            this.resetForm();
            
            alert(`Bahan ajar "${newItem.judul}" berhasil ditambahkan!`);
        },
        
        resetForm() {
            this.formBaru = {
                kode: '',
                judul: '',
                kategori: '',
                upbjj: '',
                lokasiRak: '',
                qty: 0,
                safety: 0,
                harga: 0,
                catatanHTML: ''
            };
            this.errors = {};
        }
    },
    
    mounted() {
        console.log('Vue App untuk Stok Bahan Ajar telah dimount');
        console.log(`Total ${this.stokData.length} bahan ajar dimuat`);
    }
}).mount('#app');
