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

import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.LoginFragment;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class MainActivity extends FacebookActivity {
    
    Facebook                    facebook         = new Facebook("291579937623543");
    AsyncFacebookRunner         asyncRunner      = new AsyncFacebookRunner(facebook);
    
    private static final int    LOGIN            = 0;
    private static final int    COUNT            = 1;
    private static final String FRAGMENT_PREFIX  = "fragment";
    private static boolean      restoredFragment = false;
    Fragment[]                  fragments        = new Fragment[COUNT];
    private boolean             isResume         = false;
    
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
    
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
                    Log.i("Inside restoreFragment", "Creating new Fragment");
                    fragments[fragmentIndex] = new LoginFragment();
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
    protected void onResumeFragments() {
    
        super.onResumeFragments();
        Session session = Session.getActiveSession();
        if (null == session || session.getState().isClosed()) {
            session = new Session(this);
            Session.setActiveSession(session);
        }
        if (restoredFragment) {
            return;
        }
        
        FragmentManager manager = getSupportFragmentManager();
        
        if (session.getState() == SessionState.CREATED_TOKEN_LOADED) {
            session.openForRead(this);
            Log.i("In Main activity", " Starting selector activity");
            Intent intent = new Intent(this, FriendSelectorActivity.class);
            startActivity(intent);
        } else if (session.isOpened()) {
            // TODO Start new Activity
            Log.i("In Main activity", " Starting selector activity");
            Intent intent = new Intent(this, FriendSelectorActivity.class);
            startActivity(intent);
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.body_frame, fragments[LOGIN]).commit();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                String results = "";
                if (resultCode == RESULT_OK) {
                    DateMeApplication application = (DateMeApplication) getApplication();
                    Collection<GraphUser> selection = application.getSelectedUsers();
                    if (null != selection && selection.size() > 0) {
                        ArrayList<String> names = new ArrayList<String>();
                        for (GraphUser user : selection) {
                            names.add(user.getName());
                        }
                        
                        results = TextUtils.join(", ", names);
                    } else {
                        results = "No Friends Selected";
                    }
                } else {
                    results = "Cancelled";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You picked").setMessage(results).setPositiveButton("OK", null);
                builder.show();
                
                break;
        }
    }
}
