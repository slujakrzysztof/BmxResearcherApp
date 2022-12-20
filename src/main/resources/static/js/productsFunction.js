let discountWindow = document.querySelector('.discount-container');
let cartItem = document.querySelector('.cart-items-container');

document.querySelector('#cart-btn').onclick = () => {
	cartItem.classList.toggle('active');
}

function setActive() {
	discountWindow.classList.toggle('active');
}

function updateDiscountValue(val){
	document.getElementById("discountValueText").value=val;
}

function updateDiscountSlider(val){
	document.getElementById("customRange3").value=val;
}