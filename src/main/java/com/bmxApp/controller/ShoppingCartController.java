package com.bmxApp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ShoppingCartController {

	private final ShoppingCartService shoppingCartService;
	private final DiscountService discountService;

	@GetMapping(path = { "/cart", "/cart/{shopName}" })
	public String showShoppingCart(@PathVariable(value = "shopName", required = false) String shopName,
			@Nullable @RequestParam(value = "discountValue") String discountValue, Model model,
			HttpServletRequest request) {

		List<BasketProductDTO> products = shoppingCartService.getBasketProducts(shopName);

		if (discountValue != null)
			model.addAttribute("basketProducts", discountService.getBasketProductsWithDiscount(products));
		else
			model.addAttribute("basketProducts", products);

		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("totalPrice", shoppingCartService.getTotalPriceForShop(shopName));
				//ProductFormatter.formatProductPrice(shoppingCartService.getTotalPriceForShop(shopName)));
		model.addAttribute("totalPriceForBasketProduct", shoppingCartService.getTotalPriceForEachBasketProduct(discountValue));
		model.addAttribute("totalDiscount", shoppingCartService.getTotalDiscount(shopName));
		model.addAttribute("discountValue", discountValue);
		model.addAttribute("finalPrice", shoppingCartService.getFinalPrice(shopName));
		
		System.out.println("CURRENT: " + PathCreator.getCurrentUrl(request));
		
		model.addAttribute("currentUrl", PathCreator.getCurrentUrl(request));

		return "basket";
	}

	@DeleteMapping({ "/deleteProducts", "/cart/deleteProducts" })
	public String deleteBasketProducts() {

		shoppingCartService.deleteBasketProducts();
		return "redirect:/cart";
	}

	@DeleteMapping("/removeDropdownCartProducts")
	public RedirectView removeDropdownProducts(@RequestParam("values") String[] values,
			@RequestParam String currentURL) {

		RedirectView redirectView = new RedirectView();
		shoppingCartService.deleteBasketProducts();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

	@PatchMapping("/changeQuantity")
	public RedirectView changeQuantity(@Nullable @RequestParam("quantityContainer") String quantityContainer,
			@Nullable @RequestParam("quantityValue") String quantityValue, @RequestParam("productId") int productId, @RequestParam("currentUrl") String currentUrl) {

		shoppingCartService.changeQuantity(productId, quantityContainer, quantityValue);
		return new RedirectView(currentUrl);
		//	return "redirect:/cart";
	}

	@DeleteMapping("/removeBasketProduct/{productId}")
	public RedirectView removeBasketProduct(@RequestParam String currentURL, @PathVariable int productId) {

		RedirectView redirectView = new RedirectView();
		shoppingCartService.deleteBasketProductByProductId(productId);
		redirectView.setUrl(currentURL);
		return redirectView;
	}

	@PostMapping("/addProduct")
	public RedirectView addProductToBasket(@ModelAttribute("product") ProductDTO dtoProduct,
			@RequestParam("currentUrl") String currentURL, Model model, BindingResult bindingResult) {

		shoppingCartService.addProductToCart(dtoProduct.getProductName(), dtoProduct.getShopName());
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

}
