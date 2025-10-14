const button = document.getElementById('changeText');
const text = document.getElementById('text');

button.addEventListener('click', () => {
  text.textContent = 'Teks ini telah diubah menggunakan JavaScript!';
  alert('Teks berhasil diubah 🎉');
});

