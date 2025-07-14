const toggleBtn = document.getElementById('toggle-card-info');
let isVisible = false;

const cardTypeMap = {
    1: 'Дебетовая',
    2: 'Кредитная',
    3: 'Виртуальная',
    4: 'Премиальная'
};

const elements = {
    number: document.getElementById('card-number'),
    type: document.getElementById('card-type'),
    status: document.getElementById('card-status'),
    expiry: document.getElementById('card-expiry'),
};

const realData = {
    number: elements.number.dataset.cardnumber,
    type: cardTypeMap[elements.type.dataset.cardtype],
    status: elements.status.dataset.cardstatus === 'true' ? 'Активна' : 'Неактивна',
    expiry: elements.expiry.dataset.cardexpirationdate
};

function updateDisplay() {
    if (isVisible) {
        elements.number.textContent = `• Номер карты: ${realData.number}`;
        elements.type.textContent = `• Тип: ${realData.type}`;
        elements.status.textContent = `• Статус: ${realData.status}`;
        elements.expiry.textContent = `• Срок действия: ${realData.expiry}`;
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
