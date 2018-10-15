package install.sinapse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InstallationActivity extends FragmentActivity {
    private EditText installationIdEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installation_activity);

        installationIdEditText = (EditText) findViewById(R.id.installationIdEditText);
        Button startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (installationIdEditText != null
                        && installationIdEditText.getText() != null
                        && !installationIdEditText.getText().toString().isEmpty()) {
                    createFile(installationIdEditText.getText().toString());
                    Intent intent = new Intent(InstallationActivity.this, Install_sinapseActivity.class);
//                    Intent intent = new Intent(InstallationActivity.this, ComienzoInstalacion_activity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void createFile(String installationName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/sinapse/install/IdInstall.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(installationName.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}