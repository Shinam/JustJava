package com.example.android.justjava;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int REINIT_ID = Menu.FIRST;
    private static final int COIN = Menu.FIRST + 1;
    private static final int DICE = Menu.FIRST + 2;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout activity_main;
    boolean p1 = true;
    boolean p2 = false;
    int input = 0;
    MediaPlayer LPDecrease;
    MediaPlayer LPReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = (LinearLayout) findViewById(R.id.activity_main);
        LPDecrease = MediaPlayer.create(this, R.raw.life_points_decrease);
        LPReset = MediaPlayer.create(this, R.raw.life_points_reset);
    }


    public void TakeDammages(View view) {
        Button button1 = (Button) findViewById(R.id.buttonp1);
        Button button2 = (Button) findViewById(R.id.buttonp2);
        if (p1) {
            button1.setTextColor(Color.parseColor("#ac1f28"));
            button2.setTextColor(Color.parseColor("#61d303"));
            p1 = false;
            p2 = true;
        } else {
            button1.setTextColor(Color.parseColor("#61d303"));
            button2.setTextColor(Color.parseColor("#ac1f28"));
            p1 = true;
            p2 = false;
        }
    }

    public void remove(View view) {
        int scor;
        int fin;
        TextView t = (TextView) findViewById(R.id.scoreJ1);
        if(p2)
            t = (TextView) findViewById(R.id.scoreJ2);
        scor = Integer.parseInt(t.getText().toString());
        fin = scor - input;
        if (fin <= 0) {
            fin = 0;
            if(p1)
                winner(true);
            else
                winner(false);
        }
        TimeIt(scor, fin);
        TimeDammage(input, 0);
        input = 0;
    }

    public void ajout(View view) {
        int scor;
        int fin;
        TextView t = (TextView) findViewById(R.id.scoreJ1);
        if(p2)
            t = (TextView) findViewById(R.id.scoreJ2);
        scor = Integer.parseInt(t.getText().toString());
        fin = scor + input;
        TimeIt(scor, fin);
        TimeDammage(input, 0);
        input = 0;
    }

    public void value (View v) {
        Button b = (Button) v;
        input = input * 10;
        input = input + Integer.parseInt(b.getText().toString());
        inputCheck();
    }

    public void inputCheck() {
        if (input >= 10000) {
            input = 0;
            displayInput(input);
            Toast.makeText(this, "Your number is to high!", Toast.LENGTH_SHORT).show();
        } else {
            displayInput(input);
        }
    }

    public void displayInput(int input) {
        TextView scoreView = (TextView) findViewById(R.id.dammages);
        scoreView.setText(String.valueOf(input));
    }

    public void clear(View v){
        input = 0;
        displayInput(input);
    }

    /*private void winner(boolean player)
    {
        if(player){
            startActivity(new Intent(MainActivity.this, Pop.class).putExtra("pl", "Player 2"));
        }
        else
            startActivity(new Intent(MainActivity.this, Pop.class).putExtra("pl", "Player 1"));
    }*/

    private void winner(boolean player) {
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindows, null);
        popupWindow = new PopupWindow(container, ActionBar.LayoutParams.MATCH_PARENT, 500, true);
        popupWindow.showAtLocation(activity_main, Gravity.CENTER, 0, 0);
        TextView win = (TextView) popupWindow.getContentView().findViewById(R.id.text);
        if(player)
            win.setText("Player 2");
        else
            win.setText("Player 1");
        /**Used to close the window via touch */
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void TimeIt(int val, int scored){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(val, scored);
        valueAnimator.setDuration(1800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextView textview = (TextView) findViewById(R.id.scoreJ1);
                if(p2) {
                    textview = (TextView) findViewById(R.id.scoreJ2);
                }
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
        LPDecrease.start();
    }

    private void TimeDammage(int val, int scored){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(val, scored);
        valueAnimator.setDuration(1800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Button b = (Button) findViewById(R.id.dammages);
                b.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
        LPDecrease.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, REINIT_ID, 0, "Reset");
        menu.add(0, COIN, 0, "Coin");
        menu.add(0, DICE, 0, "Dice");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case REINIT_ID:
                reset();
                break;
            case COIN:
                Coin();
                break;
            case DICE:
                Dice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset()
    {
        p1 = false;
        p2 = true;
        TakeDammages(null);
        TextView number = (TextView) findViewById(R.id.scoreJ1);
        number.setText("" + 8000);
        TextView number2 = (TextView) findViewById(R.id.scoreJ2);
        number2.setText("" + 8000);
        LPReset.start();
    }

    private void Coin()
    {
        Random rand = new Random();
        int randDice = rand.nextInt(3 - 1 ) + 1;
        if(randDice == 1)
            Toast.makeText(this, "Heads", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Tails", Toast.LENGTH_LONG).show();

    }

    private void Dice()
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(7 - 1) + 1;
        Toast.makeText(this, ""+randomNum, Toast.LENGTH_LONG).show();
    }
}
