document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".card-button");
    const infoBlock = document.querySelector(".card-info");
    const applyLink = document.querySelector(".apply-for-a-card-container");

    buttons.forEach(button => {
        button.addEventListener("click", () => {
            const cardInfo = button.dataset.info;
            const cardLink = button.dataset.link;

            infoBlock.textContent = cardInfo;
            applyLink.href = cardLink;


            infoBlock.classList.remove("fade");
            void infoBlock.offsetWidth;
            infoBlock.classList.add("fade");
        });
    });
});
