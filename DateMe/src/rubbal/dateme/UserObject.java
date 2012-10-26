package rubbal.dateme;

import com.facebook.ProfilePictureView;

import android.widget.BaseAdapter;

public class UserObject {
    
    String              name;
    String              id;
    private BaseAdapter adapter;
    private String      profilePictureSrc;
    private int         requestCode;
    
    public UserObject(String name, String id, int requestCode, String profilePictureSrc) {
    
        this.name = name;
        this.id = id;
        this.requestCode = requestCode;
        this.profilePictureSrc = profilePictureSrc;
    }
    
    public String getProfilePictureView() {
    
        return profilePictureSrc;
    }
    
    public void setProfilePictureView(String profilePictureSrc) {
    
        this.profilePictureSrc = profilePictureSrc;
    }
    
    public int getRequestCode() {
    
        return requestCode;
    }
    
    public void setRequestCode(int requestCode) {
    
        this.requestCode = requestCode;
    }
    
    public BaseAdapter getAdapter() {
    
        return adapter;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName(String name) {
    
        this.name = name;
    }
    
    public String getId() {
    
        return id;
    }
    
    public void setId(String id) {
    
        this.id = id;
    }
    
    public void setAdapter(BaseAdapter adapter) {
    
        this.adapter = adapter;
    }
    
    protected void notifyDataChangeed() {
    
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public String toString() {
    
        return "UserObject [name=" + name + ", id=" + id + ", adapter=" + adapter + ", profilePictureView="
                        + profilePictureSrc + ", requestCode=" + requestCode + "]";
    }
    
}