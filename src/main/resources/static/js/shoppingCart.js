

const form = document.getElementById('quantityForm');
const plusButton = document.getElementById('plus-button');
const minusButton = document.getElementById('minus-button');
const minusInput = document.getElementById('minus-input');
const plusInput = document.getElementById('plus-input');

plusButton.addEventListener("click", function() {
	plusInput.removeAttribute("disabled");
	minusInput.setAttribute('disabled', 'disabled');
});

minusButton.addEventListener("click", function() {
	minusInput.removeAttribute("disabled");
	plusInput.setAttribute('disabled', 'disabled');
});