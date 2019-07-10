package com.psx.simplemaths;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.psx.commons.ExchangeObject;
import com.psx.commons.Modules;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CalculationActivity extends AppCompatActivity {

    @BindView(R2.id.et_number_1)
    public EditText operandOne;
    @BindView(R2.id.et_number_2)
    public EditText operandTwo;
    @BindView(R2.id.tv_operation)
    public TextView operationSymbol;

    // Radio Buttons
    @BindView(R2.id.rb_add)
    public RadioButton opAdd;
    @BindView(R2.id.rb_sub)
    public RadioButton opSub;
    @BindView(R2.id.rb_mult)
    public RadioButton opMult;
    @BindView(R2.id.rb_div)
    public RadioButton opDiv;

    @BindView(R2.id.rg_operation_select)
    public RadioGroup operationSelect;
    @BindView(R2.id.tv_result)
    public TextView operationResult;

    private SupportedOperations selectedOperation = SupportedOperations.ADDITION;
    private Unbinder unbinder = null;
    private static final String TAG = CalculationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        unbinder = ButterKnife.bind(this);
        operationSelect.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == opAdd.getId()) {
                selectedOperation = SupportedOperations.ADDITION;
                operationSymbol.setText("+");
            } else if (checkedId == opSub.getId()) {
                selectedOperation = SupportedOperations.SUBTRACTION;
                operationSymbol.setText("-");
            } else if (checkedId == opDiv.getId()) {
                selectedOperation = SupportedOperations.DIVISION;
                operationSymbol.setText("/");
            } else if (checkedId == opMult.getId()) {
                selectedOperation = SupportedOperations.MULTIPLICATION;
                operationSymbol.setText("*");
            }
        });
    }

    public void onCalculateButtonClicked(View view) {
        if (readyForCalculation()) {
            double result = SimpleMath.performCalculation(Double.parseDouble(operandOne.getText().toString()), Double.parseDouble(operandTwo.getText().toString()), selectedOperation);
            operationResult.setText(String.valueOf(result));
            SimpleMath.sendCalculationCompleteEvent(createExchangeObjectToSend(selectedOperation));
        }
    }

    private ExchangeObject createExchangeObjectToSend(SupportedOperations selectedOperation) {
        Object[] data = {selectedOperation.toString()};
        return new ExchangeObject(data, "RESULT",
                Modules.AFTER_EFFECTS, Modules.SIMPLE_MATHS);
    }

    public boolean readyForCalculation() {
        if (TextUtils.isEmpty(operandOne.getText()) || TextUtils.isEmpty(operandTwo.getText())) {
            showToast("One of operands is empty", Toast.LENGTH_SHORT);
            return false;
        } else if (selectedOperation == SupportedOperations.DIVISION && operandTwo.getText().toString().equals("0")) {
            showToast("Division by zero not allowed", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }


    public void showToast(String message, int toastDuration) {
        if (SimpleMath.getApplicationInstance() == null) {
            Log.e(TAG, "Application Instance is Null.");
            return;
        }
        Toast.makeText(SimpleMath.getApplicationInstance().getCurrentApplication(), message, toastDuration).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
