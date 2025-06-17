const toggleBtn = document.getElementById('toggle-card-info');
let isVisible = false;

const cardData = {
    number: '1234 5678 9012 3456',
    type: 'Дебетовая',
    status: 'Активна',
    expiry: '12/27',
};

const elements = {
    number: document.getElementById('card-number'),
    type: document.getElementById('card-type'),
    status: document.getElementById('card-status'),
    expiry: document.getElementById('card-expiry'),
};

function updateDisplay() {
    if (isVisible) {
        elements.number.textContent = `• Номер карты: ${cardData.number}`;
        elements.type.textContent = `• Тип: ${cardData.type}`;
        elements.status.textContent = `• Статус: ${cardData.status}`;
        elements.expiry.textContent = `• Срок действия: ${cardData.expiry}`;
        toggleBtn.textContent = 'Скрыть информацию';
    } else {
        elements.number.textContent = '• Номер карты: **** **** **** ****';
        elements.type.textContent = '• Тип: ****';
        elements.status.textContent = '• Статус: ****';
        elements.expiry.textContent = '• Срок действия: **/**';
        toggleBtn.textContent = 'Показать информацию';
    }
}

toggleBtn.addEventListener('click', () => {
    isVisible = !isVisible;
    updateDisplay();
});

updateDisplay();