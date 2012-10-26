package rubbal.dateme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class PickerFragment extends Fragment {
    
    private final List<UserObject> listElements = new ArrayList<UserObject>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.picker, container, false);
        this.init(savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.selection_list);
        ActionListAdapter adapter = new ActionListAdapter(getActivity(), R.id.selection_list, listElements);
        if (listView == null) Log.i("In friend picker", "Listview Null");

        listView.setAdapter(adapter);
        return view;
    }
    
    @SuppressWarnings("deprecation")
    private void init(Bundle savedInstanceState) {
    
        Bundle params = new Bundle();
        params.putString("fields", "name,id");
        params.putString("access_token", Session.getActiveSession().getAccessToken());
        MainActivity.asyncRunner.request("me/friends", params, "GET", new RequestListener() {
            
            @Override
            public void onMalformedURLException(MalformedURLException e, Object state) {
            
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onIOException(IOException e, Object state) {
            
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onFileNotFoundException(FileNotFoundException e, Object state) {
            
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onFacebookError(FacebookError e, Object state) {
            
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onComplete(String response, Object state) {
            
                try {
                    JSONObject json = Util.parseJson(response);
                    JSONArray friendsData = json.getJSONArray("data");
                    
                    for (int i = 0; i < friendsData.length(); i++) {
                        UserObject userObject = new UserObject(friendsData.getJSONObject(i).getString("id"),
                                        friendsData.getJSONObject(i).getString("name"), 0);
                        listElements.add(userObject);
                    }
                    Log.i("Friend List first element", listElements.get(0).toString());
                } catch (Exception e) {
                    Log.i("In friend picker activity", "Error while parsing JSON response", e);
                }
                
            }
        }, null);
        
    }
    
    private class ActionListAdapter extends ArrayAdapter<UserObject> {
        
        private List<UserObject> listElements;
        
        public ActionListAdapter(Context context, int textViewResourceId, List<UserObject> listElements) {
        
            super(context, textViewResourceId, listElements);
            this.listElements = listElements;
            
            for (int i = 0; i < listElements.size(); i++) {
                this.listElements.get(i).setAdapter(this);
            }
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        
            View view = convertView;
            
            if (null == convertView) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.id.selection_list, null);
            }
            
            UserObject user = listElements.get(position);
            if (user != null) {
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                TextView name = (TextView) view.findViewById(R.id.text1);
                TextView id = (TextView) view.findViewById(R.id.text2);
                /*
                 * if (icon != null) {
                 * icon.setImageDrawable(user.getIcon());
                 * }
                 */
                if (name != null) {
                    name.setText(user.getName());
                }
                if (id != null) {
                    id.setText(user.getId());
                }
            }
            return view;
        }
    }
}
