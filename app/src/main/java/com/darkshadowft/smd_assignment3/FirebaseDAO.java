package com.darkshadowft.smd_assignment3;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class FirebaseDAO implements IProductDAO {
		public interface DataObserver{
				public void update();
		}

		private DataObserver observer;
		private static FirebaseDatabase database;
		private static DatabaseReference productsRef;
		private static DatabaseReference cartRef;

		private static ArrayList<Hashtable<String,String>> productData;
		private static ArrayList<Hashtable<String,String>> cartData;

		public FirebaseDAO(DataObserver obs, String type){
				if (type.equals("product"))
						initializeProductDAO(obs);
				else
						initializeCartDAO(obs);
		}

		public FirebaseDatabase initializeProductDAO(DataObserver obs){
				observer = obs;
				if (database == null){
						database = FirebaseDatabase.getInstance();
						//"true" tells firebase to store your uncommitted changes to the firebase DB on your disk
						database.setPersistenceEnabled(true);
				}

				productsRef = database.getReference("Products");

				productsRef.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
								try {
										productData = new ArrayList<Hashtable<String,String>>();
//                    A DataSnapshot instance contains data from a Firebase Database location.
//                    Any time you read Database data, you receive the data as a DataSnapshot.
										System.out.println("Populating data from cloud.....");
										for (DataSnapshot d : dataSnapshot.getChildren()) {
												GenericTypeIndicator<HashMap<String,Object>> type = new GenericTypeIndicator<HashMap<String, Object>>() {};

//                        Cast the data to HashMap<String,Object> and store the values in map object
												HashMap<String,Object> map =  d.getValue(type);

												Hashtable<String,String> obj = new Hashtable<String,String>();

												//Store the row/object in obj Hashtable
												for(String key : map.keySet()){
														obj.put(key,map.get(key).toString());
												}
												productData.add(obj);
										}

										observer.update();
								}
								catch (Exception ex) {
										Log.e("firebasedb", ex.getMessage());
								}
						}

						@Override
						public void onCancelled(DatabaseError error) {
								Log.w("firebasedb", "Failed to read value.", error.toException());
						}
				});

				return database;
		}

		public FirebaseDatabase initializeCartDAO(DataObserver obs){
				observer = obs;
				if (database == null){
						database = FirebaseDatabase.getInstance();
						//"true" tells firebase to store your uncommitted changes to the firebase DB on your disk
						database.setPersistenceEnabled(true);
				}

				cartRef = database.getReference("Cart");

				cartRef.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
								try {
										cartData = new ArrayList<Hashtable<String,String>>();
//                    A DataSnapshot instance contains data from a Firebase Database location.
//                    Any time you read Database data, you receive the data as a DataSnapshot.
										System.out.println("Populating data from cloud.....");
										for (DataSnapshot d : dataSnapshot.getChildren()) {
												GenericTypeIndicator<HashMap<String,Object>> type = new GenericTypeIndicator<HashMap<String, Object>>() {};

//                        Cast the data to HashMap<String,Object> and store the values in map object
												HashMap<String,Object> map =  d.getValue(type);

												Hashtable<String,String> obj = new Hashtable<String,String>();

												//Store the row/object in obj Hashtable
												for(String key : map.keySet()){
														obj.put(key,map.get(key).toString());
												}
												cartData.add(obj);
										}

										observer.update();
								}
								catch (Exception ex) {
										Log.e("firebasedb", ex.getMessage());
								}
						}

						@Override
						public void onCancelled(DatabaseError error) {
								Log.w("firebasedb", "Failed to read value.", error.toException());
						}
				});
				return database;
		}

		@Override
		public void saveProduct(Hashtable<String, String> attributes) {
				if (attributes != null){
						productsRef.child(attributes.get("id")).setValue(attributes).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
										if (task.isSuccessful()){
												System.out.println("Successfully saved");
										}
										else
											System.out.println("Failed to save because of: " + task.getException().toString());
								}

						});
						System.out.println("Waiting for status of saving....");
				}
		}

		@Override
		public void saveProducts(ArrayList<Hashtable<String, String>> objects) {
				for(Hashtable<String,String> obj : objects){
						saveProduct(obj);
				}
		}

		@Override
		public void saveCartItem(Hashtable<String, String> attributes) {
				if (attributes != null){
						cartRef.child(attributes.get("id")).setValue(attributes).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
										if (task.isSuccessful()){
												System.out.println("Successfully saved");
										}
										else
												System.out.println("Failed to save because of: " + task.getException().toString());
								}

						});
						System.out.println("Waiting for status of saving....");
				}
		}

		@Override
		public void saveCartItems(ArrayList<Hashtable<String, String>> objects) {
				for(Hashtable<String,String> obj : objects){
						saveCartItem(obj);
				}
		}

		@Override
		public Hashtable<String, String> loadProduct(String id) {
				return null;
		}

		@Override
		public ArrayList<Hashtable<String, String>> loadProducts() {
				if (productData == null){
						productData = new ArrayList<Hashtable<String,String>>();
				}
				return productData;
		}

		@Override
		public Hashtable<String, String> loadCartItem(String id) {
				return null;
		}

		@Override
		public ArrayList<Hashtable<String, String>> loadCartItems() {
				if (cartData == null){
						cartData = new ArrayList<Hashtable<String,String>>();
				}
				return cartData;
		}

		@Override
		public void deleteProduct(String id) {
				productsRef.child(id).removeValue();
		}

		@Override
		public void deleteAllProducts() {
				productsRef.removeValue();
		}

		@Override
		public void deleteCartItem(String id) {
				cartRef.child(id).removeValue();
		}

		@Override
		public void deleteAllCartItems() {
				cartRef.removeValue();
		}
}
