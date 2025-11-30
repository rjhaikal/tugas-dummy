Vue.component('status-badge', {
    template: '#tpl-badge',
    props: ['stok', 'safety'],
    computed: {
        status() {
            if (this.stok === 0) return 'kosong';
            if (this.stok < this.safety) return 'menipis';
            return 'aman';
        },
        badgeClass() {
            return {
                'status-badge': true,
                'status-kosong': this.status === 'kosong',
                'status-menipis': this.status === 'menipis',
                'status-aman': this.status === 'aman'
            };
        },
        statusText() {
            if (this.status === 'kosong') return 'Kosong';
            if (this.status === 'menipis') return 'Menipis';
            return 'Aman';
        },
        icon() {
            if (this.status === 'kosong') return '⚠️'; // Bahaya
            if (this.status === 'menipis') return '⚡'; // Warning
            return '✅'; // Aman
        }
    }
});