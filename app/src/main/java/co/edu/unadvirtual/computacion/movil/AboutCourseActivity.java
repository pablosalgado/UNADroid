package co.edu.unadvirtual.computacion.movil;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.InputStream;
import java.net.URL;


public class AboutCourseActivity extends AppCompatActivity {

    PDFView pdfView;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);
        tabLayout =  findViewById(R.id.tabLayout);
        pdfView = findViewById(R.id.pdfView);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               switch (tab.getPosition()){
                   case 0:
                       renderDoc("http://unadroid.tk:3003/api/docs/course");
                       break;
                       case 1:
                           renderDoc("http://unadroid.tk:3003/api/docs/syllabus");
                       break;
                       case 2:
                           renderDoc("http://unadroid.tk:3003/api/docs/definitions");
                       break;
               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        renderDoc("http://unadroid.tk:3003/api/docs/course");
    }

    private void renderDoc(String url){
        try {
            Thread thread = new Thread(() -> {
                try  {
                    InputStream input = new URL(url).openStream();
                    pdfView.fromStream(input).load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
