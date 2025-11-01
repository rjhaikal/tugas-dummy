const listContainer = document.getElementById('listBahanAjar');
const detailContainer = document.getElementById('detailBahanAjar');
let selectedIndex = null;

// Render daftar bahan ajar
function renderList() {
    listContainer.innerHTML = '';
    dataBahanAjar.forEach((item, index) => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'list-item' + (selectedIndex === index ? ' active' : '');
        itemDiv.innerHTML = `
            <div class="list-item-content">
                <img src="${item.cover}" alt="${item.namaBarang}" class="list-item-img" onerror="this.src='assets/placeholder.jpg'; this.onerror=null;">
                <div class="list-item-info">
                    <strong>${item.namaBarang}</strong>
                    <small>${item.kodeBarang}</small>
                </div>
            </div>
        `;
        itemDiv.onclick = () => showDetail(index);
        listContainer.appendChild(itemDiv);
    });
}

// Tampilkan detail bahan ajar
function showDetail(index) {
    selectedIndex = index;
    const item = dataBahanAjar[index];
    
    detailContainer.innerHTML = `
        <div class="detail-content">
            <div class="detail-image">
                <img src="${item.cover}" alt="${item.namaBarang}" class="book-cover-large" onerror="this.src='assets/placeholder.jpg'; this.onerror=null;">
            </div>
            <div class="detail-info">
                <h3>${item.namaBarang}</h3>
                <table class="detail-table">
                    <tr>
                        <td><strong>Kode Barang:</strong></td>
                        <td>${item.kodeBarang}</td>
                    </tr>
                    <tr>
                        <td><strong>Kode Lokasi:</strong></td>
                        <td>${item.kodeLokasi}</td>
                    </tr>
                    <tr>
                        <td><strong>Jenis:</strong></td>
                        <td>${item.jenisBarang}</td>
                    </tr>
                    <tr>
                        <td><strong>Edisi:</strong></td>
                        <td>${item.edisi}</td>
                    </tr>
                    <tr>
                        <td><strong>Stok Tersedia:</strong></td>
                        <td><span class="badge ${item.stok > 100 ? 'badge-success' : item.stok > 50 ? 'badge-warning' : 'badge-danger'}">${item.stok} unit</span></td>
                    </tr>
                </table>
            </div>
        </div>
    `;
    
    renderList(); // Re-render untuk update active state
}

// Initial render
renderList();

// Handle form submit
document.getElementById('addForm').addEventListener('submit', e => {
    e.preventDefault();
    
    const kodeLokasi = document.getElementById('kodeLokasi');
    const kodeBarang = document.getElementById('kodeBarang');
    const namaBarang = document.getElementById('namaBarang');
    const jenisBarang = document.getElementById('jenisBarang');
    const edisi = document.getElementById('edisi');
    const stok = document.getElementById('stok');
    const cover = document.getElementById('cover');
    
    // Validasi: Semua field harus diisi
    if (!kodeLokasi.value.trim() || !kodeBarang.value.trim() || !namaBarang.value.trim() || 
        !jenisBarang.value.trim() || !edisi.value.trim() || !stok.value.trim() || !cover.value.trim()) {
        alert('Semua field harus diisi!');
        return;
    }
    
    const newItem = {
        kodeLokasi: kodeLokasi.value.trim(),
        kodeBarang: kodeBarang.value.trim(),
        namaBarang: namaBarang.value.trim(),
        jenisBarang: jenisBarang.value.trim(),
        edisi: edisi.value.trim(),
        stok: Number(stok.value),
        cover: cover.value.trim()
    };
    
    dataBahanAjar.push(newItem);
    renderList();
    e.target.reset();
    
    alert('Bahan ajar berhasil ditambahkan!');
});