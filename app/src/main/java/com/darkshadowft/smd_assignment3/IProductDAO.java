package com.darkshadowft.smd_assignment3;

import java.util.ArrayList;
import java.util.Hashtable;

public interface IProductDAO {
		public void saveProduct(Hashtable<String,String> attributes);
		public void saveProducts(ArrayList<Hashtable<String,String>> objects);

		public void saveCartItem(Hashtable<String,String> attributes);
		public void saveCartItems(ArrayList<Hashtable<String,String>> objects);

		public Hashtable<String,String> loadProduct(String id);
		public ArrayList<Hashtable<String,String>> loadProducts();

		public Hashtable<String,String> loadCartItem(String id);
		public ArrayList<Hashtable<String,String>> loadCartItems();

		public void deleteAllProducts();
		public void deleteAllCartItems();

		public void deleteProduct(String id);
		public void deleteCartItem(String id);
}
