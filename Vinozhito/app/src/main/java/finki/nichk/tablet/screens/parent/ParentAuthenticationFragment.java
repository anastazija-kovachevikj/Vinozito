package finki.nichk.tablet.screens.parent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import finki.nichk.R;
import finki.nichk.logic.StringValuePair;

public class ParentAuthenticationFragment extends DialogFragment {
    int count = 0; // counter -> determines position of input field
    StringValuePair[] values = {
            new StringValuePair("ЕДЕН", 1),
            new StringValuePair("ДВА", 2),
            new StringValuePair("ТРИ", 3),
            new StringValuePair("ЧЕТИРИ", 4),
            new StringValuePair("ПЕТ", 5),
            new StringValuePair("ШЕСТ", 6),
            new StringValuePair("СЕДУМ", 7),
            new StringValuePair("ОСУМ", 8),
            new StringValuePair("ДЕВЕТ", 9),
            new StringValuePair("НУЛА", 0)
    };
    List<StringValuePair> shuffledValues = new ArrayList<>(Arrays.asList(values));
    int digit1_value;
    int digit2_value;
    int digit3_value;
    int digit4_value;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false); // prevent dismiss on outside touch or back button press
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.tablet_parent_auth, container, false);
        Objects.requireNonNull(getDialog()).setCancelable(false);

        // THE BUTTONS
        Button button0 = mainView.findViewById(R.id.button_0);
        Button button1 = mainView.findViewById(R.id.button_1);
        Button button2 = mainView.findViewById(R.id.button_2);
        Button button3 = mainView.findViewById(R.id.button_3);
        Button button4 = mainView.findViewById(R.id.button_4);
        Button button5 = mainView.findViewById(R.id.button_5);
        Button button6 = mainView.findViewById(R.id.button_6);
        Button button7 = mainView.findViewById(R.id.button_7);
        Button button8 = mainView.findViewById(R.id.button_8);
        Button button9 = mainView.findViewById(R.id.button_9);

        ImageButton clear = mainView.findViewById(R.id.button_clear);
        ImageButton closeButton = mainView.findViewById(R.id.close_btn);

        // THE DIGITS TO ENTER
        TextView digit_1 = mainView.findViewById(R.id.number_text1);
        TextView digit_2 = mainView.findViewById(R.id.number_text2);
        TextView digit_3 = mainView.findViewById(R.id.number_text3);
        TextView digit_4 = mainView.findViewById(R.id.number_text4);

        // randomly select 4 numbers (shuffle)
        Collections.shuffle(shuffledValues);

        // take first 4 elements from the shuffled array
        List<StringValuePair> selectedValues = shuffledValues.subList(0, 4);

        for (StringValuePair valuePair : selectedValues) {
            Log.d("Selected Value", valuePair.getString() + " - " + valuePair.getValue());
        }

        digit_1.setText(selectedValues.get(0).getString());
        digit_2.setText(selectedValues.get(1).getString());
        digit_3.setText(selectedValues.get(2).getString());
        digit_4.setText(selectedValues.get(3).getString());

        digit1_value = selectedValues.get(0).getValue();
        digit2_value = selectedValues.get(1).getValue();
        digit3_value = selectedValues.get(2).getValue();
        digit4_value = selectedValues.get(3).getValue();

        // BUTTON LISTENERS
        buttonTouchListener(button0, () -> appendNumberToField(mainView, "0"));
        buttonTouchListener(button1, () -> appendNumberToField(mainView, "1"));
        buttonTouchListener(button2, () -> appendNumberToField(mainView, "2"));
        buttonTouchListener(button3, () -> appendNumberToField(mainView, "3"));
        buttonTouchListener(button4, () -> appendNumberToField(mainView, "4"));
        buttonTouchListener(button5, () -> appendNumberToField(mainView, "5"));
        buttonTouchListener(button6, () -> appendNumberToField(mainView, "6"));
        buttonTouchListener(button7, () -> appendNumberToField(mainView, "7"));
        buttonTouchListener(button8, () -> appendNumberToField(mainView, "8"));
        buttonTouchListener(button9, () -> appendNumberToField(mainView, "9"));

        buttonTouchListener(clear, () -> clearField(mainView));
        buttonTouchListener(closeButton, () -> requireActivity().finish());
        //clear.setOnClickListener(v -> clearField(mainView));
        //closeButton.setOnClickListener(v -> requireActivity().finish());

        return mainView;
    }

    private void clearField(View mianView) {
        if (count == 0) {
            return;
        }

        String input = "input" + count;
        count--;

        TextView inputField = mianView.findViewWithTag(input);
        inputField.setText("");
    }

    private void appendNumberToField(View mainView, String number) {
        count++;
        String input = "input" + count;

        // find the input field by tag
        TextView inputField = mainView.findViewWithTag(input);

        if (inputField != null) {
            inputField.setText(number);
        }

        if (count == 4) {
            Log.e("Authentication", "NO MORE FIELDS"); // now we check
            check(mainView);
        }
    }

    private void check(View mainView) {

        TextView inputField_1 = mainView.findViewById(R.id.number_input1);
        TextView inputField_2 = mainView.findViewById(R.id.number_input2);
        TextView inputField_3 = mainView.findViewById(R.id.number_input3);
        TextView inputField_4 = mainView.findViewById(R.id.number_input4);

        System.out.println(inputField_1.getText());
        System.out.println(inputField_2.getText());
        System.out.println(inputField_3.getText());
        System.out.println(inputField_4.getText());

        String first = (String) inputField_1.getText();
        String second = (String) inputField_2.getText();
        String third = (String) inputField_3.getText();
        String forth = (String) inputField_4.getText();

        String d1 = Integer.toString(digit1_value);
        String d2 = Integer.toString(digit2_value);
        String d3 = Integer.toString(digit3_value);
        String d4 = Integer.toString(digit4_value);

//        System.out.println("->" + first);
//        System.out.println("->" + second);
//        System.out.println("->" + third);
//        System.out.println("->" + forth);

//        System.out.println("->" + digit1_value);
//        System.out.println("->" + digit2_value);
//        System.out.println("->" + digit3_value);
//        System.out.println("->" + digit4_value);

        ParentActivity parentActivity = (ParentActivity) getActivity();
        // compare numbers as strings
        if (d1.equals(first) && d2.equals(second) && d3.equals(third) && d4.equals(forth)) { // AUTH PASSED
            //System.out.println("YEAH BOI");
            if (parentActivity != null) {
                // set authenticated to true and save it to SharedPreferences
                parentActivity.setAuthenticated(true);
                dismiss(); // dismiss fragment
            }

        } else { // AUTH NOT PASSED
            assert parentActivity != null;
            parentActivity.setAuthenticated(false);

            // blinking animation for input fields
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(150);
            anim.setStartOffset(10);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(2); // number of blinks

            inputField_1.startAnimation(anim);
            inputField_2.startAnimation(anim);
            inputField_3.startAnimation(anim);
            inputField_4.startAnimation(anim);

            // reset input fields and counter
            inputField_1.setText("");
            inputField_2.setText("");
            inputField_3.setText("");
            inputField_4.setText("");
            count = 0;

            //System.out.println("sadness");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(Button button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // transparent when pressed
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // back to normal released or canceled
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // transparent when pressed
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // back to normal released or canceled
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }
}
