package de.hfu.mos;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import de.hfu.mos.campus.CampusFragment;
import de.hfu.mos.fakultaet.InformatikFragment;
import de.hfu.mos.home.FelixFragment;
import de.hfu.mos.home.HomeFragment;
import de.hfu.mos.home.budget.BudgetRechner;
import de.hfu.mos.campus.map.POIFragment;
import de.hfu.mos.home.news.RSSFragment;
import de.hfu.mos.kontakte.KontaktFragment;
import de.hfu.mos.studiengang.StudiengangFragment;
import de.hfu.mos.vorlesungsplan.VorlesungsplanFragment;
import de.hfu.mos.webmail.WebmailFragment;
import de.hfu.mos.website.WebsiteFragment;

public class MainActivity extends Activity {

    // declare properties
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SearchView searchView;

    private ActionBarDrawerToggle mDrawerToggle;
    
    //used to track backpressed. twice pressed in 1s should close app
    private int backPressed;
	private Handler h;
	private int delay; //milliseconds

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // global Fragment for other methods, e.g. onBackPressed()
    private Fragment fragment;

    // Downloadmanager to download file in WebView
    DownloadManager _DownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);       

        backPressed = 0;
        
		h = new Handler();
		delay = 1000; //milliseconds

		h.postDelayed(new Runnable(){
		    public void run(){
		    	backPressed = 0;
		    	h.postDelayed(this, delay);
		    }
		}, delay);
        
        
        //init Downloadmanager
        _DownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        // for proper titles
        mTitle = mDrawerTitle = getTitle();

        // initialize properties
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Drawermenu Header und Foot (icons)

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, mDrawerList,
                false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer, mDrawerList,
                false);
        mDrawerList.addHeaderView(header, null, false);
        mDrawerList.addFooterView(footer, null, true);

        // list the drawer items
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[8];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_nav_home, "Home");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_nav_studiengang, "Studiengang MOS");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_nav_informatik, "Fakultät Informatik");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_nav_kontakte, "Kontakt");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_nav_campus, "Campus");
        drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_nav_webmail, "Webmail");
        drawerItem[6] = new ObjectDrawerItem(R.drawable.ic_nav_vorlesungsplan, "Vorlesungsplan");
        drawerItem[7] = new ObjectDrawerItem(R.drawable.ic_nav_website, "Webseite");

        // Pass the folderData to our ListView adapter
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);

        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);

        // set the item click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        // for app icon control for nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectItem(0);
        }


    }


    public void onClick(View v) {

        fragment = null;
        
        switch (v.getId()) {

            case R.id.rssreader:
            	fragment = new RSSFragment();
            	openFragment(fragment, -2);
                break;
            case R.id.buttonBudget:
                fragment = new BudgetRechner();
                openFragment(fragment, -3);
                break;
            case R.id.buttonMap:
            	Intent hfuMap = new Intent(this,POIFragment.class);
                startActivity(hfuMap);
            	break;
            case R.id.buttonFelix:
                fragment = new FelixFragment(_DownloadManager);
                openFragment(fragment, -1);
                break;
            case R.id.fb_button:
                Intent facebookIntent = getOpenFacebookIntent(this);
                startActivity(facebookIntent);
                break;
            case R.id.studi_button:
                String pnSt = "de.hfu.funfpunktnull";
                getOpenAppIntent(this, pnSt);
                break;
           case R.id.mensa_button:
                String pnMs = "de.hfu.mensa";
                getOpenAppIntent(this, pnMs);
                break;
            case R.id.bib_button:
                String pnBb = "de.hfubib";
                getOpenAppIntent(this, pnBb);
                break;
        }
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/137629312959259"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/HFUInformatik"));
        }
    }

    public void getOpenAppIntent(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
        /* we found the activity now start the activity */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {

        /* bring user to the market or let them choose an app? */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            startActivity(intent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fragment = new WebsiteFragment(query);
                openFragment(fragment, 6);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSearchRequested() {


        return super.onSearchRequested();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    // navigation drawer click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        // update the main content by replacing fragments

        fragment = null;

        switch (position) {
            case 0:
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new StudiengangFragment(_DownloadManager);
                break;
            case 3:
                fragment = new InformatikFragment(_DownloadManager);
                break;
            case 4:
                fragment = new KontaktFragment();
                break;
            case 5:
                fragment = new CampusFragment();
                break;
            case 6:
                fragment = new WebmailFragment();
                break;
            case 7:
                fragment = new VorlesungsplanFragment(_DownloadManager);
                break;
            case 8:
                fragment = new WebsiteFragment();
                break;
            default:
                break;
        }
        openFragment(fragment, position);
    }

    //renders the fragment in activity_main
    protected void openFragment(Fragment fragment, int position) {

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            //change the title to actuall fragment depended on position
            //position is handled normaly by navigation drawer, but manually if button click
            switch (position) {
            //position of RSS:
	            case -3:
	            	setTitle("BudgetRechner");
	                break;
                //position of RSS:
                case -2:
                	setTitle("HFUReader");
                    break;
                //position of Felix:
                case -1:
                    setTitle("Felix");
                    break;
                //if position is declared by navigation drawer::
                default:
                    // update selected item and title, then close the drawer
                    mDrawerList.setItemChecked(position, true);
                    mDrawerList.setSelection(position);
                    setTitle(mNavigationDrawerItemTitles[position]);
                    mDrawerLayout.closeDrawer(mDrawerList);
            }
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public int getBackPressed(){
    	
    	return backPressed;
    	
    }
    
    public void incBackPressed(){
    	
    	backPressed++;
    }
    
    @Override
    public void onBackPressed() {

    	incBackPressed();
        //enables WebView to go page back in website and felix
        if (fragment instanceof WebsiteFragment) {
            if (!((WebsiteFragment) fragment).webGoBack() &&
            		getBackPressed() > 1) {
                super.onBackPressed();
            }
        } else if (fragment instanceof FelixFragment) {
            if (!((FelixFragment) fragment).webGoBack() &&
            		getBackPressed() > 1) {
                super.onBackPressed();
            }
        } else if(getBackPressed() > 1)
        	super.onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


}
