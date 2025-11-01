const loginForm = document.getElementById('loginForm');
const modal = document.getElementById('modal');
const modalBody = document.getElementById('modalBody');
const modalClose = document.getElementById('modalClose');

document.getElementById('forgotBtn').onclick = () => {
    modalBody.innerHTML = '<h3>Lupa Password</h3><p>Silakan hubungi admin: admin@ut.ac.id</p>';
    modal.classList.remove('hidden');
};

document.getElementById('registerBtn').onclick = () => {
    modalBody.innerHTML = '<h3>Daftar</h3><p>Gunakan akun dummy dari data.js</p>';
    modal.classList.remove('hidden');
};

modalClose.onclick = () => modal.classList.add('hidden');

modal.onclick = e => {
    if (e.target === modal) modal.classList.add('hidden');
};

loginForm.addEventListener('submit', e => {
    e.preventDefault();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    
    // Validasi: Cek apakah email dan password diisi
    if (!email || !password) {
        alert('Email dan Password harus diisi!');
        return;
    }
    
    // Cari user di database
    const found = dataPengguna.find(u => u.email === email && u.password === password);
    
    // Validasi: User tidak ditemukan
    if (!found) {
        alert('Email atau Password salah!');
        return;
    }
    
    // Login berhasil
    sessionStorage.setItem('sitta_user', JSON.stringify(found));
    window.location.href = 'dashboard.html';
});