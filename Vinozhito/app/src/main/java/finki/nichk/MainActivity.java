package finki.nichk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check and install Google Text-to-Speech engine
        checkAndInstallGoogleTTS();
    }

    private void checkAndInstallGoogleTTS() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // TTS data is installed, initialize TextToSpeech
                initializeTextToSpeech();
            } else {
                // TTS data is not installed, prompt the user to install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    private void initializeTextToSpeech() {
        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Specify the language you want, for example, Macedonian
                    Locale macedonian = new Locale("mk", "MK");

                    // Check if the language is supported
                    int result = textToSpeech.setLanguage(macedonian);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle the case where the language is not supported
                        Log.e("TTS", "Macedonian Language not supported. Falling back to English");

                        // Fallback to English if Macedonian is not supported
                        Locale fallbackLanguage = Locale.ENGLISH;
                        result = textToSpeech.setLanguage(fallbackLanguage);

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "Fallback language (English) not supported");
                        } else {
                            // The language is supported, you can now use TTS in English
                            textToSpeech.speak("Hello", TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    } else {
                        // The language is supported, you can now use TTS in Macedonian
                        textToSpeech.speak("Zdravo", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                } else {
                    // Handle the initialization failure
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}