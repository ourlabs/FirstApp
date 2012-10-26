package rubbal.dateme;

import android.widget.BaseAdapter;

public class UserObject {
    
    String              name;
    String              id;
    private BaseAdapter adapter;
    private int         requestCode;
    
    public UserObject(String name, String id, int requestCode) {
    
        this.name = name;
        this.id = id;
        this.requestCode = requestCode;
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
    
        return "UserObject [name=" + name + ", id=" + id + ", adapter=" + adapter + ", requestCode=" + requestCode
                        + "]";
    }
    
    
}