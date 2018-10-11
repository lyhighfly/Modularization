package moduleb_export_service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liukuai.moduleb.R;

/**
 * Created by liuyu on 18/7/8.
 */

public class ModuleBFragment extends Fragment {

    public static ModuleBFragment mInstance = new ModuleBFragment();
    private String title;

    public static ModuleBFragment getInstance() {
        return mInstance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moduleb_fragment, null);
        TextView textView = view.findViewById(R.id.fragment_text);
        if (!TextUtils.isEmpty(title))
            textView.setText(textView.getText() + "  :::  " + title);

        view.findViewById(R.id.fragment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Hello I`m ModuleB`s Fragment Button",
                        Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
