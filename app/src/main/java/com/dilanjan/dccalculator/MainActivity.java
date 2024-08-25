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
    String valOne; // stores the 1st value of expression
    String valTwo; // stores the 2nd value of the expression

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

    String selectedOperator; // stores the operator selected.
    Boolean isOperatorSelected = false;
    Boolean newValEntered = false;

    TextView outputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calculator); // see res/layout/calculator.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        outputDisplay = findViewById(R.id.outputDisplay);

        btnClear = findViewById(R.id.btnClear);
        btnEqual = findViewById(R.id.btnEqual);

        /*
         * Loaded buttons via res/layout/calculator.xml
         * to reuse variables defined.
         * to support themeing functions included.
         */
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

        /*
         * This is to handle the decimal point / floating number values.
         */
        btnDot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueOnDisplay = outputDisplay.getText().toString();
                int dotCount = valueOnDisplay.length() - valueOnDisplay.replace(".", "").length();

                if (dotCount == 0) { // Allow to add a decimal point if there are no decimal points before.
                    outputDisplay.append(".");
                }
            }
        });

        btnClear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { // reset to the default when C button is pressed.
                valOne = null;
                valTwo = null;
                selectedOperator = "";
                isOperatorSelected = false;
                outputDisplay.setText("");
                resetButtonColors();
            }
        });
    }

    /*
     * Do the calculation when Equal button is pressed
     *
     * @return
     */
    private View.OnClickListener calculateResult() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valOne != null) {
                    valTwo = outputDisplay.getText().toString(); // Take what is displayed on display

                    doCalc();

                    valOne = null; // Reset the valOne
                    valTwo = null; // Reset the valTwo
                    selectedOperator = ""; // Reset selectedOperator
                    resetButtonColors(); // Reset operator button styles if selected.
                }
            }
        };
    }

    /*
     * Appends the character pressed to the existing value displayed on the screen.
     */
    private View.OnClickListener appendToDisplay(String strVal) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOperatorSelected) { // if a operator is selected, Need to restart what needs to be displayed on display.
                    outputDisplay.setText("");
                    isOperatorSelected = false;
                }
                outputDisplay.append(strVal); // Appends the value of strVal to the value currently displayed.
                newValEntered = true; // appending value changed the displayed value. no operator is pressed after append.
            }
        };
    }

    /*
     * Handle the scenarios when a operator is pressed.
     */
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
                double fvalDouble = Double.parseDouble(outputDisplayVal); // String to double conversion

                if (valOne == null || valOne.isEmpty()) { // Value entered before the operator has to be the valOne
                    valOne = String.valueOf(fvalDouble);
                    selectedOperator = strOperator;
                } else if (valTwo == null || valTwo.isEmpty()) { // Perform calculation only if the valOne is present and valTwo is not set
                    if (newValEntered) { // Allowed to change the operator if new value not appended.
                        valTwo = String.valueOf(fvalDouble);
                        doCalc(); // Perform calculation
                        valOne = String.valueOf(Double.parseDouble(outputDisplay.getText().toString())); // Re assign the result to valOne for subsequent calculations
                        valTwo = null; // Reset valTwo to facilitate subsequent calculations
                        newValEntered = false; // Calculation done, can await for number input next.
                    }

                    selectedOperator = strOperator;
                }

                isOperatorSelected = true;
            }
        };
    }

    /*
     * This changes the color of selected operator when pressed.
     *
     * @param strOperator
     */
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

    /*
     * This clears any operator colors which have set as background.
     */
    private void resetButtonColors() {
        btnAddition.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnSubtract.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnMultiply.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
        btnDivision.setBackgroundColor(getResources().getColor(R.color.cal_primary, getTheme()));
    }

    /*
     * This does the calculation based on the two provided values.
     */
    private void doCalc() {
        // Calculation is performed only if the valOne, valTwo is set and also the operator is selected.
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
                    if (valTwoDouble > 0) { // Division by zero case handled.
                        result = valOneDouble / valTwoDouble;
                    } else {
                        error = true;
                    }
                    break;
                default:
                    error = true;
            }

            if (error) { // if there is an error display error.
                outputDisplay.setText("Error");
            } else {
                int resultInt = (int) result;

                // if the result has a value like 123.0 then check and remove the trailing zero and . to make it look like an int.
                if ((double) resultInt == result) {
                    outputDisplay.setText(Integer.toString(resultInt));
                } else {
                    outputDisplay.setText(Double.toString(result));
                }
            }
        }
    }
}