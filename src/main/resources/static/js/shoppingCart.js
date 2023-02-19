
var plusButton = document.getElementsByClassName('plus-button');
var minusButton = document.getElementsByClassName('minus-button');
var minusInput = document.getElementsByClassName('minus-input');
var plusInput = document.getElementsByClassName('plus-input');

for (let step = 0; step < plusInput.length; step++) {
	echo('Siema');
	plusButton[step].addEventListener("click", function() {
		minusInput[step].setAttribute('disabled', 'disabled');
		plusInput[step].removeAttribute("disabled");
	});
	
	minusButton[step].addEventListener("click", function() {
		plusInput[step].setAttribute('disabled', 'disabled');
		minusInput[step].removeAttribute("disabled");
	});

}



