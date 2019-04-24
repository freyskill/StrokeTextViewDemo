package top.keepempty.stvd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字体描边
 * 可以设置数字字体和数字颜色
 * @author frey
 */
public class StrokeTextView extends AppCompatTextView {

    private static final String TAG = StrokeTextView.class.getSimpleName();

    private int mStrokeColor;
    // 是否描边，默认false
    private boolean isStroke;
    private int strokeWidth;
    private int mStrokeNumColor;
    //用于描边的TextView
    private TextView borderText = null;
    // 文本字体
    private Typeface typeface;
    // 数字字体
    private Typeface numTypeface;

    public StrokeTextView(Context context) {
        this(context,null);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        //获取自定义的XML属性名称
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        //获取对应的属性值
        this.isStroke = a.getBoolean(R.styleable.StrokeTextView_stv_strokeIs,false);
        this.mStrokeNumColor = a.getColor(R.styleable.StrokeTextView_stv_strokeNumColor,  getTextColors().getDefaultColor());
        if(isStroke){
            this.mStrokeColor = a.getColor(R.styleable.StrokeTextView_stv_strokeColor,0xffffff);
            this.strokeWidth = a.getInteger(R.styleable.StrokeTextView_stv_strokeWidth,5);
            borderText = new TextView(context,attrs,defStyle);
        }
        a.recycle();
        init();
    }


    /**
     * 设置描边颜色
     * @param color
     */
    public void setStrokeColor(int color){
        this.mStrokeColor = color;
    }

    /**
     * 设置描边宽度
     * @param width
     */
    public void setStrokeWidth(int width){
        this.strokeWidth = width;
    }

    /**
     * 设置数字字体颜色
     * @param color
     */
    public void setNumColor(int color){
        this.mStrokeNumColor = color;
    }

    /**
     * 设置字体
     * @param typeface 文本字体
     * @param numTypeface 数字字体
     */
    public void setStrokeTypeface(Typeface typeface,Typeface numTypeface) {
        this.typeface = typeface;
        this.numTypeface = numTypeface;
        if(typeface!=null){
            this.setTypeface(typeface);
        }
        init();
    }

    public void init(){
        if(isStroke){
            TextPaint tp1 = borderText.getPaint();
            //设置描边宽度
            tp1.setStrokeWidth(this.strokeWidth);
            tp1.setStyle(Paint.Style.STROKE);
            tp1.setStrokeCap(Paint.Cap.ROUND);
            tp1.setStrokeJoin(Paint.Join.ROUND);
            //设置描边颜色
            borderText.setTextColor(this.mStrokeColor);
            borderText.setGravity(getGravity());
            if(typeface!=null){
                borderText.setTypeface(typeface);
            }
        }
        setTextSpan(false);
    }


    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        if(isStroke){
            borderText.setLayoutParams(params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isStroke){
            setTextSpan(true);
            this.postInvalidate();
            borderText.measure(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        if(isStroke){
            borderText.layout(left, top, right, bottom);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isStroke){
            borderText.draw(canvas);
        }
        super.onDraw(canvas);
    }


    /**
     * 获取数字下标
     * @param content
     * @return
     */
    private static HashMap<Integer,Integer> getNumIndex(String content){
        HashMap<Integer,Integer> map = new HashMap<>();
        String reg = "\\d+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String num = matcher.group(0);
            int start = content.indexOf(matcher.group());
            int end = start + num.length();
            map.put(start,end);
        }
        return map;
    }

    /**
     * 判断一个字符串是否含有数字
     */
    public static boolean hasDigit(String content) {
        String reg = ".*\\d+.*";
        boolean flag = false;
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 设置字体，颜色
     * @param isStroke
     */
    private void setTextSpan(boolean isStroke){
        // 设置数字字体和颜色
        CharSequence text = getText();
        if(!TextUtils.isEmpty(text) && hasDigit(text.toString())){
            SpannableString spStr = new SpannableString(text);
            HashMap<Integer, Integer> numIndex = getNumIndex(text.toString());
            for (Map.Entry<Integer,Integer> index : numIndex.entrySet()){
                // 判断是否设置了数字字体
                int mTextColor;
                if(numTypeface!=null){
                    spStr.setSpan(new StrokeTypefaceSpan(numTypeface), index.getKey(), index.getValue(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if(isStroke){
                    mTextColor = mStrokeColor;
                }else{
                    mTextColor = mStrokeNumColor;
                }
                spStr.setSpan(new ForegroundColorSpan(mTextColor), index.getKey(), index.getValue(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if(isStroke){
                borderText.setText(spStr);
            }else{
                setText(spStr);
            }
        }
    }
}
