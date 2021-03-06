package com.starparent.starparent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StarProcessMainActivity extends BaseNavigationDrawerActivity {
    //Standard constants
    private static final String TAG = "StarProcessMain";
    private final String tag = "process_tutorial";
    private final String xmlFileName = tag + ".xml";
    private final String URL = "http://starparent.com/appdata/" + xmlFileName;

    //Classes we need
    //Utils utils = new Utils();
    InputStream stream = null;
    XmlParser parser = new XmlParser();

    protected List<StaticClasses.ProcessTutorialStep> steps;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Rendering the pane");
        super.onCreate(savedInstanceState);
        onCreateDrawer();
        getLayoutInflater().inflate(R.layout.activity_star_process_main, frameLayout);
        setTitle("Star Process");

        try {
            parseXml();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        final TextView lblName = (TextView) findViewById(R.id.lblStepName);
        final TextView lblDetail = (TextView) findViewById(R.id.lblStepDetail);
        Button btnPrevious = (Button) findViewById(R.id.btnPrevious);
        Button btnNext = (Button) findViewById(R.id.btnNext);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        //Steps looks good -- Do what you will with them!  :)
        if (steps != null && b != null) {
            Log.d(TAG, steps.toString());
            index = (int) b.get("step");

            lblName.setText(Html.fromHtml(steps.get(index).name));
            lblDetail.setText(Html.fromHtml(steps.get(index).detail));
        }

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0){
                    index = 3;
                    lblName.setText(Html.fromHtml(steps.get(index).name));
                    lblDetail.setText(Html.fromHtml(steps.get(index).detail));
                }else {
                    index--;
                    lblName.setText(Html.fromHtml(steps.get(index).name));
                    lblDetail.setText(Html.fromHtml(steps.get(index).detail));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 3){
                    index = 0;
                    lblName.setText(Html.fromHtml(steps.get(index).name));
                    lblDetail.setText(Html.fromHtml(steps.get(index).detail));
                }else {
                    index++;
                    lblName.setText(Html.fromHtml(steps.get(index).name));
                    lblDetail.setText(Html.fromHtml(steps.get(index).detail));
                }
            }
        });
    }

    //This should be usable in every ActivityClass
    private void parseXml() throws XmlPullParserException, IOException {
        try {
            boolean networkSuccess = false;
            try {
                Utils network = new Utils(this);
                steps = network.execute(URL, tag).get();
                networkSuccess = true;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (!networkSuccess) {
                    steps = parser.parse(this.getAssets().open(xmlFileName), tag);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
