package com.dilanjan.dccalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "diladebug";
    String valOne;
    String valTwo;

    Button btnClear;
    Button btnEqual;

    Button btnOne;
    Button btnTwo;
    Button btnThree;
    Button btnFour;
    Button btnFive;
    Button btnSix;
    Button btnSeven;
    Button btnEight;
    Button btnNine;
    Button btnZero;
    Button btnDot;

    Button btnAddition;
    Button btnSubtract;
    Button btnDivision;
    Button btnMultiply;

    String selectedOperator;
    Boolean isOperatorSelected = false;
    Boolean newValEntered = false;

    TextView outputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        outputDisplay = findViewById(R.id.outputDisplay);

        btnClear = findViewById(R.id.btnClear);
        btnEqual = findViewById(R.id.btnEqual);

        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
        btnSix = findViewById(R.id.btnSix);
        btnSeven = findViewById(R.id.btnSeven);
        btnEight = findViewById(R.id.btnEight);
        btnNine = findViewById(R.id.btnNine);
        btnZero = findViewById(R.id.btnZero);
        btnDot = findViewById(R.id.btnDot);

        btnAddition = findViewById(R.id.btnAddition);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnDivision = findViewById(R.id.btnDivision);
        btnMultiply = findViewById(R.id.btnMultiply);

        btnOne.setOnClickListener( appendToDisplay("1") );
        btnTwo.setOnClickListener( appendToDisplay("2") );
        btnThree.setOnClickListener( appendToDisplay("3") );
        btnFour.setOnClickListener( appendToDisplay("4") );
        btnFive.setOnClickListener( appendToDisplay("5") );
        btnSix.setOnClickListener( appendToDisplay("6") );
        btnSeven.setOnClickListener( appendToDisplay("7") );
        btnEight.setOnClickListener( appendToDisplay("8") );
        btnNine.setOnClickListener( appendToDisplay("9") );
        btnZero.setOnClickListener( appendToDisplay("0") );

        btnAddition.setOnClickListener( handleOperator("+") );
        btnSubtract.setOnClickListener( handleOperator("-") );
        btnMultiply.setOnClickListener( handleOperator("*") );
        btnDivision.setOnClickListener( handleOperator("/") );

        btnEqual.setOnClickListener( calculateResult() );

        btnDot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueOnDisplay = outputDisplay.getText().toString();
                int dotCount = valueOnDisplay.length() - valueOnDisplay.replace(".", "").length();

                if (dotCount == 0) {
                    outputDisplay.append(".");
                }
            }
        });

        btnClear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valOne = "";
                valTwo = "";
                selectedOperator = "";
                isOperatorSelected = false;
                outputDisplay.setText("");
                resetButtonColors();
            }
        });
    }

    private View.OnClickListener calculateResult() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valOne != null) {
                    valTwo = outputDisplay.getText().toString();

                    doCalc();

                    valOne = null;
                    valTwo = null;
                    selectedOperator = "";
                    resetButtonColors();
                }
            }
        };
    }

    private View.OnClickListener appendToDisplay(String strVal) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOperatorSelected) {
                    outputDisplay.setText("");
                    isOperatorSelected = false;
                }
                outputDisplay.append(strVal);
                newValEntered = true;
            }
        };
    }

    private View.OnClickListener handleOperator(String strOperator) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset to normal button colors when press a operator
                resetButtonColors();

                // Highlight only the selected operator
                highlightSelectedOperator(strOperator);

                // Get what is typed on the display and the selected operator
                String outputDisplayVal = outputDisplay.getText().toString();
                double fvalDouble = Double.parseDouble(outputDisplayVal);

                if (valOne == null || valOne.isEmpty()) { // Value entered before the operator should be the valOne
                    valOne = String.valueOf(fvalDouble);
                    selectedOperator = strOperator;
                } else if (valTwo == null || valTwo.isEmpty()) {
                    if (newValEntered) {
                        valTwo = String.valueOf(fvalDouble);
                        doCalc();
                        valOne = String.valueOf(Double.parseDouble(outputDisplay.getText().toString()));
                        valTwo = null;
                        newValEntered = false;
                    }

                    selectedOperator = strOperator;
                }

                isOperatorSelected = true;
            }
        };
    }

    private void highlightSelectedOperator(String strOperator) {
        switch(strOperator) {
            case "+":
                btnAddition.setBackgroundColor(getResources().getColor(R.color.cal_highlight, getTheme()));
                break;
            case "-":
                btnSubtract.setBackgroundColor(getResources().getColor(R.color.cal_highlight, getTheme()));
                break;
            case "*":
                btnMultiply.setBackgroundColor(getResources().getColor(R.color.cal_highlight, getTheme()));
                break;
            case "/":
                btnDivision.setBackgroundColor(getResources().getColor(R.color.cal_highlight, getTheme()));
                break;
        }
    }

    private void resetButtonColors() {
        btnAddition.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnSubtract.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnMultiply.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnDivision.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
    }

    private void doCalc() {
        if (!valOne.isEmpty() && !valTwo.isEmpty() && !selectedOperator.isEmpty()) {
            double valOneDouble = Double.parseDouble(valOne);
            double valTwoDouble = Double.parseDouble(valTwo);
            double result = 0.0;
            boolean error = false;

            switch (selectedOperator) {
                case "+":
                    result = valOneDouble + valTwoDouble;
                    break;
                case "-":
                    result = valOneDouble - valTwoDouble;
                    break;
                case "*":
                    result = valOneDouble * valTwoDouble;
                    break;
                case "/":
                    if (valTwoDouble > 0) {
                        result = valOneDouble / valTwoDouble;
                    } else {
                        error = true;
                    }
                    break;
                default:
                    error = true;
            }

            if (error) {
                outputDisplay.setText("Error");
            } else {
                int resultInt = (int) result;

                if ((double) resultInt == result) {
                    outputDisplay.setText(Integer.toString(resultInt));
                } else {
                    outputDisplay.setText(Double.toString(result));
                }
            }
        }
    }
}