
    document.addEventListener("DOMContentLoaded", () => {
    const cardData = {
    card1: {
    name: "Дебетовая",
    info: "Дебетовая карта с кешбэком до 5%. Без годового обслуживания.",
    link: "/order-card-type-1"
},
    card2: {
    name: "Кредитная",
    info: "Кредитная карта с льготным периодом до 120 дней.",
    link: "/order-card-type-2"
},
    card3: {
    name: "Виртуальная",
    info: "Виртуальная карта для онлайн-покупок. Быстрая выдача.",
    link: "/order-card-type-3"
},
    card4: {
    name: "Премиальная",
    info: "Премиальная карта с бонусами на путешествия.",
    link: "/order-card-type-4"
}
};

    const buttons = document.querySelectorAll(".card-button");
    const infoText = document.querySelector(".card-info");
    const orderLink = document.querySelector(".apply-for-a-card-container");
    const cardNameSpan = document.querySelector(".card-name");

    buttons.forEach(button => {
    button.addEventListener("click", () => {
    const cardId = button.getAttribute("data-id");
    const data = cardData[cardId];

    if (data) {
    infoText.textContent = data.info;
    orderLink.setAttribute("href", data.link);
    cardNameSpan.textContent = data.name;
}
});
});
});

