package gr.sieben.demos.beanshelldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;
import bsh.TargetError;


public class MainActivity extends AppCompatActivity {

    private EditText javaCode;
    private final static Interpreter interpreter = new Interpreter();

    private void initBeanshell() {
        try {
            interpreter.set("context", this);//set any variable, you can refer to it directly from string
            //interpreter.set("rootView", findViewById(android.R.id.content));
            interpreter.set("code", findViewById(R.id.editTextCode));
            interpreter.set("button", findViewById(R.id.buttonRun));
            if (interpreter.get("portnum") == null) { // server not set
                interpreter.set("portnum", 1234);
                interpreter.eval("setAccessibility(true)"); // turn off access restrictions
                interpreter.eval("server(portnum)");
            }
        } catch (TargetError e) {
            Throwable t = e.getTarget();
            Toast.makeText(MainActivity.this, "ScriptThrewException:" + t, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "ParseError:" + e.getErrorText(), Toast.LENGTH_SHORT).show();
        } catch (EvalError e) {
            Toast.makeText(MainActivity.this, "EvalError:" + e.getErrorText(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {//handle exception
            e.printStackTrace();
        }
    }

    private void runString(String code) {

        try {
            interpreter.eval(code);//execute code
        } catch (TargetError e) {
            Throwable t = e.getTarget();
            Toast.makeText(MainActivity.this, "ScriptThrewException:" + t, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "ParseError:" + e.getErrorText(), Toast.LENGTH_SHORT).show();
        } catch (EvalError e) {
            Toast.makeText(MainActivity.this, "EvalError:" + e.getErrorText(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {//handle exception
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBeanshell();

        javaCode = (EditText) findViewById(R.id.editTextCode);
        javaCode.setText("import android.widget.Toast; \n" +
                "import android.support.design.widget.Snackbar;\n " +
                "import android.view.View;\n" +
                "import android.view.View.OnClickListener; \n" +
                "Toast.makeText(context, \"This is a Toast from beanshell\", Toast.LENGTH_SHORT).show(); \n" +
                "Snackbar.make( code,\"This is a Snackbar from beanshell\",Snackbar.LENGTH_LONG).show(); \n" +
                "code.post(new Runnable() {public void run() {button.setText(\"Changed from code\");} }); \n" +
                "code.setOnClickListener(new OnClickListener() {public void onClick(View view) " +
                "{Toast.makeText(context, \"Listener from Code\", Toast.LENGTH_SHORT).show();}});");


        Button runCode = (Button) findViewById(R.id.buttonRun);

        runCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                runString(javaCode.getText().toString());
            }
        });

    }

}

