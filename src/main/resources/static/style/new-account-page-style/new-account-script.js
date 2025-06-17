
document.addEventListener("DOMContentLoaded", () => {
    const accountData = {
        account1: {
            name: "Накопительный счет",
            info: "info",
        },
        account2: {
            name: "Вклад",
            info: "info",
        },
        account3: {
            name: "Депозитный счет",
            info: "info",
        },
        account4: {
            name: "Бюджетный счет",
            info: "info",
        }
    };

    const buttons = document.querySelectorAll(".account-button");
    const infoText = document.querySelector(".account-info");
    const accountNameSpan = document.querySelector(".account-name");

    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const accountId = button.getAttribute("data-id");
            const data = accountData[accountId];

            if (data) {
                infoText.textContent = data.info;
                accountNameSpan.textContent = data.name;
            }
        });
    });
});
