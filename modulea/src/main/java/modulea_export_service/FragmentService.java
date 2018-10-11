package modulea_export_service;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import public_services.ModuleAFragmentService;

/**
 * Created by liuyu on 18/7/8.
 */

@Route(path = "/module_a/fragment_service")
public class FragmentService implements ModuleAFragmentService{


    @Override
    public Fragment getModuleAFragment(String title) {
        ModuleAFragment fragment =  ModuleAFragment.getInstance();
        fragment.setTitle(title);
        return fragment;
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {

    }
}
