package foxsay.ru.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DiagramView extends View {

    private int income;
    private int expense;

    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        expensePaint.setColor(ContextCompat.getColor(context, R.color.expense_color));
        incomePaint.setColor(ContextCompat.getColor(context, R.color.income_color));

        if (isInEditMode()) {
            income = 74000;
            expense = 5400;
        }
    }

    public void update(int income, int expense) {
        this.income = income;
        this.expense = expense;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (expense + income == 0)
            return;

        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);

        int space = 10; // space between income and expense
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;


        canvas.drawArc(
                xMargin - space,
                yMargin,
                getWidth() - xMargin - space,
                getHeight() - yMargin,
                180 - expenseAngle / 2,
                expenseAngle,
                true,
                expensePaint
        );

        canvas.drawArc(
                xMargin + space, yMargin,
                getWidth() - xMargin + space,
                getHeight() - yMargin,
                360 - incomeAngle / 2, incomeAngle,
                true,
                incomePaint
        );
    }
}
