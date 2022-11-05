package com.darkshadowft.smd_assignment3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ProductFirebaseDAO implements IProductDAO {
		public interface DataObserver{
				public void update();
		}

		private DataObserver observer;
		private static FirebaseDatabase database;
		DatabaseReference myRef;

		ArrayList<Hashtable<String,String>> data;

		public ProductFirebaseDAO(DataObserver obs){
				observer = obs;
				if (database != null)
						return;

				database = FirebaseDatabase.getInstance();
				//"true" tells firebase to store your uncommitted changes to the firebase DB on your disk
				database.setPersistenceEnabled(true);
				myRef = database.getReference("Products");

				myRef.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
								try {
										data = new ArrayList<Hashtable<String,String>>();
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
												data.add(obj);
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
		}

		@Override
		public void save(Hashtable<String, String> attributes) {
				if (attributes != null){
						myRef.child(attributes.get("id")).setValue(attributes).addOnCompleteListener(new OnCompleteListener<Void>() {
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
		public void save(ArrayList<Hashtable<String, String>> objects) {
				for(Hashtable<String,String> obj : objects){
						save(obj);
				}
		}

		@Override
		public ArrayList<Hashtable<String, String>> load() {
				if (data == null){
						data = new ArrayList<Hashtable<String,String>>();
				}
				return data;
		}

		@Override
		public Hashtable<String, String> load(String id) {
				return null;
		}

		@Override
		public void emptyTable() {
			myRef.removeValue();
		}

		@Override
		public void delete(String id) {

		}
}
