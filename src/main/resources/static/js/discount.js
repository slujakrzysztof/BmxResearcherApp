const discountIcon = document.querySelector('.discount-button')
const discountContainer = document.querySelector('.discount-container')

discountContainer.inWindow = 0



discountIcon.onmouseover = () => {
	if (discountContainer.classList.contains('hide'))
		discountContainer.classList.remove('hide')
}

discountIcon.onmouseleave = () => {
		setTimeout(()=>{
			if(discountContainer.inWindow===0){
				discountContainer.classList.add('hide')
			}
		},500)
}

discountContainer.onmouseover = () => {
	discountContainer.inWindow=1;
}

discountContainer.onmouseleave = () => {
	discountContainer.inWindow=0;
	discountContainer.classList.add('hide')
}
