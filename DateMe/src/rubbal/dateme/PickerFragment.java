package rubbal.dateme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PickerFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
        super.onCreateView(inflater, container, savedInstanceState);
        
        View view = inflater.inflate(R.layout.picker, container, false);
        return view;
    }
    
}
