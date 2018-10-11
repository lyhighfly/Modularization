package public_services;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by liuyu on 18/7/8.
 * 所有对外提供的服务，都需要继承自IProvider，且放置在base module中，一遍其他依赖此module的module能使用
 */

public interface ModuleBFragmentService extends IProvider{

    Fragment getModuleBFragment(String title);
}
