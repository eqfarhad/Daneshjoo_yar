package ir.daneshjou_yaar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;


/**
 * Created by iqfarhad on 11/9/2017.
 */

public class Custom_Text_View extends android.support.v7.widget.AppCompatTextView{
    public Custom_Text_View(Context context) {
        super(context);
    }

    public Custom_Text_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = array.getString(R.styleable.CustomTextView_fontname);
        if (TextUtils.isEmpty(fontName))
            fontName = "IRKamran.ttf";
        Typeface putfont = Typeface.createFromAsset(context.getAssets(), fontName);
        this.setTypeface(putfont);
    }
}
