package co.edu.unadvirtual.computacion.movil;

import android.opengl.Visibility;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


public class AboutCourseActivity extends AppCompatActivity {


    TabLayout tabLayout;
    CardView info_container;
    LinearLayout info;
    LinearLayout units;
    LinearLayout general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);
        tabLayout = findViewById(R.id.tabLayout);
        info_container = findViewById(R.id.info_container);
        info = findViewById(R.id.list);
        units = findViewById(R.id.list2);
        general = findViewById(R.id.list3);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Animation anim = AnimationUtils.loadAnimation(AboutCourseActivity.this, R.anim.righttoleft);

                switch (tab.getPosition()) {
                    case 0:
                        info.setVisibility(View.VISIBLE);
                        units.setVisibility(View.GONE);
                        general.setVisibility(View.GONE);

                        break;
                    case 1:
                        units.setVisibility(View.VISIBLE);
                        info.setVisibility(View.GONE);
                        general.setVisibility(View.GONE);
                        break;
                    case 2:
                        general.setVisibility(View.VISIBLE);
                        info.setVisibility(View.GONE);
                        units.setVisibility(View.GONE);
                        break;
                }

                info_container.startAnimation(anim);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
