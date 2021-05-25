package com.ciphers.ecommerce.Helper;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.MyButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class WishListSwipeHelper extends ItemTouchHelper.SimpleCallback {

    int buttonWidth;
    private RecyclerView recyclerView;
    private List<MyButtons> buttonList;
    private GestureDetector gestureDetector;
    private int swipePosition = -1;
    private float swipeThreshHold = 0.5f;
    private Map<Integer, List<MyButtons>> buttonsBuffer;
    private Queue<Integer> removeQueue;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            for (MyButtons button : buttonList) {

                if(button.onClick(e.getX(), e.getY()))
                    break;;
            }

            return true;

        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(swipePosition < 0) return false;

            Point point = new Point((int)event.getRawX(), (int)event.getRawY());
            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            View swipeItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipeItem.getGlobalVisibleRect(rect);

            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE){

                if(rect.top < point.y && rect.bottom > point.y){
                    gestureDetector.onTouchEvent(event);
                }else{
                    removeQueue.add(swipePosition);
                    swipePosition = -1;
                    recoverSwipeItem();
                }

            }

            return false;

        }
    };

    private synchronized void recoverSwipeItem(){

        while (!(removeQueue.isEmpty())){

            int pos = removeQueue.poll();
            if(pos > -1){
                recyclerView.getAdapter().notifyItemChanged(pos);
            }

        }

    }

    public WishListSwipeHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttonList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.buttonsBuffer = new HashMap<>();
        this.buttonWidth = buttonWidth;

        removeQueue = new LinkedList<Integer>(){

            @Override
            public boolean add(Integer integer) {
                if(contains(integer))
                    return false;
                else
                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe(){

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public class MyButtons {

        String text;
        private int imgResId, textSize, color, pos;
        private RectF clickRegion;
        private MyButtonClickListener buttonClickListener;
        private Context context;
        private Resources resources;

        public MyButtons(Context context, String text, int textSize, int imgResId, int color, MyButtonClickListener buttonClickListener) {
            this.text = text;
            this.imgResId = imgResId;
            this.textSize = textSize;
            this.color = color;
            this.buttonClickListener = buttonClickListener;
            this.context = context;
            resources = context.getResources();
        }

        public boolean onClick(float x, float y) {

            if (clickRegion != null && clickRegion.contains(x, y)) {
                buttonClickListener.onClick(pos);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas c, RectF rectF, int pos) {

            Paint p = new Paint();

            p.setColor(color);
            c.drawRect(rectF, p);
            p.setColor(Color.WHITE);
            p.setTextSize(textSize);

            Rect r = new Rect();

            float cHeight = rectF.height();
            float cWidth = rectF.width();

            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x = 0, y = 0;

            if (imgResId == 0) {



                x = cWidth / 2f - r.width() / 2f - r.left;
                y = cHeight / 2f + r.height() / 2f - r.bottom;
                c.drawText(text, rectF.left+x, rectF.top+y, p);


            }else{

                Drawable d = ContextCompat.getDrawable(context, imgResId);
                Bitmap bitmap = drawableToBitmap(d);

                float bw = bitmap.getWidth()/2;
                float bh = bitmap.getHeight()/2;
                c.drawBitmap(bitmap, ((rectF.left+rectF.right)/2)-bw, ((rectF.top+rectF.bottom)/2 - bh), p);
//                c.drawBitmap(bitmap, (rectF.left+rectF.right)/2, (rectF.top+rectF.bottom)/2, p);


                x = cWidth / 2f - r.width() / 2f;
                y = cHeight / 2f + r.height() / 2f;

            }

            clickRegion = rectF;
            this.pos = pos;

        }

    }

    private Bitmap drawableToBitmap(Drawable d) {

        if(d instanceof BitmapDrawable)
            return ((BitmapDrawable)d).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0,0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if(swipePosition != pos)
            removeQueue.add(swipePosition);
        swipePosition = pos;

        if(buttonsBuffer.containsKey(swipePosition))
            buttonList = buttonsBuffer.get(swipePosition);
        else
            buttonList.clear();
        buttonsBuffer.clear();
        swipeThreshHold = 0.5f* buttonList.size()*buttonWidth;
        recoverSwipeItem();
    }

    public float getSwipeThreshHold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshHold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f *defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if(pos < 0){

            swipePosition = pos;
            return;
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX < 0){
                List<MyButtons> buffer = new ArrayList<>();

                if(!buttonsBuffer.containsKey(pos)){
                    initiateMyButtons(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                }else{
                    buffer = buttonsBuffer.get(pos);
                }
                translationX = dX* buffer.size()* buttonWidth / itemView.getWidth();
                drawButton(c, itemView,buffer, pos, translationX);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawButton(Canvas c, View itemView,List<MyButtons> buffer, int pos, float translationX) {

        float right = itemView.getRight();
        float dbButtonWidth = -1*translationX/ buffer.size();

        for (MyButtons button: buffer){

            float left = right - dbButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;

        }

    }

    public abstract void initiateMyButtons(RecyclerView.ViewHolder viewHolder, List<MyButtons> buffer);


}
