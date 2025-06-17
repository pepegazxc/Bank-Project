document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("menu-toggle");
    const menu = document.getElementById("dropdown-menu");

    toggle.addEventListener("click", () => {
        menu.classList.toggle("show");
    });

    document.addEventListener("click", (event) => {
        if (!toggle.contains(event.target) && !menu.contains(event.target)) {
            menu.classList.remove("show");
        }
    });
});
