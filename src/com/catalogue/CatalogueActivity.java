package com.catalogue;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.catalogue.adapters.CatalogueGridAdapter;
import com.catalogue.adapters.ModelsListAdapter;
import com.catalogue.entities.CatalogueModel;
import com.catalogue.events.OnCatalogueFragmentReadyListener;
import com.catalogue.fragments.BacketFragment;
import com.catalogue.fragments.CatalogueFragment;
import com.catalogue.fragments.DetailedViewFragment;
import com.catalogue.fragments.FavouriteFragment;
import com.catalogue.fragments.FilterFragment;
import com.catalogue.fragments.StartContentFragment;

public class CatalogueActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnTouchListener, OnCatalogueFragmentReadyListener,
		OnChildClickListener {
	/** Called when the activity is first created. */
	private final static int MODELS_ON_SCREEN = 9;
	private final static int GRID_NUM_COLUMNS = 3;
	private final static int MOVE_DENSITY = 50;

	private ArrayList<CatalogueModel> total_models_set;
	private ArrayList<CatalogueModel> current_models_set;
	private ArrayList<CatalogueModel> favourite_model_set;
	private ArrayList<CatalogueModel> backet_model_set;
	private ArrayList<CatalogueModel> work_model_set;
	private ModelFilter filter;

	private ExpandableListView models_list;
	private Button button_show_all_models;
	private ViewFlipper flipper;
	private double startX;
	private int current_catalogue_page;
	private int total_pages;
	private CatalogueModel current_model;

	private ImageButton button_show_backet;
	private ImageButton button_show_favourites;
	private ImageButton button_exit;
	private ImageButton button_filter;
	private ImageButton button_filter_on;
	private ImageButton button_catalogue;

	private ImageButton add_to_backet;
	private ImageButton add_to_favourite;
	private StartContentFragment start_fragment;
	private EditText f_error;

	private boolean is_filter_on;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Display display = getWindowManager().getDefaultDisplay();
		int rotation = display.getRotation();
		if (rotation != Surface.ROTATION_0 && rotation != Surface.ROTATION_180) {
			Toast.makeText(
					getApplicationContext(),
					"К сожалению, данное приложение не поддердживает работу в портретном режиме :(",
					Toast.LENGTH_LONG).show();
		} else {

			setContentView(R.layout.main);
			initializeModels();
			initializeWidgets();

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			start_fragment = new StartContentFragment();
			ft.add(R.id.fragment_container, start_fragment);
			ft.addToBackStack(null);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int sender = arg0.getId();
		switch (sender) {
		case (R.id.button_show_all_models):
			work_model_set = (ArrayList<CatalogueModel>) total_models_set
					.clone();
			load_all_models();
			break;
		case (R.id.button_add_backet):
			if (current_model != null) {
				backet_model_set.add(current_model);
				add_to_backet.setImageResource(R.drawable.basket_act);
				Toast.makeText(
						getApplicationContext(),
						current_model.getDescription() + " из коллекции "
								+ current_model.getSystem_name()
								+ " добавлен в корзину!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case (R.id.button_add_favourite):
			if (current_model != null) {
				favourite_model_set.add(current_model);
				add_to_favourite.setImageResource(R.drawable.fav_act_icon);
				Toast.makeText(
						getApplicationContext(),
						current_model.getDescription() + " из коллекции "
								+ current_model.getSystem_name()
								+ " добавлен в избранное!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case (R.id.button_exit):
			finish();
			break;
		case (R.id.button_backet):
			if (backet_model_set.size() != 0) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				BacketFragment bf = new BacketFragment();
				bf.setOnCatalogueFragmentListener(this);
				ft.replace(R.id.fragment_container, bf);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.commit();
			} else {
				Toast.makeText(getApplicationContext(), "Ваша корзина пуста!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case (R.id.button_favourites):
			if (favourite_model_set.size() != 0) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				FavouriteFragment ff = new FavouriteFragment();
				ff.setOnCatalogueFragmentReadyListener(this);
				ft.replace(R.id.fragment_container, ff);
				ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			} else {
				Toast.makeText(getApplicationContext(),
						"Нет моделей в категории \"Избранное\"!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case (R.id.button_filter):
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			FilterFragment fif = new FilterFragment();
			fif.setOnCatalogueFragmentReadyListener(this);
			ft.replace(R.id.fragment_container, fif, "FILTER_FRAGMENT");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.commit();
			button_filter.setImageResource(R.drawable.filter_off_act);
			break;
		case (R.id.button_apply_filter):
			setNewFilter();
			FragmentTransaction ft1 = getFragmentManager().beginTransaction();
			FilterFragment fif1 = (FilterFragment) getFragmentManager()
					.findFragmentByTag("FILTER_FRAGMENT");
			ft1.remove(fif1);
			ft1.commit();
			button_filter.setImageResource(R.drawable.filter_off_pass);
			button_filter_on.setImageResource(R.drawable.filter_on_act);
			is_filter_on = true;
			work_model_set = (ArrayList<CatalogueModel>) total_models_set
					.clone();
			load_all_models();
			break;
		case (R.id.button_skip_filter):
			filter.skipFilter();
			FragmentTransaction ft2 = getFragmentManager().beginTransaction();
			FilterFragment fif2 = (FilterFragment) getFragmentManager()
					.findFragmentByTag("FILTER_FRAGMENT");
			ft2.remove(fif2);
			ft2.commit();
			button_filter.setImageResource(R.drawable.filter_off_pass);
			if (is_filter_on == true) {
				is_filter_on = false;
				button_filter_on.setImageResource(R.drawable.filter_on_pass);
			}
			work_model_set = (ArrayList<CatalogueModel>) total_models_set
					.clone();
			load_all_models();
			break;
		case (R.id.button_filter_on):
			if (is_filter_on == true) {
				is_filter_on = false;
				button_filter_on.setImageResource(R.drawable.filter_on_pass);
				work_model_set = (ArrayList<CatalogueModel>) total_models_set
						.clone();
				load_all_models();
			} else {
				is_filter_on = true;
				button_filter_on.setImageResource(R.drawable.filter_on_act);
				work_model_set = (ArrayList<CatalogueModel>) total_models_set
						.clone();
				load_all_models();
			}
			break;
		default:
			break;
		}

	}

	public void setNewFilter() {
		FilterFragment fif = (FilterFragment) getFragmentManager()
				.findFragmentByTag("FILTER_FRAGMENT");
		EditText f_desc = (EditText) fif.getView().findViewById(
				R.id.filter_type);
		EditText f_price_start = (EditText) fif.getView().findViewById(
				R.id.filter_price_from);
		EditText f_price_end = (EditText) fif.getView().findViewById(
				R.id.filter_price_to);
		EditText f_height = (EditText) fif.getView().findViewById(
				R.id.filter_height);
		EditText f_width = (EditText) fif.getView().findViewById(
				R.id.filter_width);
		EditText f_length = (EditText) fif.getView().findViewById(
				R.id.filter_length);
		EditText f_error = (EditText) fif.getView().findViewById(
				R.id.filter_error);

		if (!f_desc.getText().toString().isEmpty()) {
			filter.setDescription(f_desc.getText().toString());
		} else {
			filter.setDescription(null);
		}
		if (!f_price_start.getText().toString().isEmpty()) {
			filter.setPrice_start(Integer.parseInt(f_price_start.getText()
					.toString()));
		} else {
			filter.setPrice_start(null);
		}
		if (!f_price_end.getText().toString().isEmpty()) {
			filter.setPrice_end(Integer.parseInt(f_price_end.getText()
					.toString()));
		} else {
			filter.setPrice_end(null);
		}
		if (!f_height.getText().toString().isEmpty()) {
			filter.setHeight(Integer.parseInt(f_height.getText().toString()));
		} else {
			filter.setHeight(null);
		}
		if (!f_width.getText().toString().isEmpty()) {
			filter.setWidth(Integer.parseInt(f_width.getText().toString()));
		} else {
			filter.setWidth(null);
		}
		if (!f_length.getText().toString().isEmpty()) {
			filter.setLength(Integer.parseInt(f_length.getText().toString()));
		} else {
			filter.setLength(null);
		}
		if (!f_error.getText().toString().isEmpty()) {
			filter.setError(Integer.parseInt(f_error.getText().toString()));
		} else {
			filter.setError(null);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			return false;
		case MotionEvent.ACTION_UP:
			float currentX = event.getX();
			if (this.startX - MOVE_DENSITY > currentX) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				if (current_catalogue_page < total_pages - 1) {
					current_catalogue_page++;
					flipper.showNext();
				} else {
					Toast.makeText(getApplicationContext(),
							"Это последняя страница!", Toast.LENGTH_SHORT)
							.show();
				}

				return true;
			}
			if (this.startX + MOVE_DENSITY < currentX) {
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				if (current_catalogue_page > 0) {
					current_catalogue_page--;
					flipper.showPrevious();
				} else {
					Toast.makeText(getApplicationContext(),
							"Это первая страница!", Toast.LENGTH_SHORT).show();
				}

				return true;
			}
			if (Math.abs(this.startX - currentX) < 10) {
				return false;
			}
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		if (groupPosition == 0) {
			work_model_set.clear();
			switch (childPosition) {
			case (0):
				work_model_set.add(total_models_set.get(0));
				work_model_set.add(total_models_set.get(1));
				work_model_set.add(total_models_set.get(2));
				work_model_set.add(total_models_set.get(3));
				work_model_set.add(total_models_set.get(4));
				load_all_models();
				break;
			case (1):
				work_model_set.add(total_models_set.get(5));
				work_model_set.add(total_models_set.get(6));
				work_model_set.add(total_models_set.get(7));
				work_model_set.add(total_models_set.get(8));
				work_model_set.add(total_models_set.get(9));
				load_all_models();
				break;
			case (2):
				work_model_set.add(total_models_set.get(10));
				work_model_set.add(total_models_set.get(11));
				work_model_set.add(total_models_set.get(12));
				work_model_set.add(total_models_set.get(13));
				work_model_set.add(total_models_set.get(14));
				load_all_models();
				break;
			case (3):
				work_model_set.add(total_models_set.get(15));
				work_model_set.add(total_models_set.get(16));
				work_model_set.add(total_models_set.get(17));
				work_model_set.add(total_models_set.get(18));
				work_model_set.add(total_models_set.get(19));
				load_all_models();
				break;
			case (4):
				work_model_set.add(total_models_set.get(20));
				work_model_set.add(total_models_set.get(21));
				work_model_set.add(total_models_set.get(22));
				work_model_set.add(total_models_set.get(23));
				work_model_set.add(total_models_set.get(24));
				load_all_models();
				break;
			case (5):
				work_model_set.add(total_models_set.get(25));
				work_model_set.add(total_models_set.get(26));
				work_model_set.add(total_models_set.get(27));
				work_model_set.add(total_models_set.get(28));
				work_model_set.add(total_models_set.get(29));
				work_model_set.add(total_models_set.get(30));
				load_all_models();
				break;
			default:
				break;
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"К сожалению, данная группа товаров пока не заполнена!",
					Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		int selected_item = current_catalogue_page * MODELS_ON_SCREEN + arg2;
		current_model = current_models_set.get(selected_item);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DetailedViewFragment dvf = new DetailedViewFragment();
		dvf.setOnCatalogueFragmentReadyListener(this);
		ft.replace(R.id.fragment_container, dvf);
		ft.addToBackStack("CAT");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	public void onFilterFragmentReady(FilterFragment fif) {
		// TODO Auto-generated method stub
		EditText f_desc = (EditText) fif.getView().findViewById(
				R.id.filter_type);
		EditText f_price_start = (EditText) fif.getView().findViewById(
				R.id.filter_price_from);
		EditText f_price_end = (EditText) fif.getView().findViewById(
				R.id.filter_price_to);
		EditText f_height = (EditText) fif.getView().findViewById(
				R.id.filter_height);
		EditText f_width = (EditText) fif.getView().findViewById(
				R.id.filter_width);
		EditText f_length = (EditText) fif.getView().findViewById(
				R.id.filter_length);
		f_error = (EditText) fif.getView().findViewById(R.id.filter_error);
		f_error.setEnabled(false);

		if (filter.getDescription() != null) {
			f_desc.setText(filter.getDescription());
		} else {
			f_desc.setText("");
		}

		if (filter.getPrice_start() != null) {
			f_price_start.setText(filter.getPrice_start().toString());
		} else {
			f_price_start.setText("");
		}

		if (filter.getPrice_end() != null) {
			f_price_end.setText(filter.getPrice_end().toString());
		} else {
			f_price_end.setText("");
		}

		if (filter.getHeight() != null) {
			f_height.setText(filter.getHeight().toString());
		} else {
			f_height.setText("");
		}

		if (filter.getWidth() != null) {
			f_width.setText(filter.getWidth().toString());
		} else {
			f_width.setText("");
		}

		if (filter.getLength() != null) {
			f_length.setText(filter.getLength().toString());
		} else {
			f_length.setText("");
		}

		if (filter.getError() != null) {
			f_error.setText(filter.getError().toString());
		} else {
			f_error.setText("");
		}

		Button button_apply_filter = (Button) fif.getView().findViewById(
				R.id.button_apply_filter);
		button_apply_filter.setOnClickListener(this);
		Button button_skip_filter = (Button) fif.getView().findViewById(
				R.id.button_skip_filter);
		button_skip_filter.setOnClickListener(this);

		Button button_reduce_error = (Button) fif.getView().findViewById(
				R.id.button_reduce_error);
		Button button_increase_error = (Button) fif.getView().findViewById(
				R.id.button_increase_error);

		// уменьшение погрешности
		button_reduce_error.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!f_error.getText().toString().isEmpty()) {
					int error = Integer.parseInt(f_error.getText().toString());
					if (error > 0) {
						error -= 10;
					}
					f_error.setText(Integer.valueOf(error).toString());
				} else {
					Toast.makeText(getApplicationContext(),
							"Невозможно уменьшить пустое значение!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// увеличение погрешности
		button_increase_error.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!f_error.getText().toString().isEmpty()) {
					int error = Integer.parseInt(f_error.getText().toString());
					if (error < 100) {
						error += 10;
					}
					f_error.setText(Integer.valueOf(error).toString());
				} else {
					f_error.setText(Integer.valueOf(10).toString());
				}

			}
		});

	}

	@Override
	public void onFavouriteFragmentReady(FavouriteFragment ff) {
		// TODO Auto-generated method stub
		GridView fav_grid = (GridView) ff.getView().findViewById(
				R.id.grid_favourite);
		fav_grid.setAdapter(new CatalogueGridAdapter(favourite_model_set));

	}

	// событие происходит, когда готов фрагмент корзины
	@Override
	public void onBacketFragmentReady(BacketFragment bf) {
		// TODO Auto-generated method stub
		LinearLayout table = (LinearLayout) bf.getView().findViewById(
				R.id.backet_table);
		Double total_sum = 0.0;
		for (int i = 0; i < backet_model_set.size(); i++) {
			CatalogueModel cm = backet_model_set.get(i);
			LinearLayout tr = new LinearLayout(this);

			TextView number = new TextView(this);
			Integer number_by_order = Integer.valueOf(i + 1);
			number.setText(number_by_order.toString());
			number.setTextColor(Color.BLACK);
			number.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			number.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));

			ImageView iv = new ImageView(this);
			iv.setImageResource(cm.getLow_pic_id());
			iv.setLayoutParams(new LinearLayout.LayoutParams(130,
					LayoutParams.WRAP_CONTENT, 1f));

			TextView desc = new TextView(this);
			desc.setText(cm.getSystem_name() + "\n" + cm.getDescription());
			desc.setTextColor(Color.BLACK);
			desc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			desc.setLayoutParams(new LinearLayout.LayoutParams(150,
					LayoutParams.WRAP_CONTENT, 1f));

			Double price = cm.getPrice();

			TextView price_text = new TextView(this);
			price_text.setText(price.toString());
			price_text.setTextColor(Color.BLACK);
			price_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			price_text.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));

			Double discount = price * 0.05;
			String discount_string = String.format("%.2f",
					discount.doubleValue());

			TextView discount_text = new TextView(this);
			discount_text.setText(discount_string);
			discount_text.setTextColor(Color.RED);
			discount_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			discount_text.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));

			TextView remove = new TextView(this);
			remove.setText("Удалить");
			remove.setTextColor(Color.BLACK);
			remove.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			remove.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));

			Double cost = price - discount;
			total_sum += cost;
			String cost_string = String.format("%.2f", cost.doubleValue());

			TextView cost_text = new TextView(this);
			cost_text.setText(cost_string);
			cost_text.setTextColor(Color.BLACK);
			cost_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			cost_text.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
			tr.addView(number);
			tr.addView(iv);
			tr.addView(desc);
			tr.addView(price_text);
			tr.addView(discount_text);
			tr.addView(remove);
			tr.addView(cost_text);

			table.addView(tr);

		}

	}

	// событие происходит, когда фрагмент детального просмотра готов к
	// отображению
	@Override
	public void onDetailedFragmentReady(DetailedViewFragment dvf) {
		// TODO Auto-generated method stub
		TextView model_name = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_name);
		TextView model_collection = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_collection);
		TextView model_articul = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_articul);
		TextView model_description = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_description);
		TextView model_height = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_height);
		TextView model_width = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_width);
		TextView model_length = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_length);
		TextView model_color = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_color);
		TextView model_price = (TextView) dvf.getView().findViewById(
				R.id.detailed_model_price);

		ImageView model_image = (ImageView) dvf.getView().findViewById(
				R.id.detailed_model_icon);

		add_to_backet = (ImageButton) dvf.getView().findViewById(
				R.id.button_add_backet);
		add_to_favourite = (ImageButton) dvf.getView().findViewById(
				R.id.button_add_favourite);
		add_to_backet.setOnClickListener(this);
		add_to_favourite.setOnClickListener(this);

		model_name.setText(current_model.getSystem_name() + " "
				+ current_model.getDescription());
		model_collection.setText(current_model.getSystem_name());
		model_articul.setText(current_model.getArticul());
		model_description.setText(current_model.getDescription());
		model_height.setText(current_model.getHeight().toString());
		model_width.setText(current_model.getWidth().toString());
		model_length.setText(current_model.getDeep().toString());
		model_color.setText(current_model.getColor());
		model_price.setText(current_model.getPrice().toString());

		model_image.setImageResource(current_model.getFull_pic_id());

	}

	// событие происходит, когда фрагмент каталога (сетка) готов к отображению
	@Override
	public void onCatalogueFragmentReady(CatalogueFragment cf) {
		// TODO Auto-generated method stub
		current_catalogue_page = 0;
		current_models_set.clear();

		if (is_filter_on == true) {
			int total = work_model_set.size();
			for (int i = 0; i < total; i++) {
				CatalogueModel cm = work_model_set.get(i);
				if (filter.checkModel(cm) == true) {
					current_models_set.add(cm);
				}
			}
		} else {
			current_models_set = (ArrayList<CatalogueModel>) work_model_set
					.clone();
		}

		int models_to_show = current_models_set.size();
		total_pages = (int) Math.floor(models_to_show / MODELS_ON_SCREEN) + 1;

		flipper = (ViewFlipper) cf.getView().findViewById(R.id.grid_flipper);
		if (flipper == null) {
			Toast.makeText(getApplicationContext(), "Flipper not found!",
					Toast.LENGTH_SHORT).show();
		} else {
			flipper.removeAllViewsInLayout();

			for (int i = 0; i < total_pages; i++) {
				GridView gv = new GridView(this);
				gv.setNumColumns(GRID_NUM_COLUMNS);
				gv.setHorizontalSpacing(5);
				gv.setVerticalSpacing(5);
				gv.setSelector(new ColorDrawable(Color.WHITE));

				gv.setOnItemClickListener(this);
				gv.setOnTouchListener(this);

				int start_i = i * MODELS_ON_SCREEN;
				int end_i = start_i + MODELS_ON_SCREEN;
				if (end_i > current_models_set.size()) {
					end_i = current_models_set.size();
				}
				ArrayList<CatalogueModel> cm = new ArrayList<CatalogueModel>();
				for (int j = start_i; j < end_i; j++) {
					cm.add(current_models_set.get(j));
				}
				gv.setAdapter(new CatalogueGridAdapter(cm));
				flipper.addView(gv);
			}
		}

	}

	public void load_all_models() {

		FragmentManager fragment_manager = getFragmentManager();
		FragmentTransaction fragment_transaction = fragment_manager
				.beginTransaction();
		CatalogueFragment cf = new CatalogueFragment();
		cf.setOnCatalogueFragmentReadyListener(this);

		fragment_transaction.replace(R.id.fragment_container, cf,
				"CATALOGUE_FRAGMENT");
		fragment_transaction.addToBackStack(null);
		fragment_transaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragment_transaction.commit();

	}

	private void initializeWidgets() {
		button_show_all_models = (Button) findViewById(R.id.button_show_all_models);
		button_show_all_models.setOnClickListener(this);
		models_list = (ExpandableListView) findViewById(R.id.catalogue_list);
		models_list.setAdapter(new ModelsListAdapter());
		models_list.setOnChildClickListener(this);
		button_show_backet = (ImageButton) findViewById(R.id.button_backet);
		button_show_backet.setOnClickListener(this);
		button_show_favourites = (ImageButton) findViewById(R.id.button_favourites);
		button_show_favourites.setOnClickListener(this);
		button_exit = (ImageButton) findViewById(R.id.button_exit);
		button_exit.setOnClickListener(this);
		button_filter = (ImageButton) findViewById(R.id.button_filter);
		button_filter.setOnClickListener(this);
		button_filter_on = (ImageButton) findViewById(R.id.button_filter_on);
		button_filter_on.setOnClickListener(this);
		button_catalogue = (ImageButton) findViewById(R.id.button_catalogue);
		button_catalogue.setOnClickListener(this);
	}

	// function loads a models data to Array
	private void initializeModels() {
		current_models_set = new ArrayList<CatalogueModel>();
		total_models_set = new ArrayList<CatalogueModel>();
		favourite_model_set = new ArrayList<CatalogueModel>();
		backet_model_set = new ArrayList<CatalogueModel>();
		work_model_set = new ArrayList<CatalogueModel>();
		filter = new ModelFilter();
		current_model = null;
		is_filter_on = false;

		total_models_set
				.add(new CatalogueModel("AUGUST", "AAA", "Шкаф", 199.5, 100.0,
						58.5, "венге", 3499, R.drawable.august_szf2d2s,
						R.drawable.august_szf2d2s_middle,
						R.drawable.august_szf2d2s_low));
		total_models_set.add(new CatalogueModel("AUGUST", "DDD",
				"Шкафчик под TV", 37.5, 148.5, 54.5, "венге", 2239,
				R.drawable.august_rtv2s, R.drawable.august_rtv2s_middle,
				R.drawable.august_rtv2s_low));
		total_models_set.add(new CatalogueModel("AUGUST", "BBB",
				"Шкафчик прикроватный", 37.5, 58.0, 35.5, "венге", 1899,
				R.drawable.august_com1s, R.drawable.august_com1s_middle,
				R.drawable.august_com1s_low));
		total_models_set
				.add(new CatalogueModel("AUGUST", "CCC", "Комод", 99.0, 148.5,
						45.5, "венге", 2219, R.drawable.august_kom2d4s,
						R.drawable.august_kom2d4s_middle,
						R.drawable.august_kom2d4s_low));
		total_models_set
				.add(new CatalogueModel("AUGUST", "EEE", "Стелаж", 199.5,
						100.0, 45.5, "венге", 2429, R.drawable.august_reg_100,
						R.drawable.august_reg_100_middle,
						R.drawable.august_reg_100_low));

		total_models_set.add(new CatalogueModel("BAWARIA", "JJJ",
				"Шкафчик прикроватный", 57.5, 50.0, 38.0, "грецкий орех", 1789,
				R.drawable.bawaria_dkom1s, R.drawable.bawaria_dkom1s_middle,
				R.drawable.bawaria_dkom1s_low));
		total_models_set
				.add(new CatalogueModel("BAWARIA", "SSS", "Стул", 93.0, 46.0,
						57.0, "грецкий орех", 1089, R.drawable.bawaria_dkrsii,
						R.drawable.bawaria_dkrsii_middle,
						R.drawable.bawaria_dkrsii_low));
		total_models_set.add(new CatalogueModel("BAWARIA", "FFF", "Кровать",
				60.0, 169.0, 210.0, "грецкий орех", 7279,
				R.drawable.bawaria_dloz160_200,
				R.drawable.bawaria_dloz160_200_middle,
				R.drawable.bawaria_dloz160_200_low));
		total_models_set.add(new CatalogueModel("BAWARIA", "KKK",
				"Шкафчик под TV", 61.0, 99.5, 56.5, "грецкий орех", 3299,
				R.drawable.bawaria_drtv100, R.drawable.bawaria_drtv100_middle,
				R.drawable.bawaria_drtv100_low));
		total_models_set.add(new CatalogueModel("BAWARIA", "LLL", "Витрина",
				200.0, 156.5, 44.0, "грецкий орех", 12809,
				R.drawable.bawaria_dwit3d2s,
				R.drawable.bawaria_dwit3d2s_middle,
				R.drawable.bawaria_dwit3d2s_low));

		total_models_set.add(new CatalogueModel("INDIANA", "III",
				"Письменный стол", 78.0, 65.0, 140.0, "дуб sutter", 2339,
				R.drawable.indiana_jbiu2d2s,
				R.drawable.indiana_jbiu2d2s_middle,
				R.drawable.indiana_jbiu2d2s_low));
		total_models_set.add(new CatalogueModel("INDIANA", "III", "Кровать",
				60.5, 154.0, 202.0, "дуб sutter", 2389,
				R.drawable.indiana_jloz80_160,
				R.drawable.indiana_jloz80_160_middle,
				R.drawable.indiana_jloz80_160_low));
		total_models_set
				.add(new CatalogueModel("INDIANA", "III", "Зеркало", 80.0,
						80.0, 2.5, "дуб sutter", 359,
						R.drawable.indiana_jlus80,
						R.drawable.indiana_jlus80_middle,
						R.drawable.indiana_jlus80_low));
		total_models_set.add(new CatalogueModel("INDIANA", "III", "Стелаж",
				195.5, 80.0, 40.0, "дуб sutter", 1059,
				R.drawable.indiana_jreg2do, R.drawable.indiana_jreg2do_middle,
				R.drawable.indiana_jreg2do_low));
		total_models_set.add(new CatalogueModel("INDIANA", "III", "Шкаф",
				195.5, 150.0, 57.0, "дуб sutter", 2579,
				R.drawable.indiana_jszf3d2s,
				R.drawable.indiana_jszf3d2s_middle,
				R.drawable.indiana_jszf3d2s_low));

		total_models_set.add(new CatalogueModel("LARGO CLASSIC", "LCA",
				"Столик журнальный", 55.0, 70.0, 110.0, "итальянская вишня",
				1849, R.drawable.largo_classic_law,
				R.drawable.largo_classic_law_middle,
				R.drawable.largo_classic_law_low));
		total_models_set.add(new CatalogueModel("LARGO CLASSIC", "LCA",
				"Буфет", 114.0, 156.5, 38.0, "итальянская вишня", 4259,
				R.drawable.largo_classic_reg2d1k3s,
				R.drawable.largo_classic_reg2d1k3s_middle,
				R.drawable.largo_classic_reg2d1k3s_low));
		total_models_set.add(new CatalogueModel("LARGO CLASSIC", "LCA",
				"Витрина", 96.0, 106.5, 38, "итальянская вишня", 1649,
				R.drawable.largo_classic_reg2w,
				R.drawable.largo_classic_reg2w_middle,
				R.drawable.largo_classic_reg2w_low));
		total_models_set.add(new CatalogueModel("LARGO CLASSIC", "LCA",
				"Шкафчик под TV", 42.0, 151.1, 56.5, "итальянская вишня", 1819,
				R.drawable.largo_classic_rtv2s_4_15,
				R.drawable.largo_classic_rtv2s_4_15_middle,
				R.drawable.largo_classic_rtv2s_4_15_low));
		total_models_set.add(new CatalogueModel("LARGO CLASSIC", "LCA",
				"Витрина подвесная", 40.0, 106.5, 30.0, "итальянская вишня",
				779, R.drawable.largo_classic_sw1w_4_11,
				R.drawable.largo_classic_sw1w_4_11_middle,
				R.drawable.largo_classic_sw1w_4_11_low));

		total_models_set.add(new CatalogueModel("ORLAND", "OCA",
				"Стол письменный", 78.0, 180.0, 80.0, "черешня orland", 10169,
				R.drawable.orland_biu_180, R.drawable.orland_biu_180_middle,
				R.drawable.orland_biu_180_low));
		total_models_set.add(new CatalogueModel("ORLAND", "OCA", "Комод", 90.5,
				140.5, 51.0, "черешня orland", 8019,
				R.drawable.orland_kom8s_140,
				R.drawable.orland_kom8s_140_middle,
				R.drawable.orland_kom8s_140_low));
		total_models_set.add(new CatalogueModel("ORLAND", "OCA", "Стелаж",
				213.5, 66.0, 42.0, "черешня orland", 5859,
				R.drawable.orland_reg1s_60, R.drawable.orland_reg1s_60_middle,
				R.drawable.orland_reg1s_60_low));
		total_models_set.add(new CatalogueModel("ORLAND", "OCA", "Стелаж",
				213.5, 96.0, 42.0, "черешня orland", 6839,
				R.drawable.orland_reg1s_90, R.drawable.orland_reg1s_90_middle,
				R.drawable.orland_reg1s_90_low));
		total_models_set.add(new CatalogueModel("ORLAND", "OCA", "Кресло",
				94.0, 43.0, 52.0, "черешня orland", 1179,
				R.drawable.orland_tx044, R.drawable.orland_tx044_middle,
				R.drawable.orland_tx044_low));

		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA", "Вешалка",
				199.5, 54.0, 37.0, "черешня/ваниль", 1199,
				R.drawable.vis_a_vis_vkp_20_5,
				R.drawable.vis_a_vis_vkp_20_5_middle,
				R.drawable.vis_a_vis_vkp_20_5_low));
		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA", "Кровать",
				60.5, 166.0, 206.5, "черешня/ваниль", 1699,
				R.drawable.vis_a_vis_vloz_160,
				R.drawable.vis_a_vis_vloz_160_middle,
				R.drawable.vis_a_vis_vloz_160_low));
		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA",
				"Шкафчик под TV", 48.5, 160.0, 55.0, "черешня/ваниль", 649,
				R.drawable.vis_a_vis_vrtv_5_16p,
				R.drawable.vis_a_vis_vrtv_5_16p_middle,
				R.drawable.vis_a_vis_vrtv_5_16p_low));
		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA",
				"Шкафчик прикроватный", 40.5, 44.0, 36.0, "черешня/ваниль",
				469, R.drawable.vis_a_vis_v2s_4_5l,
				R.drawable.vis_a_vis_v2s_4_5l_middle,
				R.drawable.vis_a_vis_v2s_4_5l_low));
		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA", "Стул",
				89.0, 46.0, 55.0, "черешня/ваниль", 689,
				R.drawable.vis_a_vis_vkrt, R.drawable.vis_a_vis_vkrt_middle,
				R.drawable.vis_a_vis_vkrt_low));
		total_models_set.add(new CatalogueModel("VIS A VIS", "OCA", "Шкаф",
				210.0, 182.0, 62.0, "черешня/ваниль", 2229,
				R.drawable.vis_a_vis_vsu2s_21_18,
				R.drawable.vis_a_vis_vsu2s_21_18_middle,
				R.drawable.vis_a_vis_vsu2s_21_18_low));

	}
}