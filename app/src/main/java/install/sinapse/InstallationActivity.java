package install.sinapse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                    Intent intent = new Intent(InstallationActivity.this,
                            Install_sinapseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}