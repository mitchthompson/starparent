package com.starparent.starparent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class StarPointTools extends BaseNavigationDrawerActivity {
    //Standard constants
    private static final String TAG = "StarPointsMain";
    private final String tag = "points_tutorial";
    private final String xmlFileName = tag + ".xml";
    private final String URL = "http://starparent.com/appdata/" + xmlFileName;

    //Classes we need
    Utils utils = new Utils();
    InputStream stream = null;
    XmlParser parser = new XmlParser();

    protected List<StaticClasses.PointsTutorialPoint> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer();
        getLayoutInflater().inflate(R.layout.activity_star_point_tools, frameLayout);
        setTitle("Star Points");

        try {
            parseXml();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        TextView lblTitle = (TextView)findViewById(R.id.lblToolName);
        TextView lblGoal = (TextView)findViewById(R.id.lblToolGoal);
        TextView lblHowToTitle = (TextView)findViewById(R.id.lblHowToTitle);
        TextView lblHowToText = (TextView)findViewById(R.id.lblHowToText);
        TextView lblExample = (TextView)findViewById(R.id.lblToolExample);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (points != null && b != null){
            int point = (int)b.get("point");
            int tool = (int)b.get("tool");

            lblTitle.setText(points.get(point).tools.get(tool).name);
            lblGoal.setText(points.get(point).tools.get(tool).goal);
            lblHowToTitle.setText(points.get(point).tools.get(tool).howToTitle);
            lblHowToText.setText(points.get(point).tools.get(tool).howToText);
            lblExample.setText(points.get(point).tools.get(tool).examples);
        }
    }

    //This should be usable in every ActivityClass
    private void parseXml() throws XmlPullParserException, IOException {
        stream = utils.isNetworkAvailable() ? parser.downloadUrl(URL) : this.getAssets().open(xmlFileName);
        try {
            points = parser.parse(stream, tag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}