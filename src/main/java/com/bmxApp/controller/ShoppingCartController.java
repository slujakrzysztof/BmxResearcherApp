package com.bmxApp.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.service.ProductRepositoryService;
import com.bmxApp.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

	//List<BasketProduct> basketProducts;
	//Map<Integer, String> basketProductsPrices = new TreeMap<>();

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ProductRepositoryService productRepositoryService;

	@GetMapping({ "/cart", "/cart/{shop}" })
	public String showShoppingCart(@PathVariable(required = false) String shopName, Model model) {

		/*for (BasketProduct bProduct : basketProducts) {
			basketProductsPrices.put(bProduct.getId(),
					shoppingCartService.formatPrice(shoppingCartService.getTotalPriceForProduct(bProduct.getId())));
			System.out.println(basketProductsPrices.toString());
		}*/

		model.addAttribute("shopModel", new ShopModelDTO());
		//model.addAttribute("totalPriceByProduct", basketProductsPrices);
		model.addAttribute("totalPrice", shoppingCartService.formatPrice(shoppingCartService.getTotalPriceForShop(shopName)));
		model.addAttribute("totalDiscount", shoppingCartService.getTotalDiscount(shopName));
		model.addAttribute("finalPrice", shoppingCartService.getFinalPrice(shopName));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProductsInCart(shopName));
		return "basket";
	}

	@PostMapping("/quantityChangedPlus")
	public String changeQuantityPlus(@ModelAttribute("basketProduct") BasketProduct basketProduct, Model model,
			BindingResult bindingResult) {

		shoppingCartService.changeQuantity(basketProduct, 1);

		return "redirect:/cart";
	}

	@PostMapping("/deleteProducts")
	public String deleteBasketProducts() {
		
		shoppingCartService.deleteBasketProducts();
		return "basket";
	}

	@PostMapping("/quantityChangedMinus")
	public String changeQuantityMinus(@ModelAttribute("basketProduct") BasketProduct basketProduct, Model model,
			BindingResult bindingResult) {
		
		shoppingCartService.changeQuantity(basketProduct, -1);
		return "redirect:/cart";
	}

	@GetMapping("/removeProduct/{page}/{id}")
	public String removeBasketProduct(@PathVariable String page, @PathVariable int id) {
		
		shoppingCartService.deleteBasketProductById(id);
		return "redirect:/" + page;
	}

}
