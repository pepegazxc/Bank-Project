function checkFooterVisibility() {
    const footer = document.querySelector('.footer');
    const footerTop = footer.getBoundingClientRect().top;
    const windowHeight = window.innerHeight;

    if (footerTop <= windowHeight) {
        footer.classList.add('show');
    } else {
        footer.classList.remove('show');
    }
}

window.addEventListener('scroll', checkFooterVisibility);
window.addEventListener('resize', checkFooterVisibility);
document.addEventListener('DOMContentLoaded', checkFooterVisibility);
