package com.jxkj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Desc:
 * Author:zhujb
 * Date:2020/7/6
 */
public class AlertDialogUtil {

    public static void showSingleButton(Activity context, String title, String message, String btnText, int textColor, Callback callback) {
        new Builder(context)
                .title(title)
                .content(message)
                .positiveColor(textColor)
                .positiveText(btnText)
                .onPositive(callback)
                .show();
    }

    public static void showSingleButton(Activity context, String message, String btnText, @ColorRes int textColor, Callback callback) {
        showSingleButton(context, context.getString(R.string.title_toast), message, btnText, textColor, callback);
    }

    public static void showSingleButton(Activity context, String message, String btnText, Callback callback) {
        showSingleButton(context, context.getString(R.string.title_toast), message, btnText, R.color.theme, callback);
    }

    public static void showSingleButton(Activity context, String message, String btnText, @ColorRes int textColor) {
        showSingleButton(context, context.getString(R.string.title_toast), message, btnText, textColor, null);
    }

    public static void showSingleButton(Activity context, String message, String btnText) {
        showSingleButton(context, context.getString(R.string.title_toast), message, btnText, R.color.theme, null);
    }

    public static void show(Activity context) {
        normal(context).show();
    }

    public static void show(Activity context, Callback positiveCallback, Callback negativeCallback) {
        normal(context).onPositive(positiveCallback).onNegative(negativeCallback).show();
    }

    public static void show(Activity context, String title, String message, String positiveText, String negativeText, @ColorRes int positiveColor, @ColorRes int negativeColor, Callback positiveCallback, Callback negativeCallback) {
        new Builder(context)
                .title(title)
                .content(message)
                .positiveColor(positiveColor)
                .positiveText(positiveText)
                .onPositive(positiveCallback)
                .negativeColor(negativeColor)
                .negativeText(negativeText)
                .onNegative(negativeCallback)
                .show();
    }

    public static void show(Activity context, String title, String message, String positiveText, String negativeText, Callback positiveCallback, Callback negativeCallback) {
        show(context, title, message, positiveText, negativeText, R.color.theme, R.color.gray_9, positiveCallback, negativeCallback);
    }

    public static void show(Activity context, String message, String positiveText, String negativeText, Callback positiveCallback, Callback negativeCallback) {
        show(context, context.getString(R.string.title_toast), message, positiveText, negativeText, positiveCallback, negativeCallback);
    }

    public static void show(Activity context, String message, String positiveText, String negativeText, Callback positiveCallback) {
        show(context, message, positiveText, negativeText, positiveCallback, null);
    }

    public static void show(Activity context, String message, String positiveText, String negativeText) {
        show(context, message, positiveText, negativeText, null);
    }

    public static void show(Activity context, String title, String message, String positiveText, @ColorRes int positiveColor, Callback positiveCallback) {
        show(context, title, message, positiveText, context.getString(R.string.cancel), positiveColor, R.color.gray_9, positiveCallback, null);
    }

    public static void show(Activity context, String message, @StringRes int positiveText, @ColorRes int positiveColor, Callback positiveCallback) {
        show(context, context.getString(R.string.title_toast), message, context.getString(positiveText), positiveColor, positiveCallback);
    }

    public static void show(Activity context, String message, String positiveText, @ColorRes int positiveColor, Callback positiveCallback) {
        show(context, context.getString(R.string.title_toast), message, positiveText, positiveColor, positiveCallback);
    }

    public static void show(Activity context, String message, String positiveText, Callback positiveCallback) {
        show(context, context.getString(R.string.title_toast), message, positiveText, R.color.theme, positiveCallback);
    }

    public static void show(Activity context, String message, @StringRes int positiveText, Callback positiveCallback) {
        show(context, context.getString(R.string.title_toast), message, context.getString(positiveText), R.color.theme, positiveCallback);
    }

    public static void show(Activity context, String message, Callback positiveCallback) {
        show(context, context.getString(R.string.title_toast), message, context.getString(R.string.confirm), R.color.theme, positiveCallback);
    }

    public static void showList(Activity context, String title, @ColorRes int titleColor, List items, ItemClickCallback callback) {
        new Builder(context).title(title).titleColorRes(titleColor).items(items).itemsCallBack(callback).show();
    }

    public static void showList(Activity context, String title, List items, ItemClickCallback callback) {
        new Builder(context).title(title).items(items).itemsCallBack(callback).show();
    }

    public static Builder normalSingle(Activity context) {
        return new Builder(context)
                .title(R.string.title_toast)
                .positiveColor(R.color.theme)
                .positiveText(R.string.confirm);
    }

    public static Builder simple(Activity context) {
        return new Builder(context).title(R.string.title_toast);
    }

    public static Builder normal(Activity context) {
        return new Builder(context)
                .title(R.string.title_toast)
                .positiveColor(R.color.theme)
                .positiveText(R.string.confirm)
                .negativeColor(R.color.gray_9)
                .negativeText(R.string.cancel);
    }

    public static Builder normalColor(Activity context) {
        return new Builder(context)
                .title(R.string.title_toast)
                .positiveColor(R.color.theme)
                .negativeColor(R.color.gray_9);
    }

    public static Builder normalPositive(Activity context) {
        return new Builder(context)
                .title(R.string.title_toast)
                .positiveColor(R.color.theme)
                .positiveText(R.string.confirm);
    }

    public static Builder normalNegative(Activity context) {
        return new Builder(context)
                .title(R.string.title_toast)
                .negativeColor(R.color.gray_9)
                .negativeText(R.string.cancel);
    }

    public static class Builder {
        private MaterialDialog.Builder builder;
        private Context context;
        private MaterialDialog materialDialog;
        private boolean isCircular;

        public Builder(@NonNull Activity context) {
            builder = new MaterialDialog.Builder(context);
        }


        public Builder title(int titleRes) {
            builder.title(titleRes);
            return this;
        }

        public Builder isCircular(boolean isCircular) {
            this.isCircular = isCircular;
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            builder.title(title);
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            builder.titleColorRes(colorRes);
            return this;
        }


        public Builder titleGravity(@DialogGravity int gravity) {
            builder.titleGravity(gravity == Gravity.START ? GravityEnum.START : (gravity == Gravity.CENTER ? GravityEnum.CENTER : GravityEnum.END));
            return this;
        }

        public Builder content(@StringRes int messageRes) {
            builder.content(messageRes);
            return this;
        }

        public Builder content(@NonNull CharSequence message) {
            builder.content(message);
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceled) {
            builder.canceledOnTouchOutside(canceled);
            return this;
        }

        public Builder cancelable(boolean canceled) {
            builder.cancelable(canceled);
            return this;
        }

        public Builder items(List list) {
            builder.items(list);
            return this;
        }

        public Builder items(String... items) {
            builder.items(items);
            return this;
        }

        public Builder input(@Nullable CharSequence hint, @Nullable CharSequence preFill, InputCallback callback) {
            builder.input(hint, preFill, ((dialog, input) -> {
                if (callback != null) callback.onInput(input);
            }));
            return this;
        }

        public Builder customView(@LayoutRes int layoutRes) {
            builder.customView(layoutRes, false);
            return this;
        }

        public Builder customView(View contentView) {
            builder.customView(contentView, false);
            return this;
        }

        public Builder inputType(int inputType) {
            builder.inputType(inputType);
            return this;
        }

        public Builder itemsCallBack(ItemClickCallback clickCallback) {
            builder.itemsCallback(((dialog, itemView, position, text) -> clickCallback.onItemClick(position, text)));
            return this;
        }

        public Builder positiveColor(@ColorRes int color) {
            builder.positiveColorRes(color);
            return this;
        }

        public Builder positiveText(int positiveRes) {
            builder.positiveText(positiveRes);
            return this;
        }


        public Builder positiveText(@NonNull CharSequence message) {
            builder.positiveText(message);
            return this;
        }


        public Builder onPositive(Callback callback) {
            if (callback != null) {
                builder.onPositive(((dialog, which) -> {
                    callback.onClick();
                    materialDialog = null;
                }));
            }
            return this;
        }


        public Builder negativeColor(@ColorRes int color) {
            builder.negativeColorRes(color);
            return this;
        }


        public Builder negativeColor(@NonNull ColorStateList colorStateList) {
            builder.negativeColor(colorStateList);
            return this;
        }

        public Builder negativeText(int negativeRes) {
            builder.negativeText(negativeRes);
            return this;
        }


        public Builder negativeText(@NonNull CharSequence message) {
            builder.negativeText(message);
            return this;
        }


        public Builder onNegative(Callback callback) {
            if (callback != null) {
                builder.onNegative(((dialog, which) -> {
                    callback.onClick();
                    materialDialog = null;
                }));
            }
            return this;
        }

        public Builder show(float scaleX) {
            materialDialog = builder.show();
            Window window = materialDialog.getWindow();
            assert window != null;
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
            // 设置宽度
            p.width = (int) (Tools.getScreenWidth(materialDialog.getContext()) * scaleX); // 宽度设置为屏幕的scaleX倍
            p.gravity = android.view.Gravity.CENTER;//设置位置
            window.setAttributes(p);
            return this;
        }

        public Builder show() {
            if (isCircular) {
                materialDialog = builder.build();
                Objects.requireNonNull(materialDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                materialDialog.show();
            } else {
                materialDialog = builder.show();
            }
            return this;
        }

        public void cancel() {
            cancel(false);
        }

        public void cancel(boolean hideInput) {
            if (materialDialog != null) {
                if (hideInput) {
                    View customView = materialDialog.getCustomView();
                    if (customView != null) {
                        InputMethodManager imm = (InputMethodManager) customView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(customView.getWindowToken(), 0);
                    }
                }
                materialDialog.cancel();
                materialDialog = null;
            }
        }
    }

    public interface Callback {
        void onClick();
    }

    public interface ItemClickCallback {
        void onItemClick(int position, CharSequence text);
    }

    public interface InputCallback {
        void onInput(CharSequence input);
    }

    public interface Gravity {
        int START = 0;
        int CENTER = 1;
        int END = 2;
    }

    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Gravity.CENTER, Gravity.END, Gravity.START})
    public @interface DialogGravity {

    }
}
