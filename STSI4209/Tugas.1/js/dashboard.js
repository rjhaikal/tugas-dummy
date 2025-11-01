const user = JSON.parse(sessionStorage.getItem('sitta_user') || 'null');

if (!user) {
    window.location.href = 'index.html';
} else {
    document.getElementById('userName').textContent = user.nama;
}

document.getElementById('logoutBtn').onclick = () => {
    sessionStorage.removeItem('sitta_user');
    window.location.href = 'index.html';
};

const h = new Date().getHours();
const greet = h < 12 ? 'Selamat Pagi' : h < 18 ? 'Selamat Siang' : 'Selamat Sore';

document.getElementById('greeting').textContent = `${greet}, ${user.nama.split(' ')[0]}!`;

// Display statistics
document.getElementById('totalBahanAjar').textContent = dataBahanAjar.length;
document.getElementById('totalDO').textContent = Object.keys(dataTracking).length;

// Quick Tracking Function
const doInputDashboard = document.getElementById('doInputDashboard');
const cekBtnDashboard = document.getElementById('cekBtnDashboard');
const resultDashboard = document.getElementById('resultDashboard');

// Function to get status info
function getStatusInfo(status) {
    const statusLower = status.toLowerCase();
    if (statusLower.includes('dikirim') || statusLower.includes('selesai')) {
        return { class: 'status-success', progress: 100, icon: '✅' };
    } else if (statusLower.includes('perjalanan')) {
        return { class: 'status-warning', progress: 60, icon: '🚚' };
    } else if (statusLower.includes('proses')) {
        return { class: 'status-info', progress: 30, icon: '📦' };
    } else {
        return { class: 'status-secondary', progress: 10, icon: '📋' };
    }
}

// Event listener for tracking
cekBtnDashboard.addEventListener('click', function() {
    const nomor = doInputDashboard.value.trim();
    
    // Validasi: Cek apakah nomor DO diisi
    if (!nomor) {
        alert('Nomor DO harus diisi!');
        resultDashboard.innerHTML = `
            <div class="tracking-not-found">
                <div class="not-found-icon">⚠️</div>
                <p>Mohon masukkan nomor Delivery Order</p>
            </div>
        `;
        return;
    }
    
    const item = dataTracking[nomor];
    
    // Validasi: Nomor DO tidak ditemukan
    if (!item) {
        alert('Nomor DO tidak ditemukan!');
        resultDashboard.innerHTML = `
            <div class="tracking-not-found">
                <div class="not-found-icon">❌</div>
                <h4>Nomor DO Tidak Ditemukan</h4>
                <p>Nomor DO <strong>${nomor}</strong> tidak ada dalam sistem.</p>
            </div>
        `;
        return;
    }
    
    // Determine status color and progress
    const statusInfo = getStatusInfo(item.status);
    
    let html = `
        <div class="tracking-card-modern">
            <!-- Header Section -->
            <div class="tracking-header-modern">
                <div class="header-left">
                    <div class="status-icon">${statusInfo.icon}</div>
                    <div>
                        <div class="do-number">DO #${item.nomorDO}</div>
                        <div class="customer-name">👤 ${item.nama}</div>
                    </div>
                </div>
                <div class="status-badge-modern ${statusInfo.class}">
                    ${item.status}
                </div>
            </div>
            
            <!-- Progress Section -->
            <div class="progress-section-modern">
                <div class="progress-header">
                    <span class="progress-label-modern">Status Pengiriman</span>
                    <span class="progress-percentage">${statusInfo.progress}%</span>
                </div>
                <div class="progress-bar-modern">
                    <div class="progress-fill" style="width: ${statusInfo.progress}%">
                        <div class="progress-glow"></div>
                    </div>
                </div>
            </div>
            
            <!-- Info Cards Grid -->
            <div class="info-cards-grid">
                <div class="info-card">
                    <div class="info-icon">🚚</div>
                    <div class="info-content">
                        <div class="info-label">Ekspedisi</div>
                        <div class="info-value">${item.ekspedisi}</div>
                    </div>
                </div>
                
                <div class="info-card">
                    <div class="info-icon">📅</div>
                    <div class="info-content">
                        <div class="info-label">Tanggal Kirim</div>
                        <div class="info-value">${item.tanggalKirim}</div>
                    </div>
                </div>
                
                <div class="info-card">
                    <div class="info-icon">📦</div>
                    <div class="info-content">
                        <div class="info-label">Jenis Paket</div>
                        <div class="info-value">${item.paket}</div>
                    </div>
                </div>
                
                <div class="info-card">
                    <div class="info-icon">💰</div>
                    <div class="info-content">
                        <div class="info-label">Total Pembayaran</div>
                        <div class="info-value">${item.total}</div>
                    </div>
                </div>
            </div>
            
            <!-- Timeline Section -->
            <div class="timeline-section-modern">
                <h4 class="timeline-title">📍 Riwayat Pengiriman</h4>
                <div class="timeline-modern">
    `;
    
    item.perjalanan.forEach((p, index) => {
        const isLatest = index === item.perjalanan.length - 1;
        html += `
            <div class="timeline-item-modern ${isLatest ? 'active' : ''}">
                <div class="timeline-marker"></div>
                <div class="timeline-card">
                    <div class="timeline-time">🕒 ${p.waktu}</div>
                    <div class="timeline-description">${p.keterangan}</div>
                </div>
            </div>
        `;
    });
    
    html += `
                </div>
            </div>
        </div>
    `;
    
    resultDashboard.innerHTML = html;
    
    // Smooth scroll to result
    resultDashboard.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
});

// Also allow Enter key to trigger search
doInputDashboard.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        cekBtnDashboard.click();
    }
});

// ========== TAB SWITCHING FUNCTIONALITY ==========
const tabContents = document.querySelectorAll('.tab-content');

// Initialize first time data load flags
let monitoringLoaded = false;
let rekapLoaded = false;
let historiLoaded = false;

// Function to switch tabs
function switchTab(targetTab) {
    // Hide all tab contents
    tabContents.forEach(content => content.classList.remove('active'));
    
    // Show target tab
    const targetElement = document.getElementById(`tab-${targetTab}`);
    if (targetElement) {
        targetElement.classList.add('active');
        
        // Update nav menu active state
        const navItems = document.querySelectorAll('.nav-item');
        navItems.forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('data-tab') === targetTab) {
                item.classList.add('active');
            }
        });
        
        // Update dropdown active state
        const dropdown = document.getElementById('laporanDropdown');
        if (targetTab === 'monitoring' || targetTab === 'rekap') {
            dropdown.classList.add('active');
        } else {
            dropdown.classList.remove('active');
        }
        
        // If dashboard tab, set Dashboard nav as active
        if (targetTab === 'dashboard') {
            navItems[0].classList.add('active');
        }
        
        // Load data when tab is opened for the first time
        if (targetTab === 'monitoring' && !monitoringLoaded) {
            loadMonitoringData();
            monitoringLoaded = true;
        } else if (targetTab === 'rekap' && !rekapLoaded) {
            loadRekapData();
            rekapLoaded = true;
        } else if (targetTab === 'histori' && !historiLoaded) {
            loadHistoriData();
            historiLoaded = true;
        }
    }
}

// Handle dropdown menu clicks
document.querySelectorAll('.dropdown-menu a[data-tab]').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const targetTab = this.getAttribute('data-tab');
        switchTab(targetTab);
    });
});

// Handle histori nav link click
const historiLink = document.querySelector('.nav-item[data-tab="histori"]');
if (historiLink) {
    historiLink.addEventListener('click', function(e) {
        e.preventDefault();
        switchTab('histori');
    });
}

// Handle URL hash on page load (for links from other pages)
window.addEventListener('DOMContentLoaded', function() {
    const hash = window.location.hash.substring(1); // Remove #
    if (hash && ['monitoring', 'rekap', 'histori'].includes(hash)) {
        switchTab(hash);
    }
});

// ========== MONITORING DATA ==========
function loadMonitoringData() {
    const tbody = document.getElementById('monitoringTable');
    tbody.innerHTML = '';
    
    // Filter data based on logged in user
    const userTransactions = Object.keys(dataTracking).filter(key => {
        return dataTracking[key].nama === user.nama;
    });
    
    if (userTransactions.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center; color: #999;">Belum ada data monitoring</td>
            </tr>
        `;
        return;
    }
    
    userTransactions.forEach(key => {
        const item = dataTracking[key];
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>${item.nomorDO}</strong></td>
            <td>${item.nama}</td>
            <td><span class="badge ${item.status === 'Dikirim' ? 'badge-success' : 'badge-warning'}">${item.status}</span></td>
            <td>${item.ekspedisi}</td>
            <td>${item.tanggalKirim}</td>
        `;
        tbody.appendChild(tr);
    });
}

// ========== REKAP DATA ==========
function loadRekapData() {
    // Calculate statistics
    const totalItems = dataBahanAjar.length;
    const totalStok = dataBahanAjar.reduce((sum, item) => sum + item.stok, 0);
    const stokRendah = dataBahanAjar.filter(item => item.stok < 200).length;
    
    document.getElementById('totalItems').textContent = totalItems;
    document.getElementById('totalStok').textContent = totalStok;
    document.getElementById('stokRendah').textContent = stokRendah;
    
    // Group by location
    const locationMap = {};
    dataBahanAjar.forEach(item => {
        if (!locationMap[item.kodeLokasi]) {
            locationMap[item.kodeLokasi] = { count: 0, stok: 0 };
        }
        locationMap[item.kodeLokasi].count++;
        locationMap[item.kodeLokasi].stok += item.stok;
    });
    
    // Display rekap table
    const tbody = document.getElementById('rekapTable');
    tbody.innerHTML = '';
    
    Object.keys(locationMap).forEach(lokasi => {
        const data = locationMap[lokasi];
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>${lokasi}</strong></td>
            <td>${data.count} item</td>
            <td>${data.stok} unit</td>
        `;
        tbody.appendChild(tr);
    });
}

// ========== HISTORI DATA ==========
function loadHistoriData() {
    const tbody = document.getElementById('historiTable');
    tbody.innerHTML = '';
    
    // Filter data based on logged in user
    const userTransactions = Object.keys(dataTracking).filter(key => {
        return dataTracking[key].nama === user.nama;
    });
    
    if (userTransactions.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align: center; color: #999;">Belum ada data transaksi</td>
            </tr>
        `;
        return;
    }
    
    userTransactions.forEach(key => {
        const item = dataTracking[key];
        const tr = document.createElement('tr');
        
        // Get item count from perjalanan (assuming first event shows items)
        const itemCount = item.paket ? '1 paket' : '-';
        
        tr.innerHTML = `
            <td>${item.tanggalKirim}</td>
            <td><strong>${item.nomorDO}</strong></td>
            <td>${item.nama}</td>
            <td>${itemCount}</td>
            <td><strong>${item.total}</strong></td>
            <td><span class="badge ${item.status === 'Dikirim' ? 'badge-success' : 'badge-warning'}">${item.status}</span></td>
        `;
        tbody.appendChild(tr);
    });
}