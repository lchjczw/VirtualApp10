package io.virtualapp.widgets;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import io.virtualapp.R;
public class ShimmerViewHelper {
    private static final int DEFAULT_REFLECTION_COLOR = 0xFFFFFFFF;
    private View view;
    private Paint paint;
    private float gradientX;
    private LinearGradient linearGradient;
    private Matrix linearGradientMatrix;
    private int primaryColor;
    private int reflectionColor;
    private boolean isShimmering;
    private boolean isSetUp;
    private AnimationSetupCallback callback;
    public ShimmerViewHelper(View view, Paint paint, AttributeSet attributeSet) {
        this.view = view;
        this.paint = paint;
        init(attributeSet);
    }
    public float getGradientX() {
        return gradientX;
    }
    public void setGradientX(float gradientX) {
        this.gradientX = gradientX;
        view.invalidate();
    }
    public boolean isShimmering() {
        return isShimmering;
    }
    public void setShimmering(boolean isShimmering) {
        this.isShimmering = isShimmering;
    }
    public boolean isSetUp() {
        return isSetUp;
    }
    public void setAnimationSetupCallback(AnimationSetupCallback callback) {
        this.callback = callback;
    }
    public int getPrimaryColor() {
        return primaryColor;
    }
    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        if (isSetUp) {
            resetLinearGradient();
        }
    }
    public int getReflectionColor() {
        return reflectionColor;
    }
    public void setReflectionColor(int reflectionColor) {
        this.reflectionColor = reflectionColor;
        if (isSetUp) {
            resetLinearGradient();
        }
    }
    private void init(AttributeSet attributeSet) {
        reflectionColor = DEFAULT_REFLECTION_COLOR;
        if (attributeSet != null) {
            TypedArray a = view.getContext().obtainStyledAttributes(attributeSet, R.styleable.ShimmerView, 0, 0);
            if (a != null) {
                try {
                    reflectionColor = a.getColor(R.styleable.ShimmerView_reflectionColor, DEFAULT_REFLECTION_COLOR);
                } catch (Exception e) {
                    android.util.Log.e("ShimmerTextView", "Error while creating the view:", e);
                } finally {
                    a.recycle();
                }
            }
        }
        linearGradientMatrix = new Matrix();
    }
    private void resetLinearGradient() {
        linearGradient = new LinearGradient(-view.getWidth(), 0, 0, 0,
                new int[]{
                        primaryColor,
                        reflectionColor,
                        primaryColor,
                },
                new float[]{
                        0,
                        0.5f,
                        1
                },
                Shader.TileMode.CLAMP
        );
        paint.setShader(linearGradient);
    }
    protected void onSizeChanged() {
        resetLinearGradient();
        if (!isSetUp) {
            isSetUp = true;
            if (callback != null) {
                callback.onSetupAnimation(view);
            }
        }
    }
    public void onDraw() {
        if (isShimmering) {
            if (paint.getShader() == null) {
                paint.setShader(linearGradient);
            }
            linearGradientMatrix.setTranslate(2 * gradientX, 0);
            linearGradient.setLocalMatrix(linearGradientMatrix);
        } else {
            paint.setShader(null);
        }
    }
    public interface AnimationSetupCallback {
        void onSetupAnimation(View target);
    }
}