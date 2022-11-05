package com.darkshadowft.smd_assignment3;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CartItemViewModel extends ViewModel {
		private ArrayList<Cart> cartItems;
		IProductDAO dao;

		public ArrayList<Cart> getCartItems(Bundle savedInstanceState, String key){
				if (cartItems == null){
						if (savedInstanceState == null) {
								if (dao != null){
										cartItems = Cart.load(dao);
								}
								else cartItems = new ArrayList<Cart>();
						}
						else{
								cartItems = (ArrayList<Cart>) savedInstanceState.get(key);
						}
				}
				return cartItems;
		}

		public void setDao(IProductDAO d){
				dao = d;
		}
		public ArrayList<Cart> update(){
				if (dao != null){
						cartItems = Cart.load(dao);
				}
				else cartItems = new ArrayList<Cart>();
				return cartItems;
		}
}
