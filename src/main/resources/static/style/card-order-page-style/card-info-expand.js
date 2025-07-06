document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".card-button");
    const infoText = document.querySelector(".card-info");
    const lawInfo = document.querySelector(".card-law")
    const cashbackInfo = document.querySelector(".card-cashback")
    const orderLink = document.querySelector(".apply-for-a-card-container");
    const cardNameSpan = document.querySelector(".card-name");

    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const name = button.dataset.name;
            const info = button.dataset.info;
            const law = button.dataset.law;
            const cashback = parseFloat(button.dataset.cashback);
            const link = button.dataset.link;

            cardNameSpan.textContent = name;
            infoText.textContent = info;
            lawInfo.textContent = law + "₽";
            cashbackInfo.textContent = cashback.toFixed(1)+ "%";
            orderLink.setAttribute("href", link);
        });
    });
});
