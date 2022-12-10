let cartItem = document.querySelector('.cart-items-container');

document.querySelector('#cart-btn').onclick = () =>{
	cartItem.classList.toggle('active');
}

window.onscroll = () =>{
	cartItem.classList.remove('active');
}