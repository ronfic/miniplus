package com.example.miniplus_dev;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.miniplus_dev.setting.MeasureResultTestFrag;
import com.example.miniplus_dev.setting.MeasureResultTestFrag2;


public class MyViewPagerAdapter2 extends FragmentStatePagerAdapter {




    /*

     * 이 클래스의 부모생성자 호출시 인수로 반드시 FragmentManager객체를 넘겨야한다.

     * 이 객체는 Activity에서만 만들수 있고, 여기서사용중인 Fragment가 v4이기 때문에

     * Activity중에서도 ActionBarActivity에서 얻어와야한다.

     */


    Fragment[] fragments = new Fragment[5];


    public MyViewPagerAdapter2(FragmentManager fm) {

        super(fm);

        fragments[0] = new MeasureResultTestFrag2();
        fragments[1] = new MeasureResultTestFrag2();
        fragments[2] = new MeasureResultTestFrag2();
        fragments[3] = new MeasureResultTestFrag2();
        fragments[4] = new MeasureResultTestFrag2();

        //fragments[1] = new MeasureResultLastFrag();
        //fragments[2] = new FragmentA();
        //fragments[2] = new MeasureResultOldFrag();


    }


    //아래의 메서드들의 호출 주체는 ViewPager이다.

    //ListView와 원리가 같다.



    /*

     * 여러 프레그먼트 중 어떤 프레그먼트를 보여줄지 결정

     */

    public Fragment getItem(int arg0) {

        return fragments[arg0];
    }



    /*

     * 보여질 프레그먼트가 몇개인지 결정

     */

    public int getCount() {

        return fragments.length;

    }


}