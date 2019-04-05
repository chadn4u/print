package com.example.user.print.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class NumericKeyboardTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return source;
    }
}
