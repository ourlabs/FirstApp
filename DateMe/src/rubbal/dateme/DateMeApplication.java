package rubbal.dateme;

import java.util.Collection;

import android.app.Application;

import com.facebook.GraphUser;

public class DateMeApplication extends Application {
    
    private Collection<GraphUser> selectedUsers;
    
    public Collection<GraphUser> getSelectedUsers() {
    
        return selectedUsers;
    }
    
    public void setSelectedUser(Collection<GraphUser> selectedUsers) {
    
        this.selectedUsers = selectedUsers;
    }
    
}
