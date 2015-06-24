package com.company.GUI;

import com.company.Input;

public class Button {
    Runnable listener;
    public void setListener(Runnable listener) {this.listener = listener;}
    private float x;
    private float y;
    private float width;
    private float height;

    private boolean hover=false;

    public boolean getIsHOver() {
                          return hover;
    }

    public Button(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(int deltaTime) {
        hover=Input.mouseY>(y-height/2) && Input.mouseY<(y+height/2)
                && Input.mouseX>(x-width/2) && Input.mouseX<(x+width/2);

        if (Input.isWasMousePressed() && hover)
            listener.run();
    }
}
