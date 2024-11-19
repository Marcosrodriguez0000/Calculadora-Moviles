package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private StringBuilder currentInput = new StringBuilder();
    private Stack<Double> values = new Stack<>();
    private Stack<Character> operators = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnEquals, R.id.btnDot, R.id.btnClear
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this::onButtonClick);
        }
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "C":
                currentInput.setLength(0);
                values.clear();
                operators.clear();
                display.setText("0");
                break;
            case "=":
                if (currentInput.length() > 0) {
                    values.push(Double.parseDouble(currentInput.toString()));
                    currentInput.setLength(0);
                }
                while (!operators.isEmpty()) {
                    performOperation();
                }
                display.setText(values.isEmpty() ? "0" : String.valueOf(values.pop()));
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                if (currentInput.length() > 0) {
                    values.push(Double.parseDouble(currentInput.toString()));
                    currentInput.setLength(0);
                }
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(buttonText.charAt(0))) {
                    performOperation();
                }
                operators.push(buttonText.charAt(0));
                break;
            default:
                currentInput.append(buttonText);
                display.setText(currentInput.toString());
                break;
        }
    }

    private void performOperation() {
        if (values.size() < 2 || operators.isEmpty()) return;
        double b = values.pop();
        double a = values.pop();
        char op = operators.pop();
        switch (op) {
            case '+': values.push(a + b); break;
            case '-': values.push(a - b); break;
            case '*': values.push(a * b); break;
            case '/': values.push(a / b); break;
        }
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}