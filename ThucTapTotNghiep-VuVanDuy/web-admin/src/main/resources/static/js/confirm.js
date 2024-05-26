const deleteButton = document.getElementById('deleteButton');
const confirmDialog = document.getElementById('confirmDialog');
const yesButton = document.getElementById('yesButton');
const noButton = document.getElementById('noButton');

deleteButton.addEventListener('click', () => {
    confirmDialog.style.display = 'block';
});

noButton.addEventListener('click', () => {
    confirmDialog.style.display = 'none';
});

yesButton.addEventListener('click', () => {
    // Thêm hành động xóa ở đây

    confirmDialog.style.display = 'none';
});

