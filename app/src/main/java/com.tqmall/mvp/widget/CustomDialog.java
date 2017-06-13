package com.tqmall.mvp.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tqmall.R;

/**
 * Created by Jay on 16/12/31.
 */


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private Integer checkedQty;
        private Integer qty;
        private DialogInterface.OnClickListener positiveButtonClickListener;
//        private DialogInterface.OnClickListener negativeButtonClickListener;
        public PriorityListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public interface PriorityListener{
            void negativeButtonClickListener(DialogInterface dialog,int which, String changeQty);
        }

        public Builder setPriorityListener(PriorityListener negativeButtonClickListener){
            this.negativeButtonClickListener = negativeButtonClickListener;
            return this;
        }

        public Builder setMessage(Integer checkedQty) {
            this.checkedQty = checkedQty;
            return this;
        }

//        /**
//         * Set the Dialog message from resource
//         *
//         * @param checkedQty
//         * @return
//         */
//        public Builder setMessage(String message) {
//            this.message = (String) context.getText(message);
//            return this;
//        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                        PriorityListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         PriorityListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setCheckedQty(Integer checkedQty) {
            this.checkedQty = checkedQty;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_inventory_init_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            final EditText etCheckedQty = (EditText) layout.findViewById(R.id.message);
            etCheckedQty.setText(checkedQty == null ? "0" : (checkedQty+""));
            etCheckedQty.selectAll();
            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            //设置盘点数量
            ((ImageView) layout.findViewById(R.id.ivAdd)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = etCheckedQty.getText().toString().trim();
                    try {
                        if (!TextUtils.isEmpty(str)) {
                            Integer changeQtyNum = Integer.valueOf(str);
                            if (changeQtyNum != null) {
                                changeQtyNum++;
                                etCheckedQty.setText(changeQtyNum + "");
                                etCheckedQty.selectAll();
                            } else if (str.equals("null")) {
                                etCheckedQty.setText(0 + "");
                                etCheckedQty.selectAll();
                            } else {
                                checkedQty = 1;
                                etCheckedQty.setText(checkedQty + "");
                                etCheckedQty.selectAll();
                            }

                        }
                    } catch (Exception e) {
                        etCheckedQty.setText(0 + "");
                        etCheckedQty.selectAll();
                    }
                }
            });

            ((ImageView) layout.findViewById(R.id.ivSubtract)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = etCheckedQty.getText().toString().trim();

                    try {
                        if (!TextUtils.isEmpty(str)) {
                            Integer changeQtyNum = Integer.valueOf(str);
                            if (changeQtyNum != null && changeQtyNum != 0) {
                                changeQtyNum--;
                                etCheckedQty.setText(changeQtyNum + "");
                                etCheckedQty.selectAll();
                            } else if (str.equals("null")) {
                                etCheckedQty.setText(0 + "");
                                etCheckedQty.selectAll();
                            } else {
                                checkedQty = 0;
                                etCheckedQty.setText(checkedQty + "");
                                etCheckedQty.selectAll();
                            }
                        } else {
                            etCheckedQty.setText(0 + "");
                            etCheckedQty.selectAll();
                        }

                    } catch (Exception e) {
                        etCheckedQty.setText(0 + "");
                        etCheckedQty.selectAll();
                    }
//                    if (checkedQty != null && checkedQty != 0) {
//                        checkedQty--;
//                        etCheckedQty.setText(checkedQty + "");
//                        etCheckedQty.selectAll();
//                    }
                }
            });


            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
//                                    negativeButtonClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_NEGATIVE);
                                        try {
                                            String str=etCheckedQty.getText().toString().trim();
                                            if(!TextUtils.isEmpty(str)){
                                                qty = Integer.valueOf(str);
                                                negativeButtonClickListener.negativeButtonClickListener(dialog,
                                                        DialogInterface.BUTTON_POSITIVE,str);
                                            }else {
                                                negativeButtonClickListener.negativeButtonClickListener(dialog,
                                                        DialogInterface.BUTTON_POSITIVE,"");
                                            }
                                        }catch (Exception e){

                                        }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (checkedQty != null) {
//                ((EditText) layout.findViewById(R.id.message)).setText(checkedQty+"");

            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}