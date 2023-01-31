const cartIcon = document.querySelector('.fa-cart-shopping')
const cartContainer = document.querySelector('.shopping-cart-container')

cartContainer.inWindow = 0



cartIcon.onmouseover = () => {
	if (cartContainer.classList.contains('hide'))
		cartContainer.classList.remove('hide')
}

cartIcon.onmouseleave = () => {
		setTimeout(()=>{
			if(cartContainer.inWindow===0){
				cartContainer.classList.add('hide')
			}
		},500)
}

cartContainer.onmouseover = () => {
	cartContainer.inWindow=1;
}

cartContainer.onmouseleave = () => {
	cartContainer.inWindow=0;
	cartContainer.classList.add('hide')
}
