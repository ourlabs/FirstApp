package rubbal.dateme;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class BaseListElement {
    
    private Drawable    icon;
    private String      name;
    private int         requestCode;
    
    private BaseAdapter adapter;
    
    /**
     * @param icon
     * @param name
     * @param requestCode
     */
    public BaseListElement(Drawable icon, String name, int requestCode) {
    
        this.icon = icon;
        this.name = name;
        this.requestCode = requestCode;
    }
    
    public void setAdapter(BaseAdapter adapter) {
    
        this.adapter = adapter;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setName(String name) {
    
        this.name = name;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    
    protected abstract View.OnClickListener getOnClickListener();
}
