package cit.bjtu.jxk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cit.bjtu.utils.HttpUtils;
import cit.bjtu.utils.HttpUtilsCallBack;


public class ExamActivity extends ActionBarActivity implements HttpUtilsCallBack {

    Boolean isWrongMode;
    TextView answer;

    ProgressDialog pd;
    static List<Map<String, String>> data;
    static List<Integer> wrongID;
    int Position;

    TextView title;
    RadioButton radioA;
    RadioButton radioB;
    RadioButton radioC;
    RadioButton radioD;
    TextView number;
    String rightAnswer;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("今日小测");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String paperID = bundle.getString("paperID");

        wrongID = new ArrayList<Integer>();
        isWrongMode = false;

        title = (TextView)findViewById(R.id.tvTitle);
        radioA = (RadioButton)findViewById(R.id.radioA);
        radioB = (RadioButton)findViewById(R.id.radioB);
        radioC = (RadioButton)findViewById(R.id.radioC);
        radioD = (RadioButton)findViewById(R.id.radioD);
        number = (TextView)findViewById(R.id.number);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        answer = (TextView)findViewById(R.id.rightAnswer);

        pd = ProgressDialog.show(this, "加载中", "正在加载试题数据，请稍后");

        Map<String, String> params = new HashMap<String, String>();
        params.put("paperID", paperID);
        new HttpUtils().Post(params, "http://bjtucit.sinaapp.com/api/getQuestions.php?paperID=" + paperID, this);

        final Button btn_next = (Button)findViewById(R.id.btnNext);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWrongMode == false) {
                    //正常模式
                    RadioButton selected = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selected == null || !selected.getTag().toString().equals(rightAnswer)) {
                        if(wrongID.size() != 0) {
                            if (wrongID.get(wrongID.size() - 1) != Position) {
                                wrongID.add(Position);
                            }
                        }else
                        {
                            wrongID.add(Position);
                        }
                    }
                    Position++;
                    if (Position <= data.size() - 1) {
                        title.setText("    " + data.get(Position).get("title"));
                        radioA.setText(data.get(Position).get("radioA"));
                        radioB.setText(data.get(Position).get("radioB"));
                        radioC.setText(data.get(Position).get("radioC"));
                        radioD.setText(data.get(Position).get("radioD"));
                        rightAnswer = data.get(Position).get("answer");
                        radioGroup.clearCheck();
                        number.setText(Position + 1 + "/" + data.size());
                    } else {
                        Position--;
                        if (wrongID.size() == 0) {
                            new AlertDialog.Builder(ExamActivity.this)
                                    .setTitle("恭喜")
                                    .setMessage("恭喜您答对了所有题目！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ExamActivity.this.finish();
                                        }
                                    })
                                    .show();
                        } else {
                            new AlertDialog.Builder(ExamActivity.this)
                                    .setTitle("提示")
                                    .setMessage("您答对了" + (data.size() - wrongID.size()) + "道题，答错了" + wrongID.size() + "道题。是否查看错题？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            isWrongMode = true;

                                            Position = 0;

                                            ExamActivity.this.setTitle("查看我的错题");

                                            title.setText("    " + data.get(wrongID.get(0)).get("title"));
                                            radioA.setText(data.get(wrongID.get(0)).get("radioA"));
                                            radioB.setText(data.get(wrongID.get(0)).get("radioB"));
                                            radioC.setText(data.get(wrongID.get(0)).get("radioC"));
                                            radioD.setText(data.get(wrongID.get(0)).get("radioD"));
                                            rightAnswer = data.get(wrongID.get(0)).get("answer");
                                            radioGroup.clearCheck();
                                            number.setText(Position + 1 + "/" + wrongID.size());
                                            answer.setText("正确答案：" + data.get(wrongID.get(0)).get("answer"));

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ExamActivity.this.finish();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
                else
                {
                    //错题模式

                    if(Position == wrongID.get(wrongID.size() - 1))
                    {
                        //最后一道错题
                        Toast.makeText(ExamActivity.this, "已经是最后一道错题!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Position++;
                        title.setText("    " + data.get(wrongID.get(Position)).get("title"));
                        radioA.setText(data.get(wrongID.get(Position)).get("radioA"));
                        radioB.setText(data.get(wrongID.get(Position)).get("radioB"));
                        radioC.setText(data.get(wrongID.get(Position)).get("radioC"));
                        radioD.setText(data.get(wrongID.get(Position)).get("radioD"));
                        rightAnswer = data.get(wrongID.get(Position)).get("answer");
                        radioGroup.clearCheck();
                        number.setText(Position + 1 + "/" + wrongID.size());
                        answer.setText("正确答案：" + data.get(wrongID.get(Position)).get("answer"));

                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 当ActionBar图标被点击时调用
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void method(String str) {
        if (pd.isShowing())
        {
            pd.dismiss();
        }
        if(str.equals("ERROR"))
        {
            Toast.makeText(this, "网络错误，请检查您的网络连接",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject;
            data = new ArrayList<Map<String, String>>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Map<String, String> map = new HashMap<String, String>();
                jsonObject = jsonArray.getJSONObject(i);
                map.put("title", jsonObject.getString("title"));
                map.put("radioA", jsonObject.getString("radioA"));
                map.put("radioB", jsonObject.getString("radioB"));
                map.put("radioC", jsonObject.getString("radioC"));
                map.put("radioD", jsonObject.getString("radioD"));
                map.put("answer", jsonObject.getString("answer"));
                data.add(map);
            }
            if (data.size() > 0) {
                title.setText("    " + data.get(0).get("title"));
                radioA.setText(data.get(0).get("radioA"));
                radioB.setText(data.get(0).get("radioB"));
                radioC.setText(data.get(0).get("radioC"));
                radioD.setText(data.get(0).get("radioD"));
                rightAnswer = data.get(0).get("answer");
                Position = 0;
                number.setText(Position + 1 + "/" + data.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
