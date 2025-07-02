const toggleBtn = document.getElementById('toggle-card-info');
let isVisible = false;

const cardData = {
    number: 'скрыто',
    type: 'скрыто',
    status: 'скрыто',
    expiry: 'скрыто'
};

const elements = {
    number: document.getElementById('card-number'),
    type: document.getElementById('card-type'),
    status: document.getElementById('card-status'),
    expiry: document.getElementById('card-expiry'),
};

function updateDisplay() {
    if (isVisible) {
        cardData.number.textContent = `• Номер карты: ${elements.number}`;
        cardData.type.textContent = `• Тип: ${elements.type}`;
        cardData.status.textContent = `• Статус: ${elements.status}`;
        cardData.expiry.textContent = `• Срок действия: ${elements.expiry}`;
        cardData.textContent = 'Скрыть информацию';
    } else {
        cardData.number.textContent = '• Номер карты: **** **** **** ****';
        cardData.type.textContent = '• Тип: ****';
        cardData.status.textContent = '• Статус: ****';
        cardData.expiry.textContent = '• Срок действия: **/**';
        toggleBtn.textContent = 'Показать информацию';
    }
}

toggleBtn.addEventListener('click', () => {
    isVisible = !isVisible;
    updateDisplay();
});

updateDisplay();