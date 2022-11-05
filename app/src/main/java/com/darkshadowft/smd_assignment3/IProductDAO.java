package com.darkshadowft.smd_assignment3;

import java.util.ArrayList;
import java.util.Hashtable;

public interface IProductDAO {
		public void save(Hashtable<String,String> attributes);
		public void save(ArrayList<Hashtable<String,String>> objects);
		public ArrayList<Hashtable<String,String>> load();
		public Hashtable<String,String> load(String id);
		public void emptyTable();
		public void delete(String id);
}
