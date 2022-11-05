package com.darkshadowft.smd_assignment3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public class Cart implements Serializable {
		private String id;
		private String description;
		private String name;
		private String price;
		private float rating;
		private int imgResourceId;
		private int quantity;
		private transient IProductDAO dao = null;

		public Cart() {
				this.id = UUID.randomUUID().toString();
				this.quantity = 1;
		}

		public Cart(String description, String name, String price, float rating) {
				this.id = UUID.randomUUID().toString();
				this.description = description;
				this.name = name;
				this.price = price;
				this.rating = rating;
				this.quantity = 1;
		}

		public void setId(String id) {
				this.id = id;
		}

		public String getId() {
				return id;
		}

		public void setDescription(String description) {
				this.description = description;
		}

		public String getDescription() {
				return description;
		}

		public void setName(String name) {
				this.name = name;
		}

		public String getName() {
				return name;
		}

		public void setPrice(String price) {
				this.price = price;
		}

		public String getPrice() {
				return price;
		}

		public void setRating(float rating) {
				this.rating = rating;
		}

		public float getRating() {
				return rating;
		}

		public void setImgResourceId(int imgResourceId) {
				this.imgResourceId = imgResourceId;
		}

		public int getImgResourceId() {
				return imgResourceId;
		}

		public void setQuantity(int quantity) {
				this.quantity = quantity;
		}

		public int getQuantity() {
				return quantity;
		}

		public IProductDAO getDao() {
				return dao;
		}

		public void setDao(IProductDAO dao) {
				this.dao = dao;
		}

		public void load(Hashtable<String, String> data) {
				id = data.get("id");
				description = data.get("description");
				name = data.get("name");
				price = data.get("price");
				rating = Float.parseFloat(data.get("rating"));
				imgResourceId = Integer.parseInt(data.get("imgresourceid"));
				quantity = Integer.parseInt(data.get("quantity"));
		}

		public void save() {
				if (dao != null) {
						Hashtable<String, String> data = new Hashtable<String, String>();

						data.put("id", id);
						data.put("name", name);
						data.put("description", description);
						data.put("price", price);
						data.put("rating", Float.toString(rating));
						data.put("imgresourceid", Integer.toString(imgResourceId));
						data.put("quantity", Integer.toString(quantity));

						dao.saveCartItem(data);
				}
		}

		public void delete() {
				if (dao != null) {
						dao.deleteCartItem(id);
				}
		}

		public Hashtable<String, String> getHashTable() {
				Hashtable<String, String> data = new Hashtable<String, String>();

				data.put("id", id);
				data.put("name", name);
				data.put("description", description);
				data.put("price", price);
				data.put("rating", Float.toString(rating));
				data.put("imgResourceId", Integer.toString(imgResourceId));
				data.put("quantity", Integer.toString(quantity));

				return data;
		}

		public static ArrayList<Cart> load(IProductDAO dao) {
				ArrayList<Cart> products = new ArrayList<Cart>();
				if (dao != null) {

						ArrayList<Hashtable<String, String>> objects = dao.loadCartItems();
						for (Hashtable<String, String> obj : objects) {
								Cart cartItem = new Cart();
								cartItem.setDao(dao);
								cartItem.load(obj);
								products.add(cartItem);
						}
				}
				return products;
		}
}
