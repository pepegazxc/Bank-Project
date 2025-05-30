const newsItems = document.querySelectorAll(".news-item, .global-news-item");

newsItems.forEach(item => {
    item.addEventListener("click", () => {
        newsItems.forEach(el => {
            if (el !== item) {
                el.classList.remove("expanded");
            }
        });
        item.classList.toggle("expanded");
    });
});

