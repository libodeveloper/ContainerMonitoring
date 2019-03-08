package com.esri.arcgisruntime.container.monitoring.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by libo on 2019/3/8.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class InputUtils {

    /**
     * 输入限止  对EditText限止输入 true表示限制,false表示不限制
     * @author zealjiang
     * @time 2017/2/24 10:48
     * @param editText
     * @param face  表情
     * @param chinese 中文
     * @param number  数字
     * @param english 英文
     */
    public void inputRestrict(final EditText editText, boolean face, boolean chinese, boolean number, boolean english){

        String sFace = ".*[\ud83c\udc00-\ud83c\udfff].*|.*[\ud83d\udc00-\ud83d\udfff].*|.*[\u2600-\u27ff].*";
        String sChinise = ".*[\u4e00-\u9fa5].*";
        String sNumber = ".*[0-9].*";
        String sEnglish = ".*[a-zA-Z].*";
        StringBuilder sRestric = new StringBuilder();
        StringBuilder sHint = new StringBuilder();
        if(face){
            sRestric.append(sFace);
            sHint.append("不可输入表情符");
        }
        if(chinese){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sChinise) : sRestric.append(sChinise);
            sHint = sHint.length()>0 ? sHint.append("、").append("汉字") : sHint.append("不可输入汉字");
        }
        if(number){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sNumber) : sRestric.append(sNumber);
            sHint = sHint.length()>0 ? sHint.append("、").append("数字") : sHint.append("不可输入数字");
        }
        if(english){
            sRestric = sRestric.length()>0 ? sRestric.append("|").append(sEnglish) : sRestric.append(sEnglish);
            sHint = sHint.length()>0 ? sHint.append("、").append("英文字母") : sHint.append("不可输入英文字母");
        }

        final String restric = sRestric.toString();
        final String hint = sHint.toString();

        editText.addTextChangedListener(new TextWatcher() {
            String beforeText = "";
            String changedText = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //LogUtil.e("onTextChanged string","start :"+start+"  before: "+before+"  count: "+count);
                //增加字符
                if(before==0&&count>0) {
                    changedText = s.subSequence(start, start + count).toString();
                }else{
                    changedText = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(changedText.equals("")){
                    return;
                }
                //LogUtil.e("afterTextChanged changedText",changedText);
                Pattern p = Pattern.compile(restric);
                Matcher m = p.matcher(changedText);
                if(m.matches()){
                    editText.removeTextChangedListener(this);
                    editText.setText(beforeText);
                    editText.setSelection(editText.getText().length());
                    MyToast.showShort(hint);
                    editText.addTextChangedListener(this);
                }
            }
        });
    }

}
