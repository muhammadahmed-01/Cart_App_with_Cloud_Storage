package com.darkshadowft.smd_assignment3;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductItemClickListener {
		private EditText search;
		private Filterable filterable;
		private ArrayList<Product> dataSet = new ArrayList<Product>();
		ProductAdapter adp;
		private ActivityResultLauncher<Intent> productDetailLauncher;
		private ActivityResultLauncher<Intent> addNewProductLauncher;
		private IProductDAO productDAO;
		private Menu optionsMenu;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.list);

				search = (EditText) findViewById(R.id.search);
				search.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
								filterable.getFilter().filter(search.getText().toString());
						}

						@Override
						public void afterTextChanged(Editable editable) {

						}
				});

				RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
				recyclerView.setHasFixedSize(true);

				// use a linear layout manager
				RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
				recyclerView.setLayoutManager(layoutManager);

				productDAO = new ProductFirebaseDAO(new ProductFirebaseDAO.DataObserver() {
						@Override
						public void update() {
								refresh();
						}
				});

				ProductViewModel productViewModel = new ViewModelProvider(MainActivity.this).get(ProductViewModel.class);
				productViewModel.setDao(productDAO);
				dataSet = productViewModel.getProducts(savedInstanceState, "products");

				// specify an adapter (see also next example)
				adp = new ProductAdapter(dataSet,this);
				filterable = adp;
				recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
				recyclerView.setAdapter(adp);

//				Product p1 = new Product("This is a book", "Book", "$150", 4);
//				p1.setImgResourceId(R.drawable.book);
//				dataSet.add(p1);
//				adp.notifyDataSetChanged();
//
//				Product p2 = new Product("This is a pencil", "Pencil", "$15", 5);
//				p2.setImgResourceId(R.drawable.pencil);
//				dataSet.add(p2);
//				adp.notifyDataSetChanged();

				Button newProductBtn = (Button) findViewById(R.id.new_product);
				newProductBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								addNewProduct();
						}
				});

				//register
				productDetailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
								new ActivityResultCallback<ActivityResult>() {
										@Override
										public void onActivityResult(ActivityResult result) {
												if (result.getResultCode() == RESULT_OK){
														System.out.println("Cart data successfully saved");
												}
										}
								}
				);

				addNewProductLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
								new ActivityResultCallback<ActivityResult>() {
										@Override
										public void onActivityResult(ActivityResult result) {
												if (result.getResultCode() == RESULT_OK){
														Intent intent = result.getData();
														if (intent != null){
																Product product = (Product) intent.getSerializableExtra("product");
																product.setDao(productDAO);
																product.save();
																dataSet.add(product);
																adp.notifyDataSetChanged();
														}
												}
										}
								});
		}

		// action mode
		private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {

						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.main_action, menu);
						return true;
				}

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
				}

				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						switch (item.getItemId()) {
								case R.id.delete:

										adp.removeSelectedItems();
										mode.finish();
										productDAO.emptyTable();
										for (int i = 0; i < dataSet.size(); i++){
												dataSet.get(i).save();
										}
										return true;
								default:
										return false;
						}
				}

				public void onDestroyActionMode(ActionMode mode) {
						adp.setMode(ProductAdapter.DEFAULT_MODE);
				}
		};

		@Override
		public void onClick(Product product) {
				Intent intent = new Intent(this, ProductDetailActivity.class);
				intent.putExtra("id",product.getId());
				intent.putExtra("resId",product.getImgResourceId());
				intent.putExtra("name",product.getName());
				intent.putExtra("rating",product.getRating());
				intent.putExtra("price", product.getPrice());
				intent.putExtra("description",product.getDescription());
				productDetailLauncher.launch(intent);
		}

		public void onLongClick(Product product) {
				this.startActionMode(actionModeCallback);
				adp.setMode(ProductAdapter.SELECTABLE_MODE);
		}

		public void addNewProduct(){
				Intent intent = new Intent(this, AddProductActivity.class);
				addNewProductLauncher.launch(intent);
		}

		public void onSaveInstanceState(Bundle state) {
				super.onSaveInstanceState(state);
				state.putSerializable("products",dataSet);
		}

		public boolean onCreateOptionsMenu(Menu menu) {
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.main,menu);
				optionsMenu = menu;
				return super.onCreateOptionsMenu(menu);
		}

		public boolean onOptionsItemSelected(MenuItem item) {
				return super.onOptionsItemSelected(item);
		}

		public void refresh(){
				ProductViewModel vm = new ViewModelProvider(this).get(ProductViewModel.class);
				dataSet = vm.update();
				if (dataSet != null){
						adp.updateData(dataSet);
				}
		}
}