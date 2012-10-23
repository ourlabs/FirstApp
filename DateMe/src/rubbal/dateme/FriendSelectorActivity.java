package rubbal.dateme;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.FacebookException;
import com.facebook.FriendPickerFragment;
import com.facebook.PickerFragment;

public class FriendSelectorActivity extends FragmentActivity {
    
    FriendPickerFragment friendPickerFragment;
    
    public static void populateParameters(Intent intent, String userId, boolean multiSelect) {
    
        intent.putExtra(FriendPickerFragment.USER_ID_BUNDLE_KEY, userId);
        intent.putExtra(FriendPickerFragment.MULTI_SELECT_BUNDLE_KEY, multiSelect);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_friends_activity);
        
        FragmentManager manager = getSupportFragmentManager();
        if (null == savedInstanceState) {
            final Bundle args = getIntent().getExtras();
            friendPickerFragment = new FriendPickerFragment(args);
            manager.beginTransaction().add(R.id.friend_picker_fragment, friendPickerFragment).commit();
        } else {
            friendPickerFragment = (FriendPickerFragment) manager.findFragmentById(R.id.friend_picker_fragment);
        }
        
        friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            
            @Override
            public void onError(FacebookException error) {
            
                FriendSelectorActivity.this.onError(error);
            }
        });
        
        friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
            
            @Override
            public void onDoneButtonClicked() {
            
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    private void onError(Exception error) {
    
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error").setMessage(error.getMessage()).setPositiveButton("OK", null);
        builder.show();
    }
    
    @Override
    protected void onStart() {
    
        super.onStart();
        try {
            friendPickerFragment.loadData(false);
        } catch (Exception e) {
            onError(e);
        }
    }
}
