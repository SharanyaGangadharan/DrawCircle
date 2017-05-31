package com.example.shara.assignment3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by shara on 2/23/2017.
 */

public class SimpleDrawing extends View{

    private static String mode = "Draw";
    public static int color = Color.BLACK;
    ArrayList<Circle> circles = new ArrayList<>();
    private float startX=0, startY=0, endX=0, endY=0;
    private float radius=20;
    boolean swipeInProgress = false;
    int screenWidth = 0, screenHeight = 0, toolBarPixels = 150;
    Paint paint;
    Circle circle;
    private VelocityTracker velocityTracker = null;
    private Float xVelocity = 0f;
    private Float yVelocity = 0f;

    public SimpleDrawing(Context context) {
        super(context);
        paint = new Paint();
    }

    public SimpleDrawing(Context context, AttributeSet xmlAttributes) {
        super(context, xmlAttributes);
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public void setColor(String color)
    {
        if(color == "blue")
            this.color = Color.BLUE;
        else if(color == "green")
            this.color = Color.GREEN;
        else if(color == "red")
            this.color = Color.RED;
        else if(color == "black")
            this.color = Color.BLACK;
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    protected void onDraw(Canvas canvas) {
        for(Circle each:circles) {
            startX = each.getStartX();
            startY = each.getStartY();
            endX = each.getEndX();
            endY = each.getEndY();
            radius = each.getRadius();
            if (xboundaryCheck(startX, radius) || yBoundaryCheck(startY, radius)) continue;
            paint = each.getPaint();
            canvas.drawCircle(startX, startY, radius, paint);
        }
        if (mode == "Move") {
            circleMove();
            invalidate();
        }
    }

    public boolean xboundaryCheck(float startX, float radius) {
        screenWidth = getScreenWidth();
        if(startX-radius<0){
            return true; }
        if(startX+radius>screenWidth){
            return true;}
        return false;
    }

    public boolean yBoundaryCheck(float startY, float radius) {
        screenHeight = getScreenHeight();
        if (startY-radius < 0){
            return true;}
        if (startY + radius + toolBarPixels > screenHeight) {
            return true;}
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode == "Draw") {
            int action = event.getAction();
            int actionCode = action & MotionEvent.ACTION_MASK;
            switch (actionCode) {
                case MotionEvent.ACTION_DOWN:
                    return handleActionDown(event);
                case MotionEvent.ACTION_MOVE:
                    if(circle != null) {
                        circle.setEndX(event.getX());
                        circle.setEndY(event.getY());
                        radius = calcRadius(circle.getStartX(),circle.getStartY(),circle.getEndX(),circle.getEndY());
                        if((int)endX==(int)startX||(int)endY==(int)startY) circle.setRadius(20);
                        else circle.setRadius(radius);
                        circle.setColor(color);
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    handleActionUp(event);
                    swipeInProgress = false;
                    return false;
            }
            invalidate();
        }
        else if(mode=="Delete")
        {
            ArrayList<Circle>  deleteCircles = new ArrayList<>();
            for(Circle circle: circles)
            {
                float f1 = circle.getStartX();
                float f2 = circle.getStartY();
                float f3 = event.getX();
                float f4 = event.getY();
                float distx = f3 - f1;
                float disty = f4 - f2;
                float distance = (float) Math.sqrt((distx * distx) + (disty * disty));
                if(distance <= circle.getRadius())
                    deleteCircles.add(circle);
            }
            for(Circle circle: deleteCircles)
                circles.remove(circle);
                invalidate();
            return true;
        }
        else if(mode=="Move") {
                int action = event.getAction();
                int actionCode = action & MotionEvent.ACTION_MASK;
                switch (actionCode) {
                    case MotionEvent.ACTION_DOWN:
                        if (velocityTracker == null) {
                            velocityTracker = VelocityTracker.obtain();
                        } else {
                            velocityTracker.clear();
                        }
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        velocityTracker.addMovement(event);
                        velocityTracker.computeCurrentVelocity(10);
                        xVelocity = velocityTracker.getXVelocity();
                        yVelocity = velocityTracker.getYVelocity();
                        setMoveCircle(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        circleMove();
                        invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        velocityTracker.recycle();
                        velocityTracker = null;
                        break;
                }
        }
        return true;
    }

    private void setMoveCircle(MotionEvent event){
        for(Circle circle: circles){
            float f1 = circle.getStartX();
            float f2 = circle.getStartY();
            float f3 = event.getX();
            float f4 = event.getY();
            float distx = f3 - f1;
            float disty = f4 - f2;
            float distance = (float) Math.sqrt((distx * distx) + (disty * disty));
            if(distance <= circle.getRadius()){
                circle.setMoving(true);
            }
        }
    }

    public boolean circleMove() {
        for(Circle circle: circles) {
            startX = circle.getStartX();
            startY = circle.getStartY();
            if (circle.isCircleMoving() || circle.isInMotion()){
                if(circle.isCircleMoving()){
                    circle.setxVelocity(xVelocity);
                    circle.setyVelocity(yVelocity);
                    circle.setInMotion(true);
                    circle.setMoving(false);
                }
                if (xboundaryCheck(circle.getStartX() + circle.getxVelocity(),circle.getRadius())) {
                    circle.setxVelocity(circle.getxVelocity()*-1);
                    startX += circle.getxVelocity();
                }
                else {
                    startX += circle.getxVelocity();
                }

                if (yBoundaryCheck(circle.getStartY()+circle.getyVelocity(),circle.getRadius())) {
                    circle.setyVelocity(circle.getyVelocity() * -1);
                    startY += circle.getyVelocity();
                }
                else{
                    startY += circle.getyVelocity();
                }
                circle.setStartX(startX);
                circle.setStartY(startY);
            }
        }
        return true;
    }


    public boolean handleActionDown(MotionEvent event){
        swipeInProgress = true;
        startX = (int) event.getX();
        startY = (int) event.getY();
        circle = new Circle();
        circle.setStartX(startX);
        circle.setStartY(startY);
        radius = calcRadius(circle.getStartX(),circle.getStartY(),circle.getEndX(),circle.getEndY());
        if((int)endX==(int)startX||(int)endY==(int)startY) circle.setRadius(20);
        else circle.setRadius(radius);
        circle.setColor(color);
        circles.add(circle);
        return true;
    }

    public float calcRadius(float startX, float startY, float endX, float endY)
    {
        float distanceX = endX - startX;
        float distanceY = endY - startY;
        radius = (float) Math.sqrt(distanceX *distanceX + distanceY *distanceY);
        return radius;
    }

    public boolean handleActionUp(MotionEvent event){
        endX = (int) event.getX();
        endY = (int) event.getY();
        if(circle != null){
            circle = new Circle();
            circle.setStartX(startX);
            circle.setStartY(startY);
            circle.setEndX(endX);
            circle.setEndY(endY);
            radius = calcRadius(circle.getStartX(),circle.getStartY(),circle.getEndX(),circle.getEndY());
            if((int)endX==(int)startX||(int)endY==(int)startY) circle.setRadius(20);
            else circle.setRadius(radius);
            circle.setColor(color);
            invalidate();
        }
        return false;
    }
}

class Circle {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private int color;
    private Paint paint;
    private float radius=20;
    private Float xVelocity = 0f;
    private Float yVelocity = 0f;
    private boolean isCircleMoving = false;
    private boolean inMotion = true;

    public void setStartX(float startX)
    {
        this.startX = startX;
    }

    public void setStartY(float startY)
    {
        this.startY = startY;
    }

    public void setEndX(float endX)
    {
        this.endX = endX;
    }

    public void setEndY(float endY)
    {
        this.endY = endY;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public float getStartX()
    {
        return startX;
    }

    public float getStartY()
    {
        return startY;
    }

    public float getEndX()
    {
        return endX;
    }

    public float getEndY()
    {
        return endY;
    }

    public float getRadius()
    {
        return radius;
    }

    public void setColor(int color)
    {
        this.color = color;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public Paint getPaint()
    {
        return paint;
    }

    public boolean isCircleMoving() {
        return isCircleMoving;
    }

    public void setMoving(boolean moving) {
        isCircleMoving = moving;
    }

    public void setxVelocity(Float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public Float getxVelocity() {
        return this.xVelocity;
    }

    public boolean isInMotion() {
        return inMotion;
    }

    public void setInMotion(boolean inMotion) {
        this.inMotion = inMotion;
    }

    public Float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(Float yVelocity) {
        this.yVelocity = yVelocity;
    }

}
