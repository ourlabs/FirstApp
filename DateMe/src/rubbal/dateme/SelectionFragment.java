package rubbal.dateme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.GraphUser;
import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class SelectionFragment extends Fragment {
    
    private ProfilePictureView profilePictureView;
    private TextView           userNameView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
        super.onCreateView(inflater, container, savedInstanceState);
        
        View view = inflater.inflate(R.layout.selection, container, false);
        Button button = (Button) view.findViewById(R.id.announce_button);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.body_frame, MainActivity.fragments[MainActivity.PICKER], "NewFragmentTag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        
        profilePictureView.setCropped(true);
        
        userNameView = (TextView) view.findViewById(R.id.selection_user_name);
        final Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                
                @Override
                public void onCompleted(GraphUser user, Response response) {
                
                    if (session == Session.getActiveSession()) {
                        if (user != null) {
                            profilePictureView.setUserId(user.getId());
                            userNameView.setText(user.getName());
                        }
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
        return view;
    }
    
}
