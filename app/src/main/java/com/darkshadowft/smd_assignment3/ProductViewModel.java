package com.darkshadowft.smd_assignment3;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ProductViewModel extends ViewModel {
		private ArrayList<Product> products;
		IProductDAO dao;

		public ArrayList<Product> getProducts(Bundle savedInstanceState, String key){
				if (products == null){
						if (savedInstanceState == null) {
								if (dao != null){
										products = Product.load(dao);
								}
								else products = new ArrayList<Product>();
						}
						else{
								products = (ArrayList<Product>) savedInstanceState.get(key);
						}
				}
				return products;
		}

		public void setDao(IProductDAO d){
				dao = d;
		}
		public ArrayList<Product> update(){
				if (dao != null){
						products = Product.load(dao);
				}
				else products = new ArrayList<Product>();
				return products;
		}
}