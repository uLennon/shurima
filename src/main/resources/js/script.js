function toggleMatchVisibility(matchId,buttonId) {
    var matchDiv = document.getElementById(matchId);
    var button = document.getElementById(buttonId);
    if (matchDiv) {
        if (matchDiv.style.display === "none" || matchDiv.style.display === "") {
            matchDiv.style.display = "flex";
            button.textContent = "Hide details";
        } else {
            matchDiv.style.display = "none";
            button.textContent = "See details";
        }
    }
}

function aplicarClassePorTexto(id) {
    let elemento = document.getElementById(id);
    let texto = elemento.innerText.toLowerCase();
    elemento.classList.remove("victory", "defeat");
    if (texto === "Win") {
        elemento.classList.add("victory");
    } else if (texto === "Lose") {
        elemento.classList.add("defeat");
    }
}