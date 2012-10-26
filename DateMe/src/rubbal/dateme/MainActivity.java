package rubbal.dateme;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class MainActivity extends FacebookActivity {
    
    protected static Facebook            facebook         = new Facebook("291579937623543");
    protected static AsyncFacebookRunner asyncRunner      = new AsyncFacebookRunner(facebook);
    
    protected static final int           LOGIN            = 0;
    protected static final int           SELECTION        = 1;
    protected static final int           PICKER           = 2;
    private static final int             COUNT            = 3;
    private static final String          FRAGMENT_PREFIX  = "fragment";
    private static boolean               restoredFragment = false;
    protected static Fragment[]          fragments        = new Fragment[COUNT];
    private boolean                      isResume         = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        for (int i = 0; i < COUNT; i++) {
            Log.i("onCreate of MainAct", "Restoring Fragment number : " + i);
            restoreFragment(savedInstanceState, i);
        }
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
        menu.add(1, Menu.FIRST, Menu.FIRST, "Search");
        menu.add(2, Menu.FIRST + 1, Menu.FIRST + 1, "Logout");
        menu.add(3, Menu.FIRST + 2, Menu.FIRST + 2, "Preferences");
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    /*
     * @SuppressWarnings("deprecation")
     * 
     * @Override
     * public void onActivityResult(int requestCode, int resultCode, Intent
     * data) {
     * 
     * super.onActivityResult(requestCode, resultCode, data);
     * facebook.authorizeCallback(requestCode, resultCode, data);
     * }
     */
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentById(R.id.body_frame);
        
        for (int i = 0; i < COUNT; i++) {
            if (fragments[i] == f) {
                manager.putFragment(outState, getBundleKey(i), f);
            }
        }
    }
    
    @Override
    protected void onResume() {
    
        super.onResume();
        isResume = true;
    }
    
    @Override
    protected void onPause() {
    
        super.onPause();
        isResume = false;
    }
    
    private void restoreFragment(Bundle savedInstanceState, int fragmentIndex) {
    
        Fragment fragment = null;
        Log.i("Inside restoreFragment", "Is savedInstanceState null?");
        if (savedInstanceState != null) {
            // TODO
            Log.i("Inside restoreFragment", "SavedInstanceState not null");
            
            FragmentManager fm = getSupportFragmentManager();
            fragment = fm.getFragment(savedInstanceState, getBundleKey(fragmentIndex));
        }
        Log.i("Inside restoreFragment", "SavedInstanceState is null");
        
        if (null != fragment) {
            Log.i("In restoreFragment of MainAct", "Found saved fragment no : " + fragmentIndex);
            fragments[fragmentIndex] = fragment;
            restoredFragment = true;
        } else {
            switch (fragmentIndex) {
                case LOGIN:
                    Log.i("Inside restoreFragment", "Creating new Login Fragment");
                    fragments[LOGIN] = new MyLoginFragment();
                    break;
                case SELECTION:
                    Log.i("Inside MainActivity", "Creating new Selection fragment");
                    fragments[SELECTION] = new SelectionFragment();
                    break;
                case PICKER:
                    Log.i("Inside MainActivity", "Creating new Picker fragment");
                    fragments[PICKER] = new PickerFragment();
                    break;
                default:
                    Log.e("Inside restoreFragment", "Unknown fragmentIndex");
                    break;
            }
        }
    }
    
    private String getBundleKey(int fragmentIndex) {
    
        return FRAGMENT_PREFIX + fragmentIndex;
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "dc", 10).show();
                break;
            case 2:
                Toast.makeText(this, "logout", 20).show();
                new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                    
                        Session session = getSession();
                        session.closeAndClearTokenInformation();
                        Log.i("Inside MainActivity", "Logging out");
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.body_frame, fragments[LOGIN]).commit();
                        
                    }
                };
                break;
            case 3:
                Toast.makeText(this, "dc2", 10).show();
                break;
        
        }
        return false;
    }
    
    @Override
    protected void onResumeFragments() {
    
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if (null == session || session.getState().isClosed()) {
            session = new Session(this);
            Session.setActiveSession(session);
            facebook.setAccessToken(session.getAccessToken());
        }
        if (restoredFragment) {
            return;
        }
        
        FragmentManager manager = getSupportFragmentManager();
        
        if (session.getState() == SessionState.CREATED_TOKEN_LOADED) {
            session.openForRead(this);
            Fragment fragment = manager.findFragmentById(R.id.body_frame);
            
            if (!(fragment instanceof SelectionFragment)) {
                manager.beginTransaction().replace(R.id.body_frame, fragments[SELECTION]).commit();
            }
        } else if (session.isOpened()) {
            // TODO Start new Activity
            Log.i("In Main activity", " Starting selector activity");
            Fragment fragment = manager.findFragmentById(R.id.body_frame);
            
            if (!(fragment instanceof SelectionFragment)) {
                manager.beginTransaction().replace(R.id.body_frame, fragments[SELECTION]).commit();
            }
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.body_frame, fragments[LOGIN]).commit();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
