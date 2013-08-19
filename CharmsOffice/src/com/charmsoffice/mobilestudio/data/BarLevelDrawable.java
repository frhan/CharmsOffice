package com.charmsoffice.mobilestudio.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

public final class BarLevelDrawable extends View {
    private ShapeDrawable mDrawable;
    private double mLevel = 0.1;

    final int[] segmentColors = {
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffb1d31f,
    		0xffc1a834,
    		0xffc1a834,
    		0xffc1a834,
    		0xffc1a834,
            0xffee151e,
            0xffee151e,
            0xffee151e,
            0xffee151e,
            0xffee151e,
            0xffee151e
            };
    final int [] segmentOffColor = 
    	{
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff484f27,
    		0xff5c5200,
            0xff5c5200,
            0xff5c5200,
            0xff5c5200,
    		0xff592929,
    		0xff592929,
    		0xff592929,
    		0xff592929,
    		0xff592929,
    		0xff592929
    	};
   // final int segmentOffColor = 0xff555555;


    public BarLevelDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBarLevelDrawable();
    }

    public BarLevelDrawable(Context context) {
        super(context);
        initBarLevelDrawable();
    }

    /**
     * Set the bar level. The level should be in the range [0.0 ; 1.0], i.e.
     * 0.0 gives no lit LEDs and 1.0 gives full scale.
     *
     * @param level the LED level in the range [0.0 ; 1.0].
     */
    public void setLevel(double level) {
        mLevel = level;
        invalidate();
    }

    public double getLevel() {
        return mLevel;
    }

    private void initBarLevelDrawable() {
        mLevel = 0.0;
    }

    private void drawBar(Canvas canvas) {
        int padding = 3; // Padding on both sides.
        int x = 0;
        int y = (int) getHeight()/3 - 5;

        int width = (int) (Math.floor(getWidth() / segmentColors.length))
                - (2 * padding);
        int height = 33;
        
        mDrawable = new ShapeDrawable(new RectShape());

        for (int i = 0; i < segmentColors.length; i++) {
            x = x + padding;
            if ((mLevel * segmentColors.length) > (i + 0.5)) {
                mDrawable.getPaint().setColor(segmentColors[i]);

            } else {
                mDrawable.getPaint().setColor(segmentOffColor[i]);
            }
            mDrawable.setBounds(x, y, x + width, y + height);
            mDrawable.draw(canvas);
            x = x + width + padding;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBar(canvas);
    }
}
