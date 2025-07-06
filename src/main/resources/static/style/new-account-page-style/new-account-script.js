document.addEventListener("DOMContentLoaded", () => {
    const container = document.querySelector(".first-column");
    const accountName = document.querySelector(".account-name");
    const infoText = document.querySelector(".account-info");
    const percentInfo = document.querySelector(".account-percent");

    container.addEventListener("click", (event) => {
        // ищем ближайший родитель с классом card-button
        const button = event.target.closest(".card-button");
        if (!button) return; // если клик был вне .card-button — ничего не делаем

        const name = button.dataset.name;
        const info = button.dataset.info;
        const percent = parseFloat(button.dataset.percent);

        accountName.textContent = name;
        infoText.textContent = info;
        percentInfo.textContent = percent.toFixed(1) + "%";
    });
});


