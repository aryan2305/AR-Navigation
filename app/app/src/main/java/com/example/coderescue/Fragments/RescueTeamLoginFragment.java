package com.example.coderescue.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.coderescue.Activities.RescueTeamDashboard;
import com.example.coderescue.R;
import soup.neumorphism.ShapeType;


public class RescueTeamLoginFragment extends Fragment {

    EditText username,password;
    soup.neumorphism.NeumorphImageButton showPassword;
    soup.neumorphism.NeumorphButton submit;

    private boolean password_notvisible = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_rescue_team_login, container, false);

        username = root.findViewById(R.id.username);
        password = root.findViewById(R.id.password);
        showPassword = root.findViewById(R.id.show_password);
        submit = root.findViewById(R.id.submit);

        root.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.image, null));
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_notvisible) {
                    showPassword.setShapeType(ShapeType.PRESSED);
                    password_notvisible = false;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                }
                else{
                    showPassword.setShapeType(ShapeType.FLAT);
                    password_notvisible = true;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        return root;
    }

    public void loginAction() {
        Intent intent = new Intent(getActivity(), RescueTeamDashboard.class);

        String user = username.getText().toString();
        String pswd = password.getText().toString();
        if(user.equals("Graphics") && pswd.equals(("Graphics"))){
            Log.d("Correct Sign In", "Correct username and password");
            intent.putExtra("username", user);
//            intent.putExtra("disaster_id", items.get(0).getString("disaster_id"));
            startActivity(intent);
        }
        else{
            Log.d("Incorrect Sign In", "Wrong username or password");
            CharSequence text = "Invalid username or password";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }
    }

}
