const doInput = document.getElementById('doInput');
const cekBtn = document.getElementById('cekBtn');
const result = document.getElementById('result');

cekBtn.onclick = () => {
    const nomor = doInput.value.trim();
    
    // Validasi: Cek apakah nomor DO diisi
    if (!nomor) {
        alert('Nomor DO harus diisi!');
        result.innerHTML = '';
        return;
    }
    
    const item = dataTracking[nomor];
    
    // Validasi: Nomor DO tidak ditemukan
    if (!item) {
        alert('Nomor DO tidak ditemukan!');
        result.innerHTML = `
            <div style="text-align: center; padding: 40px; background: #fff3cd; border-radius: 8px; border: 1px solid #ffc107;">
                <div style="font-size: 48px; margin-bottom: 16px;">📦</div>
                <h3 style="color: #856404; margin: 0 0 8px 0;">Nomor DO Tidak Ditemukan</h3>
                <p style="color: #856404; margin: 0;">Nomor DO <strong>${nomor}</strong> tidak ada dalam sistem.</p>
            </div>
        `;
        return;
    }
    
    // Tracking ditemukan - tampilkan data
    let html = `
        <div style="background: #d4edda; padding: 16px; border-radius: 8px; border: 1px solid #c3e6cb; margin-bottom: 16px;">
            <p style="color: #155724; margin: 0; font-weight: bold;">✅ Tracking ditemukan!</p>
        </div>
        <h3>DO: ${item.nomorDO}</h3>
        <p><b>Nama:</b> ${item.nama}</p>
        <p><b>Status:</b> ${item.status}</p>
        <p><b>Ekspedisi:</b> ${item.ekspedisi}</p>
        <p><b>Tanggal Kirim:</b> ${item.tanggalKirim}</p>
        <p><b>Paket:</b> ${item.paket}</p>
        <p><b>Total:</b> ${item.total}</p>
        <h4 style="margin-top: 20px;">Riwayat Perjalanan:</h4>
        <ul>
    `;
    
    item.perjalanan.forEach(p => {
        html += `<li>${p.waktu} — ${p.keterangan}</li>`;
    });
    
    html += '</ul>';
    result.innerHTML = html;
};