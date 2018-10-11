package moduleb_export_service;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import public_services.ModuleBFragmentService;

/**
 * Created by liuyu on 18/7/8.
 */

@Route(path = "/module_b/fragment_service")
public class FragmentService implements ModuleBFragmentService {


    @Override
    public Fragment getModuleBFragment(String title) {
        ModuleBFragment fragment =  ModuleBFragment.getInstance();
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
