package co.edu.unipiloto.ailearningmobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AnimatedBackgroundView extends View {

    private final Paint paintBackground = new Paint();
    private final Paint paintGlow = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float touchX = 0f;
    private float touchY = 0f;

    private long startTime;

    public AnimatedBackgroundView(Context context) {
        super(context);
        init();
    }

    public AnimatedBackgroundView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        init();
    }

    public AnimatedBackgroundView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        startTime = System.currentTimeMillis();

        setLayerType(
                View.LAYER_TYPE_SOFTWARE,
                null
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        if (w <= 0 || h <= 0) {
            return;
        }

        /*
         * Fondo principal.
         */
        LinearGradient gradient = new LinearGradient(
                0,
                0,
                w,
                h,
                new int[]{
                        0xFF090B18,
                        0xFF151335,
                        0xFF211349,
                        0xFF07101E
                },
                null,
                Shader.TileMode.CLAMP
        );

        paintBackground.setShader(gradient);

        canvas.drawRect(
                0,
                0,
                w,
                h,
                paintBackground
        );

        float tiempo =
                (System.currentTimeMillis() - startTime)
                        / 1000f;

        /*
         * Movimiento automático muy lento.
         */
        float mov1 =
                (float) Math.sin(tiempo * 0.35f);

        float mov2 =
                (float) Math.cos(tiempo * 0.25f);

        float mov3 =
                (float) Math.sin(tiempo * 0.18f);

        /*
         * Movimiento producido por el toque.
         */
        float px =
                touchX * 35f;

        float py =
                touchY * 35f;

        dibujarGlow(
                canvas,
                w * 0.18f + mov1 * 50 + px,
                h * 0.18f + mov2 * 60 + py,
                w * 0.45f,
                0x805C3BFF
        );

        dibujarGlow(
                canvas,
                w * 0.88f + mov2 * 65 - px,
                h * 0.40f + mov1 * 45 - py,
                w * 0.50f,
                0x704500FF
        );

        dibujarGlow(
                canvas,
                w * 0.40f + mov3 * 85,
                h * 0.90f + mov2 * 40,
                w * 0.55f,
                0x6037A7FF
        );

        /*
         * Pequeña esfera brillante para dar
         * sensación adicional de profundidad.
         */
        dibujarGlow(
                canvas,
                w * 0.73f + mov1 * 25,
                h * 0.13f + mov3 * 30,
                w * 0.18f,
                0x50FFFFFF
        );

        /*
         * Mantiene viva la animación.
         */
        postInvalidateDelayed(32);
    }

    private void dibujarGlow(
            Canvas canvas,
            float x,
            float y,
            float radius,
            int color
    ) {

        int transparente =
                color & 0x00FFFFFF;

        RadialGradient glow =
                new RadialGradient(
                        x,
                        y,
                        radius,
                        new int[]{
                                color,
                                transparente
                        },
                        null,
                        Shader.TileMode.CLAMP
                );

        paintGlow.setShader(glow);

        canvas.drawCircle(
                x,
                y,
                radius,
                paintGlow
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (
                event.getAction()
                        == MotionEvent.ACTION_DOWN
                ||
                event.getAction()
                        == MotionEvent.ACTION_MOVE
        ) {

            float centerX =
                    getWidth() / 2f;

            float centerY =
                    getHeight() / 2f;

            touchX =
                    (event.getX() - centerX)
                            / centerX;

            touchY =
                    (event.getY() - centerY)
                            / centerY;

            invalidate();

            return true;
        }

        if (
                event.getAction()
                        == MotionEvent.ACTION_UP
                ||
                event.getAction()
                        == MotionEvent.ACTION_CANCEL
        ) {

            animate()
                    .setDuration(500)
                    .withEndAction(() -> {

                        touchX = 0f;
                        touchY = 0f;

                        invalidate();
                    })
                    .start();

            return true;
        }

        return true;
    }
}