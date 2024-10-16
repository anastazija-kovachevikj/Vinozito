package finki.nichk.tablet.widgets;

import android.widget.ImageButton;

public class AudioImageButton {
    ImageButton imageButton;
    public String audioUrl;

    public AudioImageButton(ImageButton imageButton, String audioUrl) {
        this.imageButton = imageButton;
        this.audioUrl = audioUrl;
    }
}
