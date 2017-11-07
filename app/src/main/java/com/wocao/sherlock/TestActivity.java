package com.wocao.sherlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wocao.sherlock.CoreService.FloatWindowAppList;
import com.wocao.sherlock.Widget.BottomCard;
import com.wocao.sherlock.Widget.ColorPickerDialog;

public class TestActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    FloatWindowAppList appList;
    BottomCard bottomCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bottomCard=(BottomCard)findViewById(R.id.BottomCardTest);
        appList=new FloatWindowAppList(this,bottomCard,0);
        textView=(TextView)findViewById(R.id.textViewT);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("数据：" +
                        "ContentItem:" +appList.getContentNumber()+
                        "\nHeight:" +bottomCard.getHeight()+
                        "\nBottomH:" +bottomCard.getBottomH()+
                        "\nOffset:" +bottomCard.getOffset()+
                        "\nPosition:" +bottomCard.getY()+
                        "" +
                        "");
            }
        });
    }
}
