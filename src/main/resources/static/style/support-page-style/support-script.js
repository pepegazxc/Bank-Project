document.getElementById('complaint-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const type = document.getElementById('complaint-type').value;
    const desc = document.getElementById('complaint-desc').value;

    if (!desc.trim()) {
        alert('Пожалуйста, введите описание жалобы.');
        return;
    }

    // Пример добавления сообщения в историю (в реальном проекте — отправка на сервер)
    const history = document.querySelector('.history');
    const newMessage = document.createElement('div');
    newMessage.classList.add('message', 'user');
    newMessage.textContent = desc;
    history.appendChild(newMessage);

    // Очистка формы
    document.getElementById('complaint-form').reset();
});
