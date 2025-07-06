document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".card-button");
    const infoText = document.querySelector(".card-info");
    const orderLink = document.querySelector(".apply-for-a-card-container");
    const cardNameSpan = document.querySelector(".card-name");

    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const name = button.dataset.name;
            const info = button.dataset.info;
            const link = button.dataset.link;

            cardNameSpan.textContent = name;
            infoText.textContent = info;
            orderLink.setAttribute("href", link);
        });
    });
});
