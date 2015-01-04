package com.fgj.jcodecraeer.activitys;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgj.jcodecraeer.BaseActivity;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.JcodeMenu;
import com.fgj.jcodecraeer.sax.MenuService;

public class MainActivity extends BaseActivity implements OnItemClickListener{
	private GridView  gridview;
	private ArrayList<JcodeMenu> menus;
	private ImageView onwer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		prepareView();
	}

	@Override
	public void prepareView() {
		MenuService service = new MenuService(this);
		menus = service.getMenus();
		 
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new GridAdapter());
		gridview.setOnItemClickListener(this);
		
		onwer = (ImageView) findViewById(R.id.owner);
		onwer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, OwnerActivity.class);
				startActivity(i);
			}
		});
	}
	
	private class GridAdapter extends BaseAdapter{
 
		public GridAdapter(){
			
		}
		
		@Override
		public int getCount() {
			return menus.size();
		}

		@Override
		public Object getItem(int arg0) {
			return menus.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHodler hoder = null;
			if(view==null){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				view = inflater.inflate(R.layout.item_grid_menu, null);
				hoder = new ViewHodler();
				
				hoder.name = (TextView) view.findViewById(R.id.summary);
				hoder.title = (TextView) view.findViewById(R.id.title);
				hoder.img = (ImageView) view.findViewById(R.id.img_bg);
				
				view.setTag(hoder);
			}else{
				hoder = (ViewHodler) view.getTag();
			}
			
			hoder.name.setText(menus.get(arg0).getName());
			hoder.title.setText(menus.get(arg0).getHref());
			
			switch (arg0%4) {
			case 0:
				hoder.img.setImageResource(R.drawable.menu1);
				break;
			case 1:
				hoder.img.setImageResource(R.drawable.menu2);
				break;
			case 2:
				hoder.img.setImageResource(R.drawable.menu3);
				break;
			case 3:
				hoder.img.setImageResource(R.drawable.menu4);
				break;
			default:
				break;
			}
			
			return view;
		}
		
		public class ViewHodler{
			TextView name;
			TextView title;
			ImageView img;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		GridAdapter.ViewHodler holder = (GridAdapter.ViewHodler) view.getTag();
		String pagetid = holder.title.getText().toString();
		String name = holder.name.getText().toString();
		
		if(pagetid!=null && pagetid.contains("http://www.jcodecraeer.com/plus/list.php?tid=")){
			Intent i = new Intent();
			i.setClass(this, SubMainActivity.class);
			i.putExtra("pagetid", pagetid);
			i.putExtra("name", name);
			startActivity(i);
		}
		
	}

}
